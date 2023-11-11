package net.vakror.farmer.renderEngine.registry.core;

import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleRegistry<T extends RegistryObject> extends Registry<T> {
    private final Map<RegistryLocation, T> objects = new HashMap<>();

    public SimpleRegistry(RegistryKey<T> key) {
        super(key);
        DefaultRegistries.addRegistry(key, this);
    }

    @Override
    public void freeze() {
        super.frozen = true;
    }

    @Override
    public T get(RegistryLocation pName) {
        return objects.get(pName);
    }

    @Override
    public boolean has(RegistryLocation pName) {
        return objects.containsKey(pName);
    }


    @Override
    public void forEach(Consumer<T> consumer) {
        this.objects.values().forEach(consumer);
    }

    @Override
    public T register(T pValue) {
        if (super.frozen) {
            throw new IllegalStateException(String.format("Cannot register to %s once frozen", super.key.id));
        }
        objects.put(pValue.getRegistryName(), pValue);
        return pValue;
    }
}
