package net.vakror.farmer.renderEngine.registry.registries;

import net.vakror.farmer.register.option.Option;
import net.vakror.farmer.renderEngine.registry.core.RegistryKey;
import net.vakror.farmer.renderEngine.registry.core.SimpleRegistry;

public class OptionRegistry extends SimpleRegistry<Option<?>> {
    public static final RegistryKey<Option<?>> KEY = new RegistryKey<>("options");

    public OptionRegistry() {
        super(KEY);
    }
}
