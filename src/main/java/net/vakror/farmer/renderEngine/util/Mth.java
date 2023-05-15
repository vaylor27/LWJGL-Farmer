package net.vakror.farmer.renderEngine.util;

import net.vakror.farmer.renderEngine.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Mth {

    public static Matrix4f createTransformationMatrix(Entity entity) {
        return createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
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
}
