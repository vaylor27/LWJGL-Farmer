package net.vakror.farmer.renderEngine;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.shader.PerPixelStaticShader;
import net.vakror.farmer.renderEngine.shader.PerPixelTerrainShader;
import net.vakror.farmer.renderEngine.shader.SpecularStaticShader;
import net.vakror.farmer.renderEngine.shader.SpecularTerrainShader;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.util.Mth;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private final PerPixelStaticShader entityShader = FarmerGameMain.options.useSpecularLighting ? new SpecularStaticShader(): new PerPixelStaticShader();
    private EntityRenderer entityRenderer;

    private final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private final List<Terrain> terrains = new ArrayList<>();

    private TerrainRenderer terrainRenderer;
    private final PerPixelTerrainShader terrainShader = FarmerGameMain.options.useSpecularLighting ? new SpecularTerrainShader(): new PerPixelTerrainShader();

    public MasterRenderer() {
        enableCulling();
        Matrix4f projectionMatrix = Mth.createProjectionMatrix();
        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public void regenProjectionMatrix() {
        Matrix4f projectionMatrix = Mth.createProjectionMatrix();
        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    public void render(Light light, Camera camera) {
        prepare();
        renderEntities(light, camera);
        renderTerrain(light, camera);
    }

    private void renderEntities(Light light, Camera camera) {
        entityShader.start();
        entityShader.loadSkyColor(FarmerGameMain.options.skyColor);
        entityShader.loadLight(light, FarmerGameMain.options.ambientLight);
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        entityShader.stop();
        entities.clear();
    }

    private void renderTerrain(Light light, Camera camera) {
        terrainShader.start();
        terrainShader.loadSkyColor(FarmerGameMain.options.skyColor);
        terrainShader.loadLight(light, FarmerGameMain.options.ambientLight);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
    }

    public void prepare(){
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(FarmerGameMain.options.skyColor.x, FarmerGameMain.options.skyColor.y, FarmerGameMain.options.skyColor.z, 1);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void cleanUp() {
        entityShader.cleanUp();
        terrainShader.cleanUp();
    }
}
