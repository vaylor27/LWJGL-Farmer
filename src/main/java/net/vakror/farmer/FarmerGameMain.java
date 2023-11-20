package net.vakror.farmer;

import net.vakror.farmer.register.option.BooleanOption;
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
import net.vakror.farmer.renderEngine.registry.core.RegistryLocation;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.renderer.GuiRenderer;
import net.vakror.farmer.renderEngine.renderer.MasterRenderer;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;
import net.vakror.farmer.renderEngine.water.WaterTile;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class FarmerGameMain {
    static {
        System.setProperty("java.awt.headless", "true");
        DefaultRegistries.registerDefaults();
        Window.init();
    }

    public static Camera camera = new Camera();
    public static Loader loader = new Loader();
    public static WaterFrameBuffers fbos = new WaterFrameBuffers();

    public static MasterRenderer renderer = new MasterRenderer(loader, fbos);

    public static Terrain terrain = new Terrain(0, 0, FarmerGameMain.loader, new ModelTexture(FarmerGameMain.loader.loadTexture(new ResourcePath("grass")), 1, 0, false, false), new ResourcePath("heightmap"));
    public static MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);


    public static List<Entity> entities = new ArrayList<>();
    public static List<GuiTexture> guis = new ArrayList<>();
    public static List<Light> lights = new ArrayList<>();
    public static List<WaterTile> waterTiles = new ArrayList<>();

    public static void main(String[] args) {

        Listeners.addListener(InitializeListener.class, new OnInit());
        Listeners.getListeners(InitializeListener.class).forEach(InitializeListener::onInit);

        while(!GLFW.glfwWindowShouldClose(Window.window)) {
            Listeners.getListeners(RenderListener.class).forEach(RenderListener::onRender);
            Window.updateDisplay();
        }

        Listeners.getListeners(CloseGameListener.class).forEach(CloseGameListener::onGameClose);
        Window.closeDisplay();
    }

}
