package net.vakror.farmer.renderEngine.shader.gui;

import net.vakror.farmer.renderEngine.shader.ShaderProgram;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;

public class GuiShader extends ShaderProgram {

    private int transformationMatrixLocation;

    public GuiShader() {
        super(new ResourcePath("gui/vertexShader"), new ResourcePath("gui/fragmentShader"));
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(transformationMatrixLocation, matrix);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
