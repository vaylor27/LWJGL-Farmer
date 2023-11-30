package net.vakror.farmer.renderEngine.shader;


import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;

public class ParticleShader extends ShaderProgram {
	public ParticleShader() {
		super(new ResourcePath("particle/vertexShader"), new ResourcePath("particle/fragmentShader"));
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix("modelViewMatrix", modelView);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix("projectionMatrix", projectionMatrix);
	}

}
