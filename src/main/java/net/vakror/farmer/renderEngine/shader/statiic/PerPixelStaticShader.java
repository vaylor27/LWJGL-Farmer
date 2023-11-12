package net.vakror.farmer.renderEngine.shader.statiic;

import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.shader.ShaderProgram;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class PerPixelStaticShader extends ShaderProgram {
	private static final int MAX_LIGHTS = 5;

	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int[] lightPositionLocation;
	private int[] lightColorLocation;
	private int[] attenuationLocation;
	private int ambientLightLocation;
	private int useFakeLightingLocation;
	private int densityLocation;
	private int gradientLocation;
	private int skyColorLocation;
	private int numberOfRowsLocation;
	private int offsetLocation;

	public PerPixelStaticShader() {
		super(new ResourcePath("per-pixel/vertexShader"), new ResourcePath("per-pixel/fragmentShader"));
	}

	public PerPixelStaticShader(ResourcePath resourcePath, ResourcePath resourcePath1) {
		super(resourcePath, resourcePath1);
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		ambientLightLocation = super.getUniformLocation("ambientLight");
		useFakeLightingLocation = super.getUniformLocation("useFakeLighting");
		densityLocation = super.getUniformLocation("density");
		gradientLocation = super.getUniformLocation("gradient");
		skyColorLocation = super.getUniformLocation("skyColor");
		numberOfRowsLocation = super.getUniformLocation("numberOfRows");
		offsetLocation = super.getUniformLocation("offset");

		lightPositionLocation = new int[MAX_LIGHTS];
		lightColorLocation = new int[MAX_LIGHTS];
		attenuationLocation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			lightPositionLocation[i] = super.getUniformLocation("lightPosition[" + i + "]");
			lightColorLocation[i] = super.getUniformLocation("lightColor[" + i + "]");
			attenuationLocation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

	@Override
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(numberOfRowsLocation, numberOfRows);
	}

	@Override
	public void loadOffset(float x, float y) {
		super.loadVector2(offsetLocation, new Vector2f(x, y));
	}

	public void loadSkyColor(Vector3f skyColor) {
		super.loadVector3(skyColorLocation, skyColor);
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

	public void loadLights(List<Light> lights, float ambientLight) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector3(lightPositionLocation[i], lights.get(i).getPosition());
				super.loadVector3(lightColorLocation[i], lights.get(i).getColor());
				super.loadVector3(attenuationLocation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector3(lightPositionLocation[i], new Vector3f(0.0f, 0.0f, 0.0f));
				super.loadVector3(lightColorLocation[i], new Vector3f(0.0f, 0.0f, 0.0f));
				super.loadVector3(attenuationLocation[i], new Vector3f(1, 0, 0));
			}
		}
		super.loadFloat(ambientLightLocation, ambientLight);
	}

	@Override
	public void loadFog(float density, float gradient) {
		super.loadFloat(densityLocation, density);
		super.loadFloat(gradientLocation, gradient);
	}

	@Override
	public void loadFakeLighting(boolean useFakeLighting) {
		super.loadBoolean(useFakeLightingLocation, useFakeLighting);
	}
}
