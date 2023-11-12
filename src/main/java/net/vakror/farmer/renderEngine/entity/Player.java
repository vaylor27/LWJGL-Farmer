package net.vakror.farmer.renderEngine.entity;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import org.joml.Vector3f;

public class Player extends Entity {
    public float currentSpeed = 0;
    public float currentTurnSpeed = 0;
    public float upwardsSpeed = 0;
    public boolean isInAir = false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void jump() {
        this.upwardsSpeed = Options.jumpPower();
        isInAir = true;
    }

    public void sneak() {
        //TODO: make configurable
        this.upwardsSpeed = -(Options.jumpPower()/2);
    }

    public void move(Terrain terrain) {
        super.increaseRotation(new Vector3f(0, currentTurnSpeed * Window.getFrameTimeSeconds(), 0));
        float distance = currentSpeed * Window.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(new Vector3f(dx, 0, dz));
        upwardsSpeed += (-Options.gravity()) * Window.getFrameTimeSeconds();
        super.increasePosition(new Vector3f(0, upwardsSpeed * Window.getFrameTimeSeconds(), 0));

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if (super.getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }
}
