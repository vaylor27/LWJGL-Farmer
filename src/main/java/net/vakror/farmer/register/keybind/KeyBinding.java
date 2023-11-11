package net.vakror.farmer.register.keybind;

import net.vakror.farmer.renderEngine.registry.core.RegistryLocation;
import net.vakror.farmer.renderEngine.registry.core.RegistryObject;

public abstract class KeyBinding extends RegistryObject {
    public int key;
    public RegistryLocation location;

    public KeyBinding(int key) {
        this.key = key;
    }

    @Override
    public void setRegistryName(RegistryLocation location) {
        this.location = location;
    }

    @Override
    public RegistryLocation getRegistryName() {
        return location;
    }

    public abstract void execute(int scancode, int action, int mods);
}
