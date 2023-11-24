package net.vakror.farmer.renderEngine.camera;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static net.vakror.farmer.renderEngine.mouse.InputUtil.yScrollValue;


public class Camera {
    private float distanceFromFocused = 1000;
    private final Vector3f position = new Vector3f(0, 10, 0);
    public final CameraData data = CameraData.getDefault();
    public static float lastValueY = 0;
    public static float lastValueX = 0;

    public Vector2f currentCameraFocusPoint = new Vector2f(Terrain.SIZE / 2, Terrain.SIZE / 2);
    public Vector2f nextCameraFocusPoint = null;
    public double distanceToNextFocus;
    public float alpha = 1;
    public float alphaDecayRate;

    public Camera() {
    }

    public void tick() {
        interpolateFocusPoints();
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

    private void interpolateFocusPoints() {
        if (alpha <= 0) {
            nextCameraFocusPoint = null;
            alpha = 1;
            distanceToNextFocus = 0;
        }
        if (nextCameraFocusPoint != null) {
            if (currentCameraFocusPoint.equals(nextCameraFocusPoint)) {
                nextCameraFocusPoint = null;
                alpha = 1;
                distanceToNextFocus = 0;
            } else {
                if (distanceToNextFocus == 0) {
                    Vector2f vectorToNewPos = new Vector2f();
                    nextCameraFocusPoint.sub(currentCameraFocusPoint, vectorToNewPos);
                    distanceToNextFocus = java.lang.Math.hypot(vectorToNewPos.x, vectorToNewPos.y);
                    float time = 560;
                    alphaDecayRate = Math.clamp(0.0001f, 0.9f, (float) (distanceToNextFocus / time / 1000));

                }
                float x = alpha * currentCameraFocusPoint.x + (1 - alpha) * nextCameraFocusPoint.x;
                float y = alpha * currentCameraFocusPoint.y + (1 - alpha) * nextCameraFocusPoint.y;

                currentCameraFocusPoint = new Vector2f(x, y);

                alpha -= alphaDecayRate;
            }
        }
    }

    private void moveCamera() {
        currentCameraFocusPoint = new Vector2f(Math.clamp(0, 1000, currentCameraFocusPoint.x), Math.clamp(0, 1000, currentCameraFocusPoint.y));
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
        if (lastCameraData != null) {
            this.data.set(lastCameraData);
        }
    }

    public void invertPitch() {
        this.data.pitch = -data.pitch;
    }
}
