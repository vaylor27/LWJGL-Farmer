package net.vakror.farmer.register.keybind;

import net.vakror.farmer.FarmerGameMain;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MovementKeyBinding extends KeyBinding {
    private final boolean affectsSpeed;
    private final float amount;

    public MovementKeyBinding(int key, boolean affectsSpeed, float amount) {
        super(key);
        this.affectsSpeed = affectsSpeed;
        this.amount = amount;
    }

    @Override
    public void execute(int scancode, int action, int mods) {
        if (affectsSpeed) {
            FarmerGameMain.player.currentSpeed = amount;
        } else {
            FarmerGameMain.player.currentTurnSpeed = amount;
        }
        if (action == GLFW_RELEASE) {
            FarmerGameMain.player.currentTurnSpeed = 0;
            FarmerGameMain.player.currentSpeed = 0;
        }
    }
}
