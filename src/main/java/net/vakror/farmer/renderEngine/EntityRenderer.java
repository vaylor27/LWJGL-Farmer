package net.vakror.farmer.renderEngine;

import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.shader.statiic.PerPixelStaticShader;
import net.vakror.farmer.renderEngine.util.Mth;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

public class EntityRenderer extends AbstractRenderer {

	public EntityRenderer(PerPixelStaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjection(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel texturedModel : entities.keySet()) {
			prepareRenderable(texturedModel.getRawModel(), texturedModel.getTexture());
			List<Entity> batch = entities.get(texturedModel);
			for (Entity entity : batch) {
				prepareInstance(entity);
				draw(texturedModel.getRawModel().getVertexCount());
			}
			unbindTexturedModel();
		}
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transforms = Mth.createTransformationMatrix(entity);
		((PerPixelStaticShader) shader).loadTransformationMatrix(transforms);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}
