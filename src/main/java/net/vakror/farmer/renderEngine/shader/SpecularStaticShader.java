package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class SpecularStaticShader extends ShaderProgram {
	private static final int MAX_LIGHTS = 5;

	public SpecularStaticShader() {
		super(new ResourcePath("specular/vertexShader"), new ResourcePath("specular/fragmentShader"));
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
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat("numberOfRows", numberOfRows);
	}

	@Override
	public void loadOffset(float x, float y) {
		super.loadVector2("offset", new Vector2f(x, y));
	}

	public void loadSkyColor(Vector3f skyColor) {
		super.loadVector3("skyColor", skyColor);
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
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
