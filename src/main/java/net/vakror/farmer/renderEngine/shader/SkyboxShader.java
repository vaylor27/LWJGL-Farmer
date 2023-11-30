package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.shader.ShaderProgram;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;

public class SkyboxShader extends ShaderProgram {

    public SkyboxShader() {
        super(new ResourcePath("skybox/vertexShader"), new ResourcePath("skybox/fragmentShader"));
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix("projectionMatrix", matrix);
    }

    public void loadViewMatrix() {
        Matrix4f matrix = Mth.createViewMatrix();
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
        super.loadMatrix("viewMatrix", matrix);
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
