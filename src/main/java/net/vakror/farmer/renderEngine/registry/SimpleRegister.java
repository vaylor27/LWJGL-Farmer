package net.vakror.farmer.renderEngine.registry;

import net.vakror.farmer.renderEngine.registry.core.Registry;
import net.vakror.farmer.renderEngine.registry.core.RegistryKey;
import net.vakror.farmer.renderEngine.registry.core.RegistryLocation;
import net.vakror.farmer.renderEngine.registry.core.RegistryObject;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;

public class SimpleRegister<T extends Registry<V>, V extends RegistryObject> {
    public T registry;

    public SimpleRegister(T register) {
        this.registry = register;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Registry<V>, V extends RegistryObject> SimpleRegister<T, V> create(RegistryKey<V> registryKey) {
        return new SimpleRegister<>((T) DefaultRegistries.REGISTRIES.get(registryKey));
    }

    public static <T extends Registry<V>, V extends RegistryObject> SimpleRegister<T, V> create(T registry) {
        return new SimpleRegister<>(registry);
    }

    public void register(String name, V value) {
        value.setRegistryName(new RegistryLocation(name));

        registry.register(value);
    }
}
