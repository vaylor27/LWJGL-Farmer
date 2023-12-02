package net.vakror.farmer.renderEngine.listener;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.vakror.farmer.GameEntryPoint;
import net.vakror.farmer.Priority;
import net.vakror.farmer.renderEngine.listener.register.AutoRegisterComplexListener;
import net.vakror.farmer.renderEngine.listener.register.AutoRegisterListener;
import net.vakror.farmer.renderEngine.listener.register.ListenerProvider;
import net.vakror.farmer.renderEngine.mod.DynamicClassLoader;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

import static net.vakror.farmer.FarmerGameMain.LOGGER;

public class Listeners {
    private static final Multimap<Class<? extends Listener>, Listener> allListeners = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);

    public static <T extends Listener> void addListener(T listener) {
        allListeners.put(listener.getClass(), listener);
    }

    public static <T extends Listener> List<T> getListeners(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (Map.Entry<Class<? extends Listener>, Listener> entry : allListeners.entries()) {
            if (clazz.isAssignableFrom(entry.getKey())) {
                list.add((T) entry.getValue());
            }
        }
        return list;
    }

    public static Reflections reflections;

    public static void findAndRegisterAllAnnotatedListeners() {
        if (reflections == null) {
            reflections = new Reflections((Object) Arrays.stream(((DynamicClassLoader) ClassLoader.getSystemClassLoader()).getPackages()).map(Package::getName).toList().toArray(new String[0]));
        }

        Set<Class<? extends ListenerProvider>> allComplexListeners = reflections.getSubTypesOf(ListenerProvider.class).stream().filter(aClass -> aClass.getAnnotation(AutoRegisterComplexListener.class) != null).collect(Collectors.toSet());
        List<Listener> instances = new ArrayList<>();
        for (Class<? extends ListenerProvider> clazz : allComplexListeners) {
            try {
                for (Constructor<?> constructor : clazz.getConstructors()) {
                    if (constructor.getParameters().length == 0) {
                        ListenerProvider instance = (ListenerProvider) constructor.newInstance();
                        if (instance.getListeners() != null) {
                            instances.addAll(instance.getListeners());
                        } else {
                            instances.add(instance.getListener());
                        }
                    } else {
                        LOGGER.info("Found non empty arguments constructor for annotated listener {}, skipping", clazz.getName());
                    }
                }
            } catch (ReflectiveOperationException | LinkageError e) {
                LOGGER.error("Failed to load listener {}. {}", clazz, e instanceof InstantiationException ? "Make sure the class is not abstract or an interface!": "");
                e.printStackTrace();
            }
        }

        Set<Class<?>> allSimpleListeners = reflections.getTypesAnnotatedWith(AutoRegisterListener.class);
        for (Class<?> clazz : allSimpleListeners) {
            try {
                Class<? extends Listener> asmInstanceClass = clazz.asSubclass(Listener.class);
                Constructor<? extends Listener> constructor = asmInstanceClass.getDeclaredConstructor();
                Listener instance = constructor.newInstance();
                instances.add(instance);
            } catch (ReflectiveOperationException | LinkageError e) {
                LOGGER.error("Failed to load listener {}. {}", clazz, e instanceof InstantiationException ? "Make sure the class is not abstract or an interface!": "");
                e.printStackTrace();
            }
        }
        instances.forEach(Listeners::addListener);
    }

    public static Set<GameEntryPoint> findAllEntryPoints() {
        if (reflections == null) {
            reflections = new Reflections((Object) Arrays.stream(((DynamicClassLoader) ClassLoader.getSystemClassLoader()).getPackages()).map(Package::getName).toList().toArray(new String[0]));
        }

        Set<Class<? extends GameEntryPoint>> allEntryPoints = reflections.getSubTypesOf(GameEntryPoint.class);
        Set<GameEntryPoint> instances = new HashSet<>();
        for (Class<? extends GameEntryPoint> clazz : allEntryPoints) {
            try {
                instances.add(clazz.getDeclaredConstructor().newInstance());
            } catch (ReflectiveOperationException | LinkageError e) {
                LOGGER.error("Failed to load: {}", clazz);
                e.printStackTrace();
            }
        }
        reflections = null;
        return instances;
    }

    public static void runEntryPoints(Set<GameEntryPoint> gameEntryPoints) {
        Set<GameEntryPoint> systemEntryPoints = gameEntryPoints.stream().filter(gameEntryPoint -> gameEntryPoint.getPriority().equals(Priority.SYSTEM)).collect(Collectors.toSet());
        Set<GameEntryPoint> highestEntryPoints = gameEntryPoints.stream().filter(gameEntryPoint -> gameEntryPoint.getPriority().equals(Priority.HIGHEST)).collect(Collectors.toSet());
        Set<GameEntryPoint> highEntryPoints = gameEntryPoints.stream().filter(gameEntryPoint -> gameEntryPoint.getPriority().equals(Priority.HIGH)).collect(Collectors.toSet());
        Set<GameEntryPoint> normalEntryPoints = gameEntryPoints.stream().filter(gameEntryPoint -> gameEntryPoint.getPriority().equals(Priority.NORMAL)).collect(Collectors.toSet());
        Set<GameEntryPoint> lowEntryPoints = gameEntryPoints.stream().filter(gameEntryPoint -> gameEntryPoint.getPriority().equals(Priority.LOW)).collect(Collectors.toSet());
        Set<GameEntryPoint> lowestEntryPoints = gameEntryPoints.stream().filter(gameEntryPoint -> gameEntryPoint.getPriority().equals(Priority.LOWEST)).collect(Collectors.toSet());

        systemEntryPoints.forEach(GameEntryPoint::initialize);
        highestEntryPoints.forEach(GameEntryPoint::initialize);
        highEntryPoints.forEach(GameEntryPoint::initialize);
        normalEntryPoints.forEach(GameEntryPoint::initialize);
        lowEntryPoints.forEach(GameEntryPoint::initialize);
        lowestEntryPoints.forEach(GameEntryPoint::initialize);
    }
}
