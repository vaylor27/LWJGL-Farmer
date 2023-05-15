package net.vakror.farmer.renderEngine;

import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.shader.StaticShader;
import net.vakror.farmer.renderEngine.util.Mth;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class Renderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	private Matrix4f projectionMatrix;

	public Renderer(StaticShader shader) {
		createProjectionMatrix();
		shader.start();
		shader.loadProjection(projectionMatrix);
		shader.stop();
	}
	
	public void prepare(){
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1, 0, 0, 1);
	}
	
	public void render(Entity entity, StaticShader shader) {
		TexturedModel texturedModel = entity.getModel();
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		Matrix4f transforms = Mth.createTransformationMatrix(entity);
		shader.loadTransformationMatrix(transforms);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Window.WIDTH / (float) Window.HEIGHT;
		float y_scale = (1f / (float) Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00(x_scale);
		projectionMatrix.m11(y_scale);
		projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
		projectionMatrix.m23(-1);
		projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
		projectionMatrix.m33(0);
	}

}
