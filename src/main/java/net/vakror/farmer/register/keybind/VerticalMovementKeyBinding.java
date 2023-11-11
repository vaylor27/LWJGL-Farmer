package net.vakror.farmer.register.keybind;

import net.vakror.farmer.FarmerGameMain;

public class VerticalMovementKeyBinding extends KeyBinding {
    private final float amount;
    private final boolean shouldExecuteIfOnGround;

    public VerticalMovementKeyBinding(int key, float amount, boolean shouldExecuteIfOnGround) {
        super(key);
        this.amount = amount;
        this.shouldExecuteIfOnGround = shouldExecuteIfOnGround;
    }

    @Override
    public void execute(int scancode, int action, int mods) {
        if (!shouldExecuteIfOnGround) {
            if (!FarmerGameMain.player.isInAir) {
                FarmerGameMain.player.upwardsSpeed = amount;
            }
        } else {
            FarmerGameMain.player.upwardsSpeed = amount;
        }
    }
}
