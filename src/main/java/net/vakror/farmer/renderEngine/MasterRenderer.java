package net.vakror.farmer.renderEngine;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.shader.statiic.PerPixelStaticShader;
import net.vakror.farmer.renderEngine.shader.terrain.PerPixelTerrainShader;
import net.vakror.farmer.renderEngine.shader.statiic.SpecularStaticShader;
import net.vakror.farmer.renderEngine.shader.terrain.SpecularTerrainShader;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private final PerPixelStaticShader entityShader = Options.useSpecularLighting() ? new SpecularStaticShader(): new PerPixelStaticShader();
    private EntityRenderer entityRenderer;

    private final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private final List<Terrain> terrains = new ArrayList<>();

    private TerrainRenderer terrainRenderer;
    private final PerPixelTerrainShader terrainShader = Options.useSpecularLighting() ? new SpecularTerrainShader(): new PerPixelTerrainShader();

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

    public void render(List<Light> lights, Camera camera) {
        prepare();
        renderEntities(lights, camera);
        renderTerrain(lights, camera);
    }

    private void renderEntities(List<Light> lights, Camera camera) {
        entityShader.start();
        entityShader.loadSkyColor(Options.skyColor());
        entityShader.loadLights(lights, Options.ambientLight());
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        entityShader.stop();
        entities.clear();
    }

    private void renderTerrain(List<Light> light, Camera camera) {
        terrainShader.start();
        terrainShader.loadSkyColor(Options.skyColor());
        terrainShader.loadLights(light, Options.ambientLight());
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
    }

    public void prepare(){
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Vector3f skyColor = Options.skyColor();
        GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
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
