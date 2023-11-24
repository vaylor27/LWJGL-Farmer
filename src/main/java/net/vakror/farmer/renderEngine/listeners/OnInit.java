package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.gui.ResetPositionGui;
import net.vakror.farmer.renderEngine.listener.*;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.OBJLoader;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;
import net.vakror.farmer.renderEngine.water.WaterTile;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

import static net.vakror.farmer.FarmerGameMain.*;
import static net.vakror.farmer.renderEngine.mouse.InputUtil.*;
import static net.vakror.farmer.renderEngine.camera.Camera.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class OnInit implements InitializeListener {

    @Override
    public void onInit() {
        setupGui();
        setupWater();
        setupObjects();
        setupLight();
        setupMouse();
        setupListeners();
    }

    private void setupWater() {
        addWater(new WaterTile(Terrain.SIZE / 2, Terrain.SIZE / 2, -2f));
    }

    public static void addWater(WaterTile tile) {
        waterTiles.add(tile);
        fbos.computeIfAbsent(tile.getHeight(), WaterFrameBuffers::new);
    }

    private void setupObjects() {
        RawModel model = OBJLoader.loadOBJ(new ResourcePath("tree"), loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(new ResourcePath("tree")), 1, 0, false, true);
        TexturedModel staticModel = new TexturedModel(model, texture);

        RawModel rawFern = OBJLoader.loadOBJ(new ResourcePath("fern"), loader);
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture(new ResourcePath("fern1Atlas")), 1, 0, true, true);
        fernTexture.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(rawFern, fernTexture);

        RawModel rawGrass = OBJLoader.loadOBJ(new ResourcePath("grassModel"), loader);
        ModelTexture grassTexture = new ModelTexture(loader.loadTexture(new ResourcePath("grassTextureAtlas")), 1, 0, true, true);
        grassTexture.setNumberOfRows(2);
        TexturedModel grass = new TexturedModel(rawGrass, grassTexture);

        Random random = new Random(676452);
        for (int i = 0; i< 400; i++) {
            if (i % 20 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * +300;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
            } if (i % 5 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * +300;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grass, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));

                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * +300;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
            }
        }
    }

    public static void setupGui() {
//        GuiTexture gui = new ResetPositionGui(fbos.getRefractionTexture(), loader.loadTexture(new ResourcePath("image")), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//        GuiTexture gui1 = new ResetPositionGui(fbos.getReflectionTexture(), loader.loadTexture(new ResourcePath("image")), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//        guis.add(gui);
//        guis.add(gui1);
    }

    private void setupMouse() {
        if (Options.startWithMouseCaptured()) {
            glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            InputUtil.isCursorDisabled = true;
        } else {
            glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            Listeners.getListeners(MouseCapturedListener.class).forEach(MouseCapturedListener::onReleased);
            InputUtil.isCursorDisabled = false;
        }

        fixMousePos();
    }

    public static void setupListeners() {
        Listeners.addListener(MouseCapturedListener.class, new CorrectCameraOnCapture());
        Listeners.addListener(MouseCapturedListener.class, new UnHoverGuisOnCapture());

        Listeners.addListener(MouseMovementListener.class, new OnGuiHover());

        Listeners.addListener(MouseButtonListener.class, new OnGuiClick());
        Listeners.addListener(MouseButtonListener.class, new OnTerrainClick());

        Listeners.addListener(CloseGameListener.class, new CleanUp());

        Listeners.addListener(RenderListener.class, new ReflectionRefractionNormalSceneFrameBufferTextureRenderer());
        Listeners.addListener(RenderListener.class, new TickMouseAndCamera());

    }

    public static void setupLight() {
        lights.add(new Light(new Vector3f(0, 1000, -1000), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0))); // sun
        lights.add(new Light(new Vector3f(185, 10, 293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(370, 17, 300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(293, 7, 305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
    }

    public static void fixMousePos() {
        GLFW.glfwSetCursorPos(Window.window, initialMouseCoords.x, initialMouseCoords.y);
        FarmerGameMain.camera.data.angleAroundFocused = 0;
        FarmerGameMain.camera.data.pitch = 20;
        FarmerGameMain.camera.data.yaw = 180;
        FarmerGameMain.camera.currentCameraFocusPoint = new Vector2f(500, 500);
        lastValueX = (float) initialMouseCoords.x;
        lastValueY = (float) initialMouseCoords.y;
        currentMousePos.setScreenCoordinates(initialMouseCoords.x, initialMouseCoords.y);
        previousCapturedPos.setScreenCoordinates(initialMouseCoords.x, initialMouseCoords.y);
    }
}
