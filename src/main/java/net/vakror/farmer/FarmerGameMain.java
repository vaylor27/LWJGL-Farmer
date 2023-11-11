package net.vakror.farmer;

import net.vakror.farmer.register.option.BooleanOption;
import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.entity.Player;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
import net.vakror.farmer.renderEngine.registry.core.RegistryLocation;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.shader.PerPixelStaticShader;
import net.vakror.farmer.renderEngine.shader.SpecularStaticShader;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.OBJLoader;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FarmerGameMain {
    public static Camera camera = null;
    public static MasterRenderer renderer;
    public static Player player;

    public static void main(String[] args) {

        DefaultRegistries.registerDefaults();
        Window.init();
        Loader loader = new Loader();

        DevTesting.runTest(loader);

        while(!GLFW.glfwWindowShouldClose(Window.window)) {
            DevTesting.tick();
        }
        ((BooleanOption) DefaultRegistries.OPTIONS.get(new RegistryLocation("useAmbientLight"))).setValue(false);

        renderer.cleanUp();
        loader.cleanUp();
        Window.closeDisplay();
        Options.save();
    }

}
