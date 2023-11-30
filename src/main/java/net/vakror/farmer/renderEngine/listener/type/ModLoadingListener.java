package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.Mod;
import net.vakror.farmer.renderEngine.listener.Listener;

/**
 * All of these methods are called when the mod is loaded, not the mod class
 */
public interface ModLoadingListener extends Listener {
    /**
     * called before any mods are loaded but after the process has started
     */
    default void onModLoadingStart() {
    }

    /**
     * Called when there are no more mods that need to be loaded
     */
    default void onModLoadingFinish() {
    }

    default void onModClassesDiscovered() {

    }

    default void onModClassDiscovered(Class<? extends Mod> clazz) {

    }

    default void onLoadFail(Class<? extends Mod> clazz) {

    }

    /**
     * Called after a mod instance is created but before it is added to the map
     * @param mod the mod
     */
    default void onModLoaded(Mod mod) {

    }

    /**
     * called after a mod instance is created but after it is added to the map
     * @param mod the mod
     */
    default void onModAdd(Mod mod) {

    }
}
