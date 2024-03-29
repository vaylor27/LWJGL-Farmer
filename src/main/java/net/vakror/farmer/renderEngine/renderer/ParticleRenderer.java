package net.vakror.farmer.renderEngine.renderer;

import java.util.List;

import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.particle.Particle;
import net.vakror.farmer.renderEngine.shader.ParticleShader;
import net.vakror.farmer.renderEngine.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private RawModel quad;
	private ParticleShader shader;
	
	public ParticleRenderer(Matrix4f projectionMatrix){
		quad = Loader.loadToVAO(VERTICES, 2);
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<Particle> particles, Camera camera){
		Matrix4f viewMatrix = Mth.createViewMatrix();
		prepare();
		for (Particle particle : particles) {
			updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		finishRendering();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.translate(position, modelMatrix);
		modelMatrix.m00(viewMatrix.m00());
		modelMatrix.m01(viewMatrix.m10());
		modelMatrix.m02(viewMatrix.m20());
		modelMatrix.m10(viewMatrix.m01());
		modelMatrix.m11(viewMatrix.m11());
		modelMatrix.m12(viewMatrix.m21());
		modelMatrix.m20(viewMatrix.m02());
		modelMatrix.m21(viewMatrix.m12());
		modelMatrix.m22(viewMatrix.m22());

		modelMatrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1));
		modelMatrix.scale(new Vector3f(scale, scale, scale));
		Matrix4f modelViewMatrix = null;
		modelViewMatrix = viewMatrix.mul(modelMatrix, modelViewMatrix);
		shader.loadModelViewMatrix(modelViewMatrix);
	}
	
	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVertexCount());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}
