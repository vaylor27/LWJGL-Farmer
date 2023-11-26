package net.vakror.farmer.renderEngine.shader;


import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.shader.ShaderProgram;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class WaterShader extends ShaderProgram {

	private int modelMatrixLocation;
	private int viewMatrixLocation;
	private int projectionMatrixLocation;
	private int reflectionTextureLocation;
	private int refractionTextureLocation;
	private int dudvmapLocation;
	private int moveFactorLocation;
	private int cameraPositionLocation;
	private int normalMapLocation;
	private int lightPositionLocation;
	private int lightColorLocation;
	private int depthMapLocation;
	private int nearFarPlaneLocation;

	public WaterShader() {
		super(new ResourcePath("water/vertexShader"), new ResourcePath("water/fragmentShader"));
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		projectionMatrixLocation = getUniformLocation("projectionMatrix");
		viewMatrixLocation = getUniformLocation("viewMatrix");
		modelMatrixLocation = getUniformLocation("modelMatrix");
		reflectionTextureLocation = getUniformLocation("reflectionTexture");
		refractionTextureLocation = getUniformLocation("refractionTexture");
		dudvmapLocation = getUniformLocation("dudvMap");
		moveFactorLocation = getUniformLocation("moveFactor");
		cameraPositionLocation = getUniformLocation("cameraPosition");
		normalMapLocation = getUniformLocation("normalMap");
		lightColorLocation = getUniformLocation("lightColor");
		lightPositionLocation = getUniformLocation("lightPosition");
		depthMapLocation = getUniformLocation("depthMap");
		nearFarPlaneLocation = getUniformLocation("nearFarPlane");
	}

	public void loadNearAndFarPlane(float nearPlane, float farPlane) {
		super.loadVector2(nearFarPlaneLocation, new Vector2f(nearPlane, farPlane));
	}

	public void loadLight(Light light) {
		super.loadVector3(lightColorLocation, light.getColor());
		super.loadVector3(lightPositionLocation, light.getPosition());
	}

	public void loadMoveFactor(float factor) {
		loadFloat(moveFactorLocation, factor);
	}
	public void connectTextureUnits() {
		super.loadInt(reflectionTextureLocation, 0);
		super.loadInt(refractionTextureLocation, 1);
		super.loadInt(dudvmapLocation, 2);
		super.loadInt(normalMapLocation, 3);
		super.loadInt(depthMapLocation, 4);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(projectionMatrixLocation, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Mth.createViewMatrix(camera);
		loadMatrix(viewMatrixLocation, viewMatrix);

		loadVector3(cameraPositionLocation, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(modelMatrixLocation, modelMatrix);
	}

}
