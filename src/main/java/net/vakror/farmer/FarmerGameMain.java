package net.vakror.farmer;

import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
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
    public static Options options = new Options(true, 0.2f);
    public static MasterRenderer renderer;

    public static void main(String[] args) {

        Window.init();
        Loader loader = new Loader();

        RawModel model = OBJLoader.loadOBJ(new ResourcePath("tree"), loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(new ResourcePath("tree")), 10, 0, false, true);
        TexturedModel staticModel = new TexturedModel(model, texture);
        List<Entity> allCubes = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i< 400; i++) {
            float x = random.nextFloat() * 600;
            float z = random.nextFloat() * 600;
            allCubes.add(new Entity(staticModel, new Vector3f(x, 0, z), 0, 0, 0f, 1f));
        }

        RawModel model1 = OBJLoader.loadOBJ(new ResourcePath("fern"), loader);
        ModelTexture texture1 = new ModelTexture(loader.loadTexture(new ResourcePath("fern1")), 10, 0, true, true);
        TexturedModel staticModel1 = new TexturedModel(model1, texture1);
        for (int i = 0; i< 400; i++) {
            float x = random.nextFloat() * 600;
            float z = random.nextFloat() * 600;
            allCubes.add(new Entity(staticModel1, new Vector3f(x, 0, z), 0, 0, 0f, 1f));
        }

        RawModel model2 = OBJLoader.loadOBJ(new ResourcePath("grassModel"), loader);
        ModelTexture texture2 = new ModelTexture(loader.loadTexture(new ResourcePath("grassTexture")), 10, 0, true, true);
        TexturedModel staticModel2 = new TexturedModel(model2, texture2);
        for (int i = 0; i< 400; i++) {
            float x = random.nextFloat() * 600;
            float z = random.nextFloat() * 600;
            allCubes.add(new Entity(staticModel2, new Vector3f(x, 0, z), 0, 0, 0f, 1f));
        }

        Light light = new Light(new Vector3f(400, 400, 100), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture(new ResourcePath("grass")), 1, 0, false, true));
        Terrain terrain1 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture(new ResourcePath("grass")), 1, 0, false, true));

        camera = new Camera();

        renderer = new MasterRenderer();
        while(!GLFW.glfwWindowShouldClose(Window.window)) {
            //game logic
            for (Entity cube : allCubes) {
                renderer.processEntity(cube);
            }
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain1);
            renderer.render(light, camera);
            Window.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        Window.closeDisplay();

    }

}
