package net.vakror.farmer.renderEngine.renderer;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.shader.statiic.SpecularStaticShader;
import net.vakror.farmer.renderEngine.shader.terrain.SpecularTerrainShader;
import net.vakror.farmer.renderEngine.shader.water.WaterShader;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;
import net.vakror.farmer.renderEngine.water.WaterTile;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.vakror.farmer.FarmerGameMain.*;
import static net.vakror.farmer.FarmerGameMain.loader;
import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private final SpecularStaticShader entityShader = new SpecularStaticShader();
    private EntityRenderer entityRenderer;

    private final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private final List<Terrain> terrains = new ArrayList<>();

    private TerrainRenderer terrainRenderer;
    private final SpecularTerrainShader terrainShader = new SpecularTerrainShader();

    private final SkyboxRenderer skyboxRenderer;
    private static final Matrix4f projectionMatrix = Mth.createProjectionMatrix();;

    private final WaterShader waterShader = new WaterShader();
    private final WaterRenderer waterRenderer;

    public static GuiRenderer guiRenderer = new GuiRenderer(loader);

    public MasterRenderer(Loader loader, Map<Float, WaterFrameBuffers> fbos) {
        enableCulling();
        waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix, fbos);
        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
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

    public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane, boolean shouldRenderWaterAndGuis) {
        for (Terrain terrain : terrains) {
            processTerrain(terrain);
        }
        for (Entity entity : entities) {
            processEntity(entity);
        }
        render(lights, camera, clipPlane, shouldRenderWaterAndGuis);
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane, boolean shouldRenderWaterAndGuis) {
        prepare();

        renderSkybox(camera);

        glEnable(GL_DEPTH_TEST);
        renderEntities(lights, camera, clipPlane);
        renderTerrain(lights, camera, clipPlane);

        if (shouldRenderWaterAndGuis) {
            for (WaterTile tile: waterTiles) {
                waterRenderer.render(tile, camera);
            }

            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
            guiRenderer.render(guis);
            glDepthMask(true);
            glEnable(GL_DEPTH_TEST);
        }
    }

    private void renderSkybox(Camera camera) {
        skyboxRenderer.render(camera);
    }

    private void renderEntities(List<Light> lights, Camera camera, Vector4f clipPlane) {
        entityShader.start();
        entityShader.loadClipPlane(clipPlane);
        entityShader.loadSkyColor(Options.skyColor());
        entityShader.loadLights(lights, Options.ambientLight());
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        entityShader.stop();
        entities.clear();
    }

    private void renderTerrain(List<Light> light, Camera camera, Vector4f clipPlane) {
        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColor(Options.skyColor());
        terrainShader.loadLights(light, Options.ambientLight());
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
    }

    public void prepare() {
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
        waterShader.cleanUp();
        guiRenderer.cleanUp();
    }
}
