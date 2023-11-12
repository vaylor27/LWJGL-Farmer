package net.vakror.farmer.renderEngine.entity;

import net.vakror.farmer.renderEngine.Window;
import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;


public class Camera {
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;
    private final Vector3f position = new Vector3f(0, 10, 0);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;

    private double scrollValue = 0;
    private int clickValue = -1;
    private float lastValueY = 0;
    private float lastValueX = 0;
    private double mouseDx = 0;
    private double mouseDy = 0;


    public void glfwScrollCallback(long windowID, double dx, double dy) {
        scrollValue = dy;
    }

    boolean mouseInNeedOfCorrection = true;

    public void glfwCursorPos(long windowID, double dx, double dy) {
        if (mouseInNeedOfCorrection) {
            dx = 0;
            dy = 0;
            GLFW.glfwSetCursorPos(windowID, dx, dy);
            mouseInNeedOfCorrection = false;
        }
        mouseDx = dx;
        mouseDy = dy;
    }

    private final Player player;


    public Camera(Player player) {
        this.player = player;
        GLFW.glfwSetScrollCallback(Window.window, GLFWScrollCallback.create(this::glfwScrollCallback));
        GLFW.glfwSetCursorPosCallback(Window.window, GLFWCursorPosCallback.create(this::glfwCursorPos));
    }

    public void move(float x, float y, float z) {
        position.add(x, y, z);
    }

    public void tick() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPos(horizontalDistance, verticalDistance);

        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    private float calculateHorizontalDistance() {
        return distanceFromPlayer * Math.cos(Math.toRadians(pitch));
    }
    private float calculateVerticalDistance() {
        return distanceFromPlayer * Math.sin(Math.toRadians(pitch));
    }

    private void calculateCameraPos(float horizontalDistance, float verticalDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = horizontalDistance * Math.sin(Math.toRadians(theta));
        float offsetZ = horizontalDistance * Math.cos(Math.toRadians(theta));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance + 5;
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

    private void calculateZoom() {
        float zoomLevel = (float) scrollValue;
        distanceFromPlayer -= zoomLevel;
        distanceFromPlayer =  Math.clamp(10, 168, distanceFromPlayer);
        scrollValue = 0;
    }

    private void calculatePitch() {
        float pitchChange = (float)mouseDy - lastValueY; // *0.1f
        pitch += pitchChange;
        pitch = Math.clamp(5, 70, pitch);
        lastValueY = (float) mouseDy;
    }

    private void calculateAngleAroundPlayer() {
        float angleChange = (float) mouseDx - lastValueX; // *0.1f
        angleAroundPlayer -= angleChange;

        angleAroundPlayer = Math.clamp(-115, 115, angleAroundPlayer);
        lastValueX = (float) mouseDx;
    }
}
