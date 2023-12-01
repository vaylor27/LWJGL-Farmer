package net.vakror.farmer.renderEngine.camera;

import net.vakror.farmer.GameEntryPoint;
import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.listener.register.AutoRegisterListener;
import net.vakror.farmer.renderEngine.listener.type.MouseButtonListener;
import net.vakror.farmer.renderEngine.listener.type.MouseCapturedListener;
import net.vakror.farmer.renderEngine.listener.type.MouseScrollListener;
import net.vakror.farmer.renderEngine.listener.type.RenderListener;
import net.vakror.farmer.renderEngine.mouse.MousePicker;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.util.InputUtil;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static net.vakror.farmer.renderEngine.util.InputUtil.*;


@AutoRegisterListener
public class Camera implements MouseCapturedListener, MouseScrollListener, MouseButtonListener, RenderListener, GameEntryPoint {
    private static float distanceFromFocused = 1000;
    private static final Vector3f position = new Vector3f(0, 10, 0);
    public static final CameraData data = CameraData.getDefault();
    public static float lastValueY = 0;
    public static float lastValueX = 0;

    public static Vector2f currentCameraFocusPoint = new Vector2f(Terrain.SIZE / 2, Terrain.SIZE / 2);
    public static Vector2f nextCameraFocusPoint = null;
    public static double distanceToNextFocus;
    public static float alpha = 1;
    public static float alphaDecayRate;
    public static CameraData lastCameraData;

    public void onRender() {
        interpolateFocusPoints();
        calculatePitch();
        calculateAngleAroundTerrain();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPos(horizontalDistance, verticalDistance);
        moveCamera();

        data.yaw = 180 - (data.angleAroundFocused);
//        this.position.set(new Vector3f(Math.clamp(-300, 1300, position.x), Math.clamp(10, 100, position.y), Math.clamp(-300, 1300, position.z)));
    }

    private static void interpolateFocusPoints() {
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

    private static void moveCamera() {
        currentCameraFocusPoint = new Vector2f(Math.clamp(0, 1000, currentCameraFocusPoint.x), Math.clamp(0, 1000, currentCameraFocusPoint.y));
    }

    private static float calculateHorizontalDistance() {
        return distanceFromFocused * Math.cos(Math.toRadians(data.pitch));
    }
    private static float calculateVerticalDistance() {
        return distanceFromFocused * Math.sin(Math.toRadians(data.pitch));
    }

    private static void calculateCameraPos(float horizontalDistance, float verticalDistance) {
        float theta = 0 + data.angleAroundFocused;
        float offsetX = horizontalDistance * Math.sin(Math.toRadians(theta));
        float offsetZ = horizontalDistance * Math.cos(Math.toRadians(theta));
        position.x = currentCameraFocusPoint.x - offsetX;
        position.z = currentCameraFocusPoint.y - offsetZ;
        position.y = verticalDistance + 5;
    }

    public static Vector3f getPosition() {
        return position;
    }

    public static float getPitch() {
        return data.pitch;
    }

    public static float getYaw() {
        return data.yaw;
    }

    @Override
    public void onScroll(long window, double x, double y) {
        float zoomLevel = (float) y;
        distanceFromFocused -= (zoomLevel * (Options.scrollSensitivity()));
        distanceFromFocused = Math.clamp(10, 1200, distanceFromFocused);
        yScrollValue = 0;
    }

    private static void calculatePitch() {
        if (InputUtil.isCursorDisabled) {
            float pitchChange = InputUtil.currentMousePos.getAsScreenCoords().y - lastValueY; // *0.1f
            data.pitch += (pitchChange * (Options.sensitivity() / 100));
            data.pitch = Math.clamp(6, 90, data.pitch);
            lastValueY = InputUtil.currentMousePos.getAsScreenCoords().y;
        }
    }

    private static void calculateAngleAroundTerrain() {
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

    public static void setData(CameraData lastCameraData) {
        if (lastCameraData != null) {
            data.set(lastCameraData);
        }
    }

    public static void invertPitch() {
        data.pitch = -data.pitch;
    }


    @Override
    public void onCaptured() {
        setData(lastCameraData);
        lastValueX = InputUtil.previousCapturedPos.getAsScreenCoords().x;
        lastValueY = InputUtil.previousCapturedPos.getAsScreenCoords().y;
        currentMousePos.setScreenCoordinates(InputUtil.previousCapturedPos.getAsScreenCoords().x, InputUtil.previousCapturedPos.getAsScreenCoords().y);
    }

    @Override
    public void onReleased() {
        lastCameraData = new CameraData();
    }

    @Override
    public void onClick(long window, int button, int action) {
        if (!InputUtil.isHoveringOverGui && !InputUtil.isCursorDisabled) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
                Vector3f terrainPoint = MousePicker.getCurrentTerrainPoint();
                if (terrainPoint != null && terrainPoint.x > 0 && terrainPoint.x < Terrain.SIZE && terrainPoint.z > 0 && terrainPoint.z < Terrain.SIZE) {
                    nextCameraFocusPoint = new Vector2f(terrainPoint.x, terrainPoint.z);
                    distanceToNextFocus = 0;
                    alpha = 1;
                }
            }
        }
    }

    @Override
    public void initialize() {
        data.angleAroundFocused = 0;
        data.pitch = 20;
        data.yaw = 180;
        currentCameraFocusPoint = new Vector2f(500, 500);
        lastValueX = (float) initialMouseCoords.x;
        lastValueY = (float) initialMouseCoords.y;
    }
}
