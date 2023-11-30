package net.vakror.farmer.renderEngine.renderer;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.listener.register.AutoRegisterListener;
import net.vakror.farmer.renderEngine.listener.type.CloseGameListener;
import net.vakror.farmer.renderEngine.listener.type.RenderListener;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.shader.SpecularStaticShader;
import net.vakror.farmer.renderEngine.shader.SpecularTerrainShader;
import net.vakror.farmer.renderEngine.shader.WaterShader;
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
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

@AutoRegisterListener
public class MasterRenderer implements CloseGameListener, RenderListener {

    public static final Matrix4f projectionMatrix = Mth.createProjectionMatrix();
    private static final SpecularStaticShader entityShader = new SpecularStaticShader();
    private static EntityRenderer entityRenderer = new EntityRenderer(entityShader, projectionMatrix);

    private static final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private static final List<Terrain> terrains = new ArrayList<>();
    private static final SpecularTerrainShader terrainShader = new SpecularTerrainShader();
    private static TerrainRenderer terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    private static final SkyboxRenderer skyboxRenderer = new SkyboxRenderer(projectionMatrix);
    private static final WaterShader waterShader = new WaterShader();
    private static final WaterRenderer waterRenderer = new WaterRenderer(waterShader, projectionMatrix);

    public static GuiRenderer guiRenderer = new GuiRenderer();

    public static Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static void regenProjectionMatrix() {
        Matrix4f projectionMatrix = Mth.createProjectionMatrix();
        entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    public static void renderScene(List<Entity> entities, Terrain[][] terrains, List<Light> lights, Vector4f clipPlane, boolean shouldRenderWaterAndGuis) {
        for (Terrain[] terrains1 : terrains) {
            for (Terrain terrain : terrains1) {
                if (terrain != null) {
                    processTerrain(terrain);
                }
            }
        }
        for (Entity entity : entities) {
            processEntity(entity);
        }
        render(lights, clipPlane, shouldRenderWaterAndGuis);
    }

    public static void render(List<Light> lights, Vector4f clipPlane, boolean shouldRenderWaterAndGuis) {
        prepare();

        renderSkybox();

        glEnable(GL_DEPTH_TEST);
        renderEntities(lights, clipPlane);
        renderTerrain(lights, clipPlane);

        if (shouldRenderWaterAndGuis) {
            for (WaterTile tile: waterTiles) {
                waterRenderer.render(tile);
            }

            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
            guiRenderer.render(guis);
            glDepthMask(true);
            glEnable(GL_DEPTH_TEST);
        }
    }

    private static void renderSkybox() {
        skyboxRenderer.render();
    }

    private static void renderEntities(List<Light> lights, Vector4f clipPlane) {
        entityShader.start();
        entityShader.loadClipPlane(clipPlane);
        entityShader.loadSkyColor(Options.skyColor());
        entityShader.loadLights(lights, Options.ambientLight());
        entityShader.loadViewMatrix();
        entityRenderer.render(entities);
        entityShader.stop();
        entities.clear();
    }

    private static void renderTerrain(List<Light> light, Vector4f clipPlane) {
        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColor(Options.skyColor());
        terrainShader.loadLights(light, Options.ambientLight());
        terrainShader.loadViewMatrix();
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
    }

    public static void prepare() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Vector3f skyColor = Options.skyColor();
        GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
    }

    public static void processEntity(Entity entity) {
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

    public static void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void onGameClose() {
        entityShader.cleanUp();
        terrainShader.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
    }

    @Override
    public void onRender() {
        GL11.glEnable(GL_CLIP_DISTANCE0);
        fbos.forEach((height, fbo) -> {
            renderReflection(height, fbo);
            renderRefraction(height, fbo);
        });
        renderNormal();
    }

    private static void renderReflection(Float height, WaterFrameBuffers fbo) {
        fbo.bindReflectionFrameBuffer();
        float distance = 2 * (Camera.getPosition().y - height);
        Camera.getPosition().y -= distance;
        Camera.invertPitch();
        renderScene(FarmerGameMain.entities, FarmerGameMain.terrains, lights, new Vector4f(0, 1, 0, -height + 1f), false);
        Camera.getPosition().y += distance;
        Camera.invertPitch();
    }

    private static void renderRefraction(Float height, WaterFrameBuffers fbo) {
        fbo.bindRefractionFrameBuffer();
        renderScene(FarmerGameMain.entities, FarmerGameMain.terrains, lights, new Vector4f(0, -1, 0, height + 1f), false);
    }

    private static void renderNormal() {
        GL11.glDisable(GL_CLIP_DISTANCE0);
        WaterFrameBuffers.unbindCurrentFrameBuffer();
        renderScene(FarmerGameMain.entities, FarmerGameMain.terrains, lights, new Vector4f(0, -1, 0, 100000), true);
    }
}
