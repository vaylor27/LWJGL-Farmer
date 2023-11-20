package net.vakror.farmer.renderEngine.renderer;

import net.vakror.farmer.renderEngine.shader.terrain.SpecularTerrainShader;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.util.Mth;
import org.joml.Matrix4f;

import java.util.List;

public class TerrainRenderer extends AbstractRenderer{

    public TerrainRenderer(SpecularTerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjection(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains) {
        for (Terrain terrain: terrains) {
            prepareRenderable(terrain.getModel(), terrain.getTexture());
            loadModelMatrix(terrain);
            draw(terrain.getModel().getVertexCount());
            unbindTexturedModel();
        }
    }

    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = Mth.createTransformationMatrix(terrain);
        ((SpecularTerrainShader) shader).loadTransformationMatrix(transformationMatrix);
    }
}
