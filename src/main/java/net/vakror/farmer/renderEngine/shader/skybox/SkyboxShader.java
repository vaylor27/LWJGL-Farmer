package net.vakror.farmer.renderEngine.shader.skybox;

import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.shader.ShaderProgram;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;

public class SkyboxShader extends ShaderProgram {

    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    public SkyboxShader() {
        super(new ResourcePath("skybox/vertexShader"), new ResourcePath("skybox/fragmentShader"));
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(projectionMatrixLocation, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Mth.createViewMatrix(camera);
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
        super.loadMatrix(viewMatrixLocation, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
