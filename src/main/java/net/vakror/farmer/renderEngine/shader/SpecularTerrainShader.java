package net.vakror.farmer.renderEngine.shader;

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

	public SpecularTerrainShader() {
		super(new ResourcePath("specular/terrain/vertexShader"), new ResourcePath("specular/terrain/fragmentShader"));
	}

	public void loadClipPlane(Vector4f plane) {
		super.loadVector4("plane", plane);
	}

	@Override
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat("shineDamper", damper);
		super.loadFloat("reflectivity", reflectivity);
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	public void loadSkyColor(Vector3f skyColor) {
		super.loadVector3("skyColor", skyColor);
	}


	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix("transformationMatrix", matrix);
	}

	public void loadProjection(Matrix4f projection) {
		super.loadMatrix("projectionMatrix", projection);
	}

	public void loadViewMatrix() {
		super.loadMatrix("viewMatrix", Mth.createViewMatrix());
	}

	public void loadLights(List<Light> lights, float ambientLight) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector3("lightPosition[" + i + "]", lights.get(i).getPosition());
				super.loadVector3("lightColor[" + i + "]", lights.get(i).getColor());
				super.loadVector3("attenuation[" + i + "]", lights.get(i).getAttenuation());
			} else {
				super.loadVector3("lightPosition[" + i + "]", new Vector3f(0.0f, 0.0f, 0.0f));
				super.loadVector3("lightColor[" + i + "]", new Vector3f(0.0f, 0.0f, 0.0f));
				super.loadVector3("attenuation[" + i + "]", new Vector3f(1, 0, 0));
			}
		}
		super.loadFloat("ambientLight", ambientLight);
	}

	@Override
	public void loadFakeLighting(boolean useFakeLighting) {
		super.loadBoolean("useFakeLighting", useFakeLighting);
	}
}
