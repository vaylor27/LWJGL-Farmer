package net.vakror.farmer.renderEngine.util;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Mth {

    public static Matrix4f createTransformationMatrix(Entity entity) {
        return createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
    }

    public static Matrix4f createTransformationMatrix(Terrain terrain) {
        return createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
    }

    private static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation);
        matrix.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
        matrix.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
        matrix.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
        matrix.scale(scale);
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
        viewMatrix.translate(camera.getPosition().negate());
        camera.getPosition().negate();
        return viewMatrix;
    }

    //TODO: make these configurable

    public static Matrix4f createProjectionMatrix() {
        Matrix4f projectionMatrix;
        float aspectRatio = (float) Window.WIDTH / (float) Window.HEIGHT;
        float y_scale = (1f / (float) Math.tan(Math.toRadians(FarmerGameMain.options.fov / 2f))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FarmerGameMain.options.farPlane - FarmerGameMain.options.nearPlane;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FarmerGameMain.options.farPlane + FarmerGameMain.options.nearPlane) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * FarmerGameMain.options.nearPlane * FarmerGameMain.options.farPlane) / frustum_length));
        projectionMatrix.m33(0);
        return projectionMatrix;
    }
}
