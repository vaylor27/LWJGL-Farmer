package net.vakror.farmer.renderEngine.util;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Mth {

    public static float barryCentricFunction(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z)*(pos.x - p3.x)+(p3.x - p2.x) * (pos.y - p3.z))/det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x)+(p1.x - p3.x)*(pos.y - p3.z))/det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(new Vector3f(translation.x, translation.y, 0));
        matrix.scale(new Vector3f(scale.x, scale.y, 1f));
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Entity entity) {
        return createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
    }

    public static Matrix4f createTransformationMatrix(Terrain terrain) {
        return createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation);
        matrix.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
        matrix.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
        matrix.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
        matrix.scale(scale);
        return matrix;
    }

    public static Matrix4f createViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(Camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(Camera.getYaw()), new Vector3f(0, 1, 0));
        viewMatrix.translate(Camera.getPosition().negate());
        Camera.getPosition().negate();
        return viewMatrix;
    }

    //TODO: make these configurable

    public static Matrix4f createProjectionMatrix() {
        Matrix4f projectionMatrix;
        float aspectRatio = (float) Window.WIDTH / (float) Window.HEIGHT;
        float y_scale = (1f / (float) Math.tan(Math.toRadians(Options.fov() / 2f))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustum_length = Options.farPlane() - Options.nearPlane();

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((Options.farPlane() + Options.nearPlane()) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * Options.nearPlane() * Options.farPlane()) / frustum_length));
        projectionMatrix.m33(0);
        return projectionMatrix;
    }
}
