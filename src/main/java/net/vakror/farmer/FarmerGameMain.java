package net.vakror.farmer;

import net.harawata.appdirs.AppDirsFactory;
import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.ResourceDownloader;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.font.mesh.FontType;
import net.vakror.farmer.renderEngine.font.mesh.GUIText;
import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.listener.Listeners;
import net.vakror.farmer.renderEngine.listener.type.CloseGameListener;
import net.vakror.farmer.renderEngine.listener.type.RenderListener;
import net.vakror.farmer.renderEngine.mod.ModLoader;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.Callbacks;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;
import net.vakror.farmer.renderEngine.water.WaterTile;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.vakror.farmer.renderEngine.util.JVMUtil.restartJVM;


public class FarmerGameMain {
    static {
        System.setProperty("java.awt.headless", "true");
        appDirPath = AppDirsFactory.getInstance().getUserDataDir("james game", "0.0.1", "james");
        DefaultRegistries.registerDefaults();
    }

    public static Map<Float, WaterFrameBuffers> fbos = new HashMap<>();

    public static Terrain[][] terrains;

    public static List<Entity> entities = new ArrayList<>();
    public static List<GuiTexture> guis = new ArrayList<>();
    public static List<Light> lights = new ArrayList<>();
    public static List<WaterTile> waterTiles = new ArrayList<>();
    public static final String appDirPath;
    public static final int assetVersion = 3;
    public static FontType font;
    public static GUIText text;
    public static final Logger LOGGER = LoggerFactory.getLogger("Farmer Game Main");
    public static String devModClass;

    public static void main(String[] args) {

        for (String arg : args) {
            String arg1 = arg.replace(" ", "");
            if (arg1.startsWith("ModInit=")) {
                String modClass = arg1.split("ModInit=")[1];
                devModClass = modClass.substring(0, modClass.lastIndexOf('.'));
            }
        }

        if (ResourceDownloader.doesNeedToDownloadResources()) {
            ResourceDownloader.downloadResources();
        }
        if (restartJVM()) {
            return;
        }

        terrains = new Terrain[10][10];
        Window.init();
        Callbacks.init();
        ModLoader.findAndLoadAllMods();
        font = new FontType(Loader.loadTexture(new ResourcePath("font/arial"), false), new ResourcePath("arial"));

        addTerrain(new Terrain(0, 0, new ModelTexture(Loader.loadTexture(new ResourcePath("grass")), 1, 0, false, false), new ResourcePath("heightmap")));

        Listeners.runEntryPoints(Listeners.findAllEntryPoints());
        Listeners.findAndRegisterAllAnnotatedListeners();

        while (!GLFW.glfwWindowShouldClose(Window.window)) {
            Listeners.getListeners(RenderListener.class).forEach(RenderListener::onRender);
            Window.updateDisplay();
        }

        Listeners.getListeners(CloseGameListener.class).forEach(CloseGameListener::onGameClose);
        Window.closeDisplay();
    }

    public static void addTerrain(Terrain terrain) {
        terrains[(int) terrain.x][(int) terrain.z] = terrain;
    }
}
