package net.vakror.farmer.renderEngine.shader;

import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "vertexShader";
	private static final String FRAGMENT_FILE = "fragmentShader";

	private int transformationMatrixLocation;
	private int projectionMatrixLocation;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLoactions() {
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(transformationMatrixLocation, matrix);
	}

	public void loadProjection(Matrix4f projection) {
		super.loadMatrix(projectionMatrixLocation, projection);
	}
}
