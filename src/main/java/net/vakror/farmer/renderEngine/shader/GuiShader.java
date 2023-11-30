package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;

public class GuiShader extends ShaderProgram {
    public GuiShader() {
        super(new ResourcePath("gui/vertexShader"), new ResourcePath("gui/fragmentShader"));
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix("transformationMatrix", matrix);
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
