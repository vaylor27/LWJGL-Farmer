package net.vakror.farmer.renderEngine.entity;

import org.joml.Vector3d;
import org.joml.Vector3f;

public class Camera {


    private final Vector3f position = new Vector3f(0, 10, 0);
    private float pitch;
    private float yaw = 180;
    private float roll;

    public void move(float x, float y, float z) {
        position.add(x, y, z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
