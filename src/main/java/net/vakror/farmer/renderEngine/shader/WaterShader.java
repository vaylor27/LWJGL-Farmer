package net.vakror.farmer.renderEngine.shader;


import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class WaterShader extends ShaderProgram {
	public WaterShader() {
		super(new ResourcePath("water/vertexShader"), new ResourcePath("water/fragmentShader"));
	}

	@Override
	public void bindAttributes() {
		bindAttribute(0, "position");
	}

	public void loadNearAndFarPlane(float nearPlane, float farPlane) {
		super.loadVector2("nearFarPlane", new Vector2f(nearPlane, farPlane));
	}

	public void loadLight(Light light) {
		super.loadVector3("lightColor", light.getColor());
		super.loadVector3("lightPosition", light.getPosition());
	}

	public void loadMoveFactor(float factor) {
		loadFloat("moveFactor", factor);
	}
	public void connectTextureUnits() {
		super.loadInt("reflectionTexture", 0);
		super.loadInt("refractionTexture", 1);
		super.loadInt("dudvMap", 2);
		super.loadInt("normalMap", 3);
		super.loadInt("depthMap", 4);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix("projectionMatrix", projection);
	}
	
	public void loadViewMatrix(){
		Matrix4f viewMatrix = Mth.createViewMatrix();
		loadMatrix("viewMatrix", viewMatrix);

		loadVector3("cameraPosition", Camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix("modelMatrix", modelMatrix);
	}

}
