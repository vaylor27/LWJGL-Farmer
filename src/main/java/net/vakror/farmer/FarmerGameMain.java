package net.vakror.farmer;

import net.harawata.appdirs.AppDirsFactory;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.listener.CloseGameListener;
import net.vakror.farmer.renderEngine.listener.InitializeListener;
import net.vakror.farmer.renderEngine.listener.Listeners;
import net.vakror.farmer.renderEngine.listener.RenderListener;
import net.vakror.farmer.renderEngine.listeners.OnInit;
import net.vakror.farmer.renderEngine.mouse.MousePicker;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.renderer.MasterRenderer;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;
import net.vakror.farmer.renderEngine.water.WaterTile;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipInputStream;

import static net.vakror.farmer.renderEngine.util.JVMUtil.restartJVM;

public class FarmerGameMain {
    static {
        System.setProperty("java.awt.headless", "true");
        DefaultRegistries.registerDefaults();
    }

    public static Camera camera = new Camera();
    public static Loader loader = new Loader();
    public static Map<Float, WaterFrameBuffers> fbos = new HashMap<>();

    public static MasterRenderer renderer;

    public static Terrain terrain;
    public static MousePicker picker;

    public static List<Entity> entities = new ArrayList<>();
    public static List<GuiTexture> guis = new ArrayList<>();
    public static List<Light> lights = new ArrayList<>();
    public static List<WaterTile> waterTiles = new ArrayList<>();
    public static final String appDirPath = AppDirsFactory.getInstance().getUserDataDir("james game", "0.0.1", "james");

    public static void main(String[] args) {
        if (restartJVM()) {
            return;
        }

        Window.init();
        if (ResourceDownloader.doesNeedToDownloadResources()) {
            ResourceDownloader.downloadResources();
        }

        renderer = new MasterRenderer(loader, fbos);
        terrain = new Terrain(0, 0, FarmerGameMain.loader, new ModelTexture(FarmerGameMain.loader.loadTexture(new ResourcePath("grass")), 1, 0, false, false), new ResourcePath("heightmap"));
        picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
        Listeners.addListener(InitializeListener.class, new OnInit());
        Listeners.getListeners(InitializeListener.class).forEach(InitializeListener::onInit);

        while (!GLFW.glfwWindowShouldClose(Window.window)) {
            Listeners.getListeners(RenderListener.class).forEach(RenderListener::onRender);
            Window.updateDisplay();
        }

        Listeners.getListeners(CloseGameListener.class).forEach(CloseGameListener::onGameClose);
        Window.closeDisplay();
    }
}
