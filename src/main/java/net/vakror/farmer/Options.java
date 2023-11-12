package net.vakror.farmer;

import net.vakror.farmer.register.option.Option;
import net.vakror.farmer.register.option.Vector3fOption;
import net.vakror.farmer.renderEngine.registry.core.RegistryLocation;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import org.joml.Vector3f;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

//TODO: add option groups and a gui
public class Options {

    public static boolean useSpecularLighting() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("useSpecularLighting")).getAsBooleanOption().value();
    }

    public static boolean useAmbientLight() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("ambientLight")).getAsBooleanOption().value();
    }


    public static float ambientLight() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("ambientLight")).getAsFloatOption().value();
    }

    public static float fov() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("fov")).getAsFloatOption().value();
    }

    public static float nearPlane() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("nearPlane")).getAsFloatOption().value();
    }

    public static float farPlane() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("farPlane")).getAsFloatOption().value();
    }

    public static float fogDensity() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("fogDensity")).getAsFloatOption().value();
    }

    public static float fogGradient() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("fogGradient")).getAsFloatOption().value();
    }

    public static Vector3f skyColor() {
        return ((Vector3fOption) DefaultRegistries.OPTIONS.get(new RegistryLocation("skyColor"))).value();
    }

    public static float runSpeed() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("runSpeed")).getAsFloatOption().value();
    }

    public static float turnSpeed() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("turnSpeed")).getAsFloatOption().value();
    }

    public static float gravity() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("gravity")).getAsFloatOption().value();
    }

    public static float jumpPower() {
        return DefaultRegistries.OPTIONS.get(new RegistryLocation("jumpPower")).getAsFloatOption().value();
    }

    public static void save() {
        File file = new File("options.txt");
        try(StringWriter writer = new StringWriter()) {
            file.delete();
            file.createNewFile();
            DefaultRegistries.OPTIONS.forEach((option -> {
                try {
                    writer.write(option.getRegistryName().toString().replace("src/main/resources/assets/", "") + "=" + option.getValueString() + "\n");
                } catch (Exception e) {
                    throw new IllegalStateException("Error trying to save options", e);
                }
            }));
            String s = writer.toString();
            Files.write(file.toPath(), s.getBytes());
        } catch (Exception e) {
            throw new IllegalStateException("Error trying to save options", e);
        }
    }

    public static void read() {
        try {
            File file = new File("options.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                String[] splitLine = line.split("=");
                if (splitLine.length != 2) {
                    throw new IllegalStateException(String.format("Line %s in Options.txt contains %s equal signs, not two. = is meant to separate key-value pairs", lines.indexOf(line) + 1, splitLine.length - 1));
                }
                String key = splitLine[0];
                String value = splitLine[1];
                RegistryLocation location = new RegistryLocation(key);
                deserializeOption(key, value, location);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error trying to load options", e);
        }
    }

    private static<T> void deserializeOption(String key, String value, RegistryLocation location) {
        if (DefaultRegistries.OPTIONS.has(location)) {
            Option<T> option = (Option<T>) DefaultRegistries.OPTIONS.get(location);
            option.setValue(option.valueFromString(value));
        } else {
            System.out.printf("Option \"%s\" was not found, skipping%n", key);
        }
    }
}
