package net.vakror.farmer.renderEngine.mouse;


import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.util.Mth;
import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class MousePicker {

    private static final int RECURSION_COUNT = 1000;
    private static final float RAY_RANGE = 3000;

    private Vector3f currentRay = new Vector3f();

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    private Terrain terrain;
    private Vector3f currentTerrainPoint;

    public MousePicker(Camera cam, Matrix4f projection, Terrain terrain) {
        camera = cam;
        projectionMatrix = projection;
        viewMatrix = Mth.createViewMatrix(camera);
        this.terrain = terrain;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public void update() {
        viewMatrix = Mth.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }
    }

    private Vector3f calculateMouseRay() {
        Vector3f normalizedCoords = getNormalisedDeviceCoordinates();
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
        Matrix4f m = new Matrix4f();
        Vector4f ray_eye = clipCoords.mul(projectionMatrix.invert(m));
        ray_eye = new Vector4f(ray_eye.x, ray_eye.y, -1.0f, 0.0f);
        Vector4f ray = (ray_eye.mul(viewMatrix.invert(m)));
        Vector3f ray_wor = new Vector3f(ray.x, ray.y, ray.z);
        // don't forget to normalise the vector at some point
        ray_wor = ray_wor.normalize();
        return ray_wor;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = new Matrix4f();
        viewMatrix.invert(invertedView);
        Vector4f rayWorld = invertedView.transform(eyeCoords);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = new Matrix4f();
        projectionMatrix.invert(invertedProjection);
        Vector4f eyeCoords = invertedProjection.transform(clipCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector3f getNormalisedDeviceCoordinates() {
        int[] screenWidth = new int[1];
        int[] screenHeight = new int[1];
        GLFW.glfwGetWindowSize(Window.window, screenWidth, screenHeight);
        float x = (2.0f * InputUtil.currentMousePos.getAsScreenCoords().x) / screenWidth[0] - 1.0f;
        float y = 1.0f - (2.0f * InputUtil.currentMousePos.getAsScreenCoords().y) / screenHeight[0];
        float z = 1.0f;
        return new Vector3f(x, y, z);
    }

    //**********************************************************

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return start.add(scaledRay);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.x, endPoint.z);
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        return !isUnderGround(startPoint) && isUnderGround(endPoint);
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.x, testPoint.z);
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z);
        }
        return testPoint.y < height;
    }

    private Terrain getTerrain(float worldX, float worldZ) {
        return terrain;
    }

}