package net.vakror.farmer.renderEngine.registry.registries;

import net.vakror.farmer.register.keybind.KeyBinding;
import net.vakror.farmer.renderEngine.registry.core.RegistryKey;
import net.vakror.farmer.renderEngine.registry.core.SimpleRegistry;

public class KeyBindingRegistry extends SimpleRegistry<KeyBinding> {
    public static final RegistryKey<KeyBinding> KEY = new RegistryKey<>("key_bindings");

    public KeyBindingRegistry() {
        super(KEY);
    }
}
