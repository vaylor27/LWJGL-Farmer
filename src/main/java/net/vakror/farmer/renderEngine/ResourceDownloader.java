package net.vakror.farmer.renderEngine;

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
                appDir.delete();
            }
            appDir.mkdirs();
            downloadAssets(appDir);
            renameDownloadedAssetsFolder(appDir);
        }
    }

    private static void renameDownloadedAssetsFolder(File appDirPath) {
        File file = new File(appDirPath, "Game-Assets-main");
        file.renameTo(new File(appDirPath, "assets"));
    }

    public static boolean doesNeedToDownloadResources() {
        File appDir = new File(appDirPath);
        return !appDir.exists() || !appDir.isDirectory() || Objects.requireNonNull(appDir.list()).length == 0;
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
