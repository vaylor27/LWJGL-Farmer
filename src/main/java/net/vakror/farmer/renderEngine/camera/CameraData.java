package net.vakror.farmer.renderEngine.camera;

public class CameraData {
    public float angleAroundFocused;
    public float pitch;
    public float yaw;


    public CameraData() {
        angleAroundFocused = Camera.data.angleAroundFocused;
        pitch = Camera.getPitch();
        yaw = Camera.getYaw();
    }

    private CameraData(float angleAroundFocused, float pitch, float yaw) {
        this.angleAroundFocused = angleAroundFocused;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static CameraData getDefault() {
        return new CameraData(0, 20, 180);
    }

    public void set(CameraData lastCameraData) {
        this.angleAroundFocused = lastCameraData.angleAroundFocused;
        this.pitch = lastCameraData.pitch;
        this.yaw = lastCameraData.yaw;
    }
}
