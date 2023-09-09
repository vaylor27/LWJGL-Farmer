package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;

public class PerPixelStaticShader extends ShaderProgram{

	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int lightPositionLocation;
	private int lightColorLocation;

	public PerPixelStaticShader() {
		super(new ResourcePath("per-pixel/vertexShader"), new ResourcePath("per-pixel/fragmentShader"));
	}

	public PerPixelStaticShader(ResourcePath resourcePath, ResourcePath resourcePath1) {
		super(resourcePath, resourcePath1);
	}

	@Override
	protected void getAllUniformLoactions() {
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		lightPositionLocation = super.getUniformLocation("lightPosition");
		lightColorLocation = super.getUniformLocation("lightColor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(transformationMatrixLocation, matrix);
	}

	public void loadProjection(Matrix4f projection) {
		super.loadMatrix(projectionMatrixLocation, projection);
	}

	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(viewMatrixLocation, Mth.createViewMatrix(camera));
	}

	public void loadLight(Light light) {
		super.loadVector(lightPositionLocation, light.getPosition());
		super.loadVector(lightColorLocation, light.getColor());
	}
}
