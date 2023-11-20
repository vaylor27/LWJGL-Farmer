package net.vakror.farmer.renderEngine.shader.terrain;

import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.shader.ShaderProgram;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class SpecularTerrainShader extends ShaderProgram {
	private static final int MAX_LIGHTS = 5;

	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int[] lightPositionLocation;
	private int[] lightColorLocation;
	private int[] attenuationLocation;
	private int ambientLightLocation;
	private int useFakeLightingLocation;
	private int skyColorLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int planeLocation;

	public SpecularTerrainShader() {
		super(new ResourcePath("specular/terrain/vertexShader"), new ResourcePath("specular/terrain/fragmentShader"));
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		ambientLightLocation = super.getUniformLocation("ambientLight");
		useFakeLightingLocation = super.getUniformLocation("useFakeLighting");
		skyColorLocation = super.getUniformLocation("skyColor");
		planeLocation = super.getUniformLocation("plane");

		lightPositionLocation = new int[MAX_LIGHTS];
		lightColorLocation = new int[MAX_LIGHTS];
		attenuationLocation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			lightPositionLocation[i] = super.getUniformLocation("lightPosition[" + i + "]");
			lightColorLocation[i] = super.getUniformLocation("lightColor[" + i + "]");
			attenuationLocation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
	}

	public void loadClipPlane(Vector4f plane) {
		super.loadVector4(planeLocation, plane);
	}

	@Override
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(shineDamperLocation, damper);
		super.loadFloat(reflectivityLocation, reflectivity);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	public void loadSkyColor(Vector3f skyColor) {
		super.loadVector3(skyColorLocation, skyColor);
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
	public void loadFakeLighting(boolean useFakeLighting) {
		super.loadBoolean(useFakeLightingLocation, useFakeLighting);
	}
}
