package net.vakror.farmer.register.keybind;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.listeners.RunSpeedListener;
import net.vakror.farmer.renderEngine.registry.SimpleRegister;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.registry.registries.KeyBindingRegistry;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBindingRegister {
    public static final SimpleRegister<KeyBindingRegistry, KeyBinding> REGISTER = SimpleRegister.create(DefaultRegistries.KEYBINDINGS);

    public static final KeyBinding FORWARD = new MovementKeyBinding(GLFW_KEY_W, true, RunSpeedListener::getRunSpeed);
    public static final KeyBinding BACKWARD = new MovementKeyBinding(GLFW_KEY_S, true, RunSpeedListener::getNegativeRunSpeed);
    public static final KeyBinding LEFT = new MovementKeyBinding(GLFW_KEY_A, false, RunSpeedListener::getRunSpeed);
    public static final KeyBinding RIGHT = new MovementKeyBinding(GLFW_KEY_D, false, RunSpeedListener::getNegativeRunSpeed);

    public static final KeyBinding DESTROY_CURSOR = new ToggleCursorDisabledKeyBinding(GLFW_KEY_LEFT_ALT);

    public static void registerKeyBindings() {
        REGISTER.register("forward", FORWARD);
        REGISTER.register("backward", BACKWARD);
        REGISTER.register("left", LEFT);
        REGISTER.register("right", RIGHT);

        REGISTER.register("destroy_cursor", DESTROY_CURSOR);
    }
}