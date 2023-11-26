package net.vakror.farmer;

import net.harawata.appdirs.AppDirsFactory;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.font.mesh.FontType;
import net.vakror.farmer.renderEngine.font.mesh.GUIText;
import net.vakror.farmer.renderEngine.font.render.TextMaster;
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
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.*;

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
    public static final int assetVersion = 3;
    public static FontType font;
    public static GUIText text;

    public static void main(String[] args) {

        if (ResourceDownloader.doesNeedToDownloadResources()) {
            ResourceDownloader.downloadResources();
        }
        if (restartJVM()) {
            return;
        }

        Window.init();
        TextMaster.init(loader);
        font = new FontType(loader.loadTexture(new ResourcePath("font/arial"), false), new ResourcePath("arial"));
        text = new GUIText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse at tincidunt ex. Morbi vehicula, orci eu pretium convallis, nisl libero dictum enim, a dignissim sem libero vitae nibh. Quisque suscipit tellus venenatis felis tempor, ut facilisis velit laoreet. Quisque id libero vel nibh pellentesque porttitor. Vivamus sed sem lobortis, mollis orci ut, vehicula mi. In vel ullamcorper diam. Pellentesque at tristique justo. Fusce aliquet, sem sed placerat molestie, risus nisl dictum metus, vitae consectetur leo nulla vitae velit. Cras faucibus nec eros a vestibulum. Phasellus ornare massa sit amet molestie luctus. Ut ut facilisis eros, ac commodo eros. Phasellus tincidunt at magna aliquam porttitor. Fusce ut elementum sem. Pellentesque nec elit fermentum, porta turpis sit amet, interdum neque. Praesent velit est, mattis at vulputate ut, volutpat vel metus. Nam ut enim quis mauris rutrum blandit. Sed sagittis non arcu eget semper. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed vel diam at magna ullamcorper posuere in et nunc. Vestibulum tempus laoreet mauris, ac congue ante rutrum vitae. Nam sollicitudin leo elit, et tempor dui sodales sed. Curabitur erat risus, tempor quis nulla at, vulputate eleifend tellus. Maecenas in risus lobortis, tincidunt lectus ut, efficitur est. Phasellus vestibulum magna sed pretium aliquet. Integer nec scelerisque arcu. Pellentesque felis est, luctus consectetur velit sollicitudin, fermentum rutrum urna. Cras porttitor arcu eu sem mattis efficitur. ", 1, font, new Vector2f(0, 0), 1f, true);

        TextMaster.loadText(text);

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
