package net.vakror.farmer.renderEngine.camera;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static net.vakror.farmer.renderEngine.mouse.InputUtil.yScrollValue;


public class Camera {
    public float currentStrafeSpeed;
    public float currentSpeed;
    private float distanceFromFocused = 1000;
    private final Vector3f position = new Vector3f(0, 10, 0);
    public CameraData data = CameraData.getDefault();
    public static float lastValueY = 0;
    public static float lastValueX = 0;

    public Vector2f currentCameraFocusPoint = new Vector2f(500, 500);

    public Camera() {
    }

    public void tick() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundTerrain();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPos(horizontalDistance, verticalDistance);
        moveCamera();

        this.data.yaw = 180 - (data.angleAroundFocused);
//        this.position.set(new Vector3f(Math.clamp(-300, 1300, position.x), Math.clamp(10, 100, position.y), Math.clamp(-300, 1300, position.z)));
    }

    private void moveCamera() {
        float distance = currentSpeed * Window.getFrameTimeSeconds();
        float dx = (float) (distance * java.lang.Math.sin(java.lang.Math.toRadians(angleAroundFocused())));
        float dz = (float) (distance * java.lang.Math.cos(java.lang.Math.toRadians(angleAroundFocused())));
        this.currentCameraFocusPoint.add(dx, dz);
        float distance1 = currentStrafeSpeed * Window.getFrameTimeSeconds();
        float dx1 = (float) (distance1 * -java.lang.Math.sin(java.lang.Math.toRadians(angleAroundFocused())));
        float dz1 = (float) (distance1 * java.lang.Math.cos(java.lang.Math.toRadians(angleAroundFocused())));
        this.currentCameraFocusPoint.add(new Vector2f(dx, dz));
        this.currentCameraFocusPoint.add(new Vector2f(dz1, dx1));
        currentCameraFocusPoint = new Vector2f(Math.clamp(0, 1000, currentCameraFocusPoint.x), Math.clamp(0, 1000, currentCameraFocusPoint.y));
    }

    public float angleAroundFocused() {
        return data.angleAroundFocused;
    }

    private float calculateHorizontalDistance() {
        return distanceFromFocused * Math.cos(Math.toRadians(data.pitch));
    }
    private float calculateVerticalDistance() {
        return distanceFromFocused * Math.sin(Math.toRadians(data.pitch));
    }

    private void calculateCameraPos(float horizontalDistance, float verticalDistance) {
        float theta = 0 + data.angleAroundFocused;
        float offsetX = horizontalDistance * Math.sin(Math.toRadians(theta));
        float offsetZ = horizontalDistance * Math.cos(Math.toRadians(theta));
        position.x = currentCameraFocusPoint.x - offsetX;
        position.z = currentCameraFocusPoint.y - offsetZ;
        position.y = verticalDistance + 5;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return data.pitch;
    }

    public float getYaw() {
        return data.yaw;
    }


    private void calculateZoom() {
        if (!InputUtil.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            float zoomLevel = (float) yScrollValue;
            distanceFromFocused -= (zoomLevel * (Options.scrollSensitivity() / 100));
            distanceFromFocused = Math.clamp(10, 1200, distanceFromFocused);
            yScrollValue = 0;
        }
    }

    private void calculatePitch() {
        if (InputUtil.isCursorDisabled) {
            float pitchChange = InputUtil.currentMousePos.getAsScreenCoords().y - lastValueY; // *0.1f
            data.pitch += (pitchChange * (Options.sensitivity() / 100));
            data.pitch = Math.clamp(6, 90, data.pitch);
            lastValueY = InputUtil.currentMousePos.getAsScreenCoords().y;
        }
    }

    private void calculateAngleAroundTerrain() {
        if (InputUtil.isCursorDisabled) {
            float angleChange = InputUtil.currentMousePos.getAsScreenCoords().x - lastValueX; // *0.1f
            data.angleAroundFocused -= (angleChange * (Options.sensitivity() / 100));

            if (data.angleAroundFocused > 360) {
                data.angleAroundFocused = 0;
            }
            if (data.angleAroundFocused < 0) {
                data.angleAroundFocused = 360;
            }
            lastValueX = InputUtil.currentMousePos.getAsScreenCoords().x;
        }
    }

    public void setData(CameraData lastCameraData) {
        this.data = lastCameraData;
    }
}
