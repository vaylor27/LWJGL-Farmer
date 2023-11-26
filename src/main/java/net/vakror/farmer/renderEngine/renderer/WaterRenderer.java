package net.vakror.farmer.renderEngine.renderer;

import java.util.Map;


import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.shader.WaterShader;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;
import net.vakror.farmer.renderEngine.water.WaterTile;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static net.vakror.farmer.FarmerGameMain.lights;

public class WaterRenderer {

	private RawModel quad;
	private final WaterShader shader;
	private final Map<Float, WaterFrameBuffers> fbos;
	private final int dudvTexture;
	private final int waterNormalMapTexture;

	private static final float MOVE_SPEED = 0.03f;
	private float moveFactor = 0;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, Map<Float, WaterFrameBuffers> fbos) {
		this.shader = shader;
		this.fbos = fbos;
		dudvTexture = loader.loadTexture(new ResourcePath("dudv"));
		waterNormalMapTexture = loader.loadTexture(new ResourcePath("waterNormalMap"));
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(WaterTile tile, Camera camera) {
		prepareRender(camera, tile);
		Matrix4f modelMatrix = Mth.createTransformationMatrix(
				new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
				WaterTile.TILE_SIZE);
		shader.loadModelMatrix(modelMatrix);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		unbind();
	}
	
	private void prepareRender(Camera camera, WaterTile waterTile) {
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += MOVE_SPEED * Window.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadNearAndFarPlane(Options.nearPlane(), Options.farPlane());
		shader.loadLight(lights.get(0));
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.get(waterTile.getHeight()).getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.get(waterTile.getHeight()).getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterNormalMapTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.get(waterTile.getHeight()).getRefractionDepthTexture());

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
