package net.vakror.farmer.renderEngine.camera;

public class CameraData {
    public float angleAroundFocused;
    public float pitch;
    public float yaw;


    public CameraData(Camera camera) {
        angleAroundFocused = camera.data.angleAroundFocused;
        pitch = camera.getPitch();
        yaw = camera.getYaw();
    }

    private CameraData(float angleAroundFocused, float pitch, float yaw) {
        this.angleAroundFocused = angleAroundFocused;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static CameraData getDefault() {
        return new CameraData(0, 20, 180);
    }

    public float getAngleAroundFocused() {
        return angleAroundFocused;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
