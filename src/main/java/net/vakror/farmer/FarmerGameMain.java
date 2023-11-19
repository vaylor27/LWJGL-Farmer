package net.vakror.farmer;

import net.vakror.farmer.register.option.BooleanOption;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
import net.vakror.farmer.renderEngine.mouse.MousePicker;
import net.vakror.farmer.renderEngine.registry.core.RegistryLocation;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import org.lwjgl.glfw.GLFW;

public class FarmerGameMain {
    static {
        System.setProperty("java.awt.headless", "true");
        DefaultRegistries.registerDefaults();
        Window.init();
    }

    public static Camera camera = new Camera();
    public static Loader loader = new Loader();
    public static MasterRenderer renderer = new MasterRenderer(loader);
    public static MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), DevTesting.terrain);

    public static void main(String[] args) {

        DevTesting.runTest(loader);

        while(!GLFW.glfwWindowShouldClose(Window.window)) {
            DevTesting.tick();
        }
        ((BooleanOption) DefaultRegistries.OPTIONS.get(new RegistryLocation("useAmbientLight"))).setValue(false);

        DevTesting.guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        Window.closeDisplay();
        Options.save();
    }

}
