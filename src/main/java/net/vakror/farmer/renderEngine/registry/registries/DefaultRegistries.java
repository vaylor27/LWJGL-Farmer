package net.vakror.farmer.renderEngine.registry.registries;

import net.vakror.farmer.register.keybind.KeyBindingRegister;
import net.vakror.farmer.register.option.OptionRegister;
import net.vakror.farmer.renderEngine.registry.core.Registry;
import net.vakror.farmer.renderEngine.registry.core.RegistryKey;
import net.vakror.farmer.renderEngine.registry.core.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class DefaultRegistries {

    public static final KeyBindingRegistry KEYBINDINGS = new KeyBindingRegistry();
    public static final OptionRegistry OPTIONS = new OptionRegistry();
    public static Map<RegistryKey<? extends RegistryObject>, Registry<? extends RegistryObject>> REGISTRIES = new HashMap<>();

    public static  <T extends RegistryObject> void addRegistry(RegistryKey<T> key, Registry<T> registry) {
        if (REGISTRIES == null) {
            REGISTRIES = new HashMap<>();
        }
        REGISTRIES.put(key, registry);
    }

    public static void registerDefaults() {
        OptionRegister.registerOptions();
        KeyBindingRegister.registerKeyBindings();
    }
}
