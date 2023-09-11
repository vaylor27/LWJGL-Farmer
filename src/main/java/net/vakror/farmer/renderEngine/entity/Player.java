package net.vakror.farmer.renderEngine.entity;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import org.joml.Vector3f;

import java.util.logging.Logger;

public class Player extends Entity {

    public float currentSpeed = 0;
    public float currentTurnSpeed = 0;
    private float updwardsSpeed = 0;
    public boolean isInAir = false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void jump() {
        this.updwardsSpeed = FarmerGameMain.options.jumpPower;
        isInAir = true;
    }

    public void sneak() {
        //TODO: make configurable
        this.updwardsSpeed = -(FarmerGameMain.options.jumpPower/2);
    }

    public void move() {
        super.increaseRotation(new Vector3f(0, currentTurnSpeed * Window.getFrameTimeSeconds(), 0));
        float distance = currentSpeed * Window.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(new Vector3f(dx, 0, dz));
        updwardsSpeed += FarmerGameMain.options.gravity * Window.getFrameTimeSeconds();
        super.increasePosition(new Vector3f(0, updwardsSpeed * Window.getFrameTimeSeconds(), 0));
        if (super.getPosition().y < 0) {
            updwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = 0;
        }
    }
}
