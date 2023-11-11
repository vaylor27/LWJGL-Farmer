package net.vakror.farmer.renderEngine.registry.core;

public class RegistryKey<T extends RegistryObject> {
    public final String id;

    public RegistryKey(String id) {
        this.id = id;
    }
}
