package net.vakror.farmer.renderEngine;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.vakror.farmer.FarmerGameMain;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static net.vakror.farmer.FarmerGameMain.appDirPath;

public class ResourceDownloader {

    private static final String FILE_URL = "https://github.com/vaylor27/Game-Assets/archive/refs/heads/main.zip";

    public static void downloadResources() {
        File appDir = new File(appDirPath);
        if (doesNeedToDownloadResources()) {
            if (appDir.exists()) {
                deleteDirectory(appDir);
            }
            appDir.mkdirs();
            downloadAssets(appDir);
            renameDownloadedAssetsFolder(appDir);
        }
    }

    /**
     * Force deletion of directory
     */
    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return (path.delete());
    }

    private static void renameDownloadedAssetsFolder(File appDirPath) {
        File file = new File(appDirPath, "Game-Assets-main");
        File file1 = new File(appDirPath, "assets");
        if (file1.exists()) {
            file1.delete();
        }
        file.renameTo(file1);
    }

    public static boolean doesNeedToDownloadResources() {
        File appDir = new File(appDirPath);
        return !appDir.exists() || !appDir.isDirectory() || Objects.requireNonNull(appDir.list()).length == 0 || !isCorrectAssetVersion();
    }

    private static boolean isCorrectAssetVersion() {
        File assetInfoFile = new File(appDirPath, "assets/asset-info.json");
        if (!assetInfoFile.exists()) {
            return false;
        }
        try (FileReader reader = new FileReader(assetInfoFile)) {
            JsonElement element = JsonParser.parseReader(reader);
            if (!element.isJsonObject()) {
                return false;
            }
            JsonObject assetInfo = element.getAsJsonObject();
            if (!assetInfo.has("asset-version")) {
                return false;
            }
            JsonElement assetVersion = assetInfo.get("asset-version");
            if (!assetVersion.isJsonPrimitive() || !assetVersion.getAsJsonPrimitive().isNumber()) {
                return false;
            }
            return assetVersion.getAsJsonPrimitive().getAsInt() == FarmerGameMain.assetVersion;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void downloadAssets(File appDir) {
        byte[] readBytes = readAssetZip();
        ZipInputStream stream = new ZipInputStream(new ByteArrayInputStream(readBytes));
        readZipFile(stream, appDir);
    }

    private static ZipEntry currentZipEntry;
    private static byte[] buffer = new byte[1024];

    private static void readZipFile(ZipInputStream stream, File appDir) {
        try {
            currentZipEntry = stream.getNextEntry();
            while (currentZipEntry != null) {
                readZipEntry(stream, appDir);
            }

            stream.closeEntry();
            stream.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void readZipEntry(ZipInputStream stream, File appDir) throws IOException {
        File newFile = newFile(appDir, currentZipEntry);
        if (currentZipEntry.isDirectory()) {
            if (!newFile.isDirectory() && !newFile.mkdirs()) {
                throw new IOException("Failed to create directory " + newFile);
            }
        } else {
            // fix for Windows-created archives
            File parent = newFile.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("Failed to create directory " + parent);
            }

            // write file content
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = stream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
        }
        currentZipEntry = stream.getNextEntry();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private static byte[] readAssetZip() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                byteArrayOutputStream.write(dataBuffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            // handle exception
        }
        return new byte[0];
    }
}
