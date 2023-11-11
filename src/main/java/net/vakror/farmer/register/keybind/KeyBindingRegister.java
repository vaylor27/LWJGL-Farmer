package net.vakror.farmer.register.keybind;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.registry.SimpleRegister;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.registry.registries.KeyBindingRegistry;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBindingRegister {
    public static final SimpleRegister<KeyBindingRegistry, KeyBinding> REGISTER = SimpleRegister.create(DefaultRegistries.KEYBINDINGS);

    public static final KeyBinding FORWARD = new MovementKeyBinding(GLFW_KEY_W, true, Options.runSpeed());
    public static final KeyBinding BACKWARD = new MovementKeyBinding(GLFW_KEY_S, true, -Options.runSpeed());
    public static final KeyBinding LEFT = new MovementKeyBinding(GLFW_KEY_A, false, Options.turnSpeed());
    public static final KeyBinding RIGHT = new MovementKeyBinding(GLFW_KEY_D, false, -Options.turnSpeed());

    public static final KeyBinding JUMP = new VerticalMovementKeyBinding(GLFW_KEY_SPACE, Options.jumpPower(), false);
    public static final KeyBinding SNEAK = new VerticalMovementKeyBinding(GLFW_KEY_LEFT_SHIFT, -(Options.jumpPower() / 2), false);


    public static void registerKeyBindings() {
        REGISTER.register("forward", FORWARD);
        REGISTER.register("backward", BACKWARD);
        REGISTER.register("left", LEFT);
        REGISTER.register("right", RIGHT);
        REGISTER.register("jump", JUMP);
        REGISTER.register("sneak", SNEAK);
    }

            /*
            if (key == GLFW_KEY_SPACE) {
                if (!FarmerGameMain.player.isInAir) {
                    FarmerGameMain.player.jump();
                }
            }
            if (key == GLFW_KEY_LEFT_SHIFT) {
                if (!FarmerGameMain.player.isInAir) {
                    FarmerGameMain.player.sneak();
                }
            }
            if (key == GLFW_KEY_C) {
                FarmerGameMain.options.ambientLight-=0.2f;
                FarmerGameMain.options.ambientLight = Math.max(0, FarmerGameMain.options.ambientLight);
            }
            if (key == GLFW_KEY_V) {
                FarmerGameMain.options.ambientLight+=0.2f;
                FarmerGameMain.options.ambientLight = Math.min(1, FarmerGameMain.options.ambientLight);
            }

            if (key == GLFW_KEY_E) {
                FarmerGameMain.options.fov-=5f;
                FarmerGameMain.renderer.regenProjectionMatrix();
            }
            if (key == GLFW_KEY_R) {
                FarmerGameMain.options.fov+=5f;
                FarmerGameMain.renderer.regenProjectionMatrix();
            }

            if (key == GLFW_KEY_T) {
                FarmerGameMain.options.nearPlane-=1f;
                FarmerGameMain.renderer.regenProjectionMatrix();
            }
            if (key == GLFW_KEY_Y) {
                FarmerGameMain.options.nearPlane+=1f;
                FarmerGameMain.renderer.regenProjectionMatrix();
            }

            if (key == GLFW_KEY_U) {
                FarmerGameMain.options.farPlane-=10f;
                FarmerGameMain.renderer.regenProjectionMatrix();
            }
            if (key == GLFW_KEY_I) {
                FarmerGameMain.options.farPlane+=10f;
                FarmerGameMain.renderer.regenProjectionMatrix();
            }*/
}
