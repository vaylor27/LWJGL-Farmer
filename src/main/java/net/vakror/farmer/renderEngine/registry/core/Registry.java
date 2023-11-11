package net.vakror.farmer.renderEngine.registry.core;

import java.util.function.Consumer;

public abstract class Registry<T extends RegistryObject> {
    public final RegistryKey<T> key;
    public boolean frozen;
    public Registry(RegistryKey<T> key) {
        this.key = key;
    }
    public abstract void freeze();

    public RegistryKey<T> key() {
        return this.key;
    }

    public abstract T register(T pValue);

    public abstract T get(RegistryLocation pName);
    public abstract boolean has(RegistryLocation pName);

    public abstract void forEach(Consumer<T> consumer);
}
