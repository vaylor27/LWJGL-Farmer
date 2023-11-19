package net.vakror.farmer.renderEngine.listener;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Listeners {
    private static final Multimap<Class<? extends Listener>, Listener> allListeners = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);

    public static <T extends Listener> void addListener(Class<T> clazz, T listener) {
        allListeners.put(clazz, listener);
    }

    public static <T extends Listener> void removeListener(Class<T> clazz, T listener) {
        allListeners.remove(clazz, listener);
    }

    public static <T extends Listener> void removeListeners(Class<T> clazz) {
        allListeners.removeAll(clazz);
    }

    public static <T extends Listener> List<T> getListeners(Class<T> clazz) {
        return (List<T>) allListeners.get(clazz);
    }
}
