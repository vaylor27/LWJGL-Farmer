package net.vakror.farmer.renderEngine.mod;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.Mod;
import net.vakror.farmer.renderEngine.listener.Listeners;
import net.vakror.farmer.renderEngine.listener.type.ModLoadingListener;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static net.vakror.farmer.FarmerGameMain.LOGGER;
import static net.vakror.farmer.renderEngine.listener.Listeners.reflections;

public class ModLoader {
    public static void findAndLoadAllMods() {
        loadModClasses();
        loadMods();
    }

    public static final Map<String, Mod> MODS = new HashMap<>();

    private static void loadMods() {
        List<ModLoadingListener> modLoadingListeners = Listeners.getListeners(ModLoadingListener.class);
        modLoadingListeners.forEach(ModLoadingListener::onModLoadingStart);
        if (reflections == null) {
            reflections = new Reflections((Object) Arrays.stream(((DynamicClassLoader) ClassLoader.getSystemClassLoader()).getPackages()).map(Package::getName).toList().toArray(new String[0]));
        }

        Set<Class<? extends Mod>> modClasses = reflections.getSubTypesOf(Mod.class);
        modLoadingListeners.forEach(ModLoadingListener::onModClassesDiscovered);

        for (Class<? extends Mod> modClass : modClasses) {
            modLoadingListeners.forEach(clazz -> clazz.onModClassDiscovered(modClass));
            try {
                if (!modClass.isAnnotation() && !modClass.isEnum() && !modClass.isInterface() && !modClass.isArray() && !modClass.isAnonymousClass() && !Modifier.isAbstract(modClass.getModifiers()) && Modifier.isPublic(modClass.getModifiers())) {
                    for (Constructor<?> constructor : modClass.getConstructors()) {
                        if (constructor.getParameters().length == 0) {
                            Mod mod = (Mod) constructor.newInstance();
                            modLoadingListeners.forEach(modLoadingListener -> modLoadingListener.onModLoaded(mod));
                            MODS.put(mod.getId(), mod);
                            LOGGER.info("Loaded mod \"{}\" successfully", mod);
                            modLoadingListeners.forEach(modLoadingListener -> modLoadingListener.onModAdd(mod));
                        }
                    }
                }
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                LOGGER.error("Failed to load mod \"{}\"", modClass.getName());
                e.printStackTrace();
                modLoadingListeners.forEach(clazz -> clazz.onLoadFail(modClass));
            }
        }
        modLoadingListeners.forEach(ModLoadingListener::onModLoadingFinish);
        reflections = null;
    }

    private static void loadModClasses() {
        File directory = new File(FarmerGameMain.appDirPath, "mods/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            try {
                if (!file.isDirectory() && file.getName().endsWith(".jar") && !file.getName().startsWith(".")) {
                    DynamicClassLoader classLoader = (DynamicClassLoader) ClassLoader.getSystemClassLoader();
                    classLoader.add(file.toURI().toURL());
                    for (String className : getAllClassesInJar(file)) {
                        classLoader.loadClass(className);
                    }
                } else {
                    if (!file.getName().startsWith(".")) {
                        if (file.isDirectory()) {
                            LOGGER.info("Found directory \"{}\" in mods folder, skipping", file.getName());
                        } else {
                            LOGGER.info("Found file \"{}\" in mods folder, even though it is not a jar file, skipping", file.getName());
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error loading mod \"{}\"", file.getName());
            } catch (ClassNotFoundException e) {
                LOGGER.error("Could not find class \"{}\" in mod \"{}\" even though it was detected. You messed up.", e.getClass(), file.getName());
            }
        }
    }

    public static Set<String> getAllClassesInJar(File givenFile) throws IOException {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                }
            }
            return classNames;
        }
    }
}
