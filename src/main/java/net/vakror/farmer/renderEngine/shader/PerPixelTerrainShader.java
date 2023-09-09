package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;

public class PerPixelTerrainShader extends ShaderProgram{

	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int lightPositionLocation;
	private int lightColorLocation;
	private int ambientLightLocation;
	private int useFakeLightingLocation;

	public PerPixelTerrainShader() {
		super(new ResourcePath("per-pixel/terrain/vertexShader"), new ResourcePath("per-pixel/terrain/fragmentShader"));
	}

	public PerPixelTerrainShader(ResourcePath resourcePath, ResourcePath resourcePath1) {
		super(resourcePath, resourcePath1);
	}

	@Override
	protected void getAllUniformLoactions() {
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		lightPositionLocation = super.getUniformLocation("lightPosition");
		lightColorLocation = super.getUniformLocation("lightColor");
		ambientLightLocation = super.getUniformLocation("ambientLight");
		useFakeLightingLocation = super.getUniformLocation("useFakeLighting");
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

	public void loadLight(Light light, float ambientLight) {
		super.loadVector(lightPositionLocation, light.getPosition());
		super.loadVector(lightColorLocation, light.getColor());
		super.loadFloat(ambientLightLocation, ambientLight);
	}

	@Override
	public void loadFakeLighting(boolean useFakeLighting) {
		super.loadBoolean(useFakeLightingLocation, useFakeLighting);
	}
}
