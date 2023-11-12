package net.vakror.farmer;

import net.vakror.farmer.register.option.BooleanOption;
import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Player;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
import net.vakror.farmer.renderEngine.registry.core.RegistryLocation;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import org.lwjgl.glfw.GLFW;

public class FarmerGameMain {
    public static Camera camera = null;
    public static MasterRenderer renderer;
    public static Player player;

    public static void main(String[] args) {

        System.setProperty("java.awt.headless", "true");
        DefaultRegistries.registerDefaults();
        Window.init();
        Loader loader = new Loader();

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
