package net.vakror.farmer.register.keybind;

import net.vakror.farmer.FarmerGameMain;

import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MovementKeyBinding extends KeyBinding {
    private final boolean isForwardBackward;
    private final Supplier<Float> amount;

    public MovementKeyBinding(int key, boolean isForwardBackward, Supplier<Float> amount) {
        super(key);
        this.isForwardBackward = isForwardBackward;
        this.amount = amount;
    }

    @Override
    public void execute(int scancode, int action, int mods) {
        if (isForwardBackward) {
            FarmerGameMain.camera.currentSpeed = amount.get();
        } else {
            FarmerGameMain.camera.currentStrafeSpeed = amount.get();
        }
        if (action == GLFW_RELEASE) {
            if (!isForwardBackward) {
                FarmerGameMain.camera.currentStrafeSpeed = 0;
            } else {
                FarmerGameMain.camera.currentSpeed = 0;
            }
        }
    }
}
