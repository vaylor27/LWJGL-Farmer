package net.vakror.farmer.register.keybind;

import net.vakror.farmer.renderEngine.registry.SimpleRegister;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.registry.registries.KeyBindingRegistry;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBindingRegister {
    public static final SimpleRegister<KeyBindingRegistry, KeyBinding> REGISTER = SimpleRegister.create(DefaultRegistries.KEYBINDINGS);

    public static final KeyBinding DESTROY_CURSOR = new ToggleCursorDisabledKeyBinding(GLFW_KEY_LEFT_ALT);

    public static void registerKeyBindings() {
        REGISTER.register("destroy_cursor", DESTROY_CURSOR);
    }
}