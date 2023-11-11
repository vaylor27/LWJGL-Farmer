package net.vakror.farmer;

import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.MasterRenderer;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.entity.Player;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.OBJLoader;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DevTesting {
    public static Light light;
    public static Terrain terrain;
    public static Terrain terrain1;
    public static List<Entity> allCubes = new ArrayList<>();

    public static void runTest(Loader loader) {
        RawModel model = OBJLoader.loadOBJ(new ResourcePath("tree"), loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(new ResourcePath("tree")), 10, 0, false, true);
        TexturedModel staticModel = new TexturedModel(model, texture);
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

        RawModel bunnyModel = OBJLoader.loadOBJ(new ResourcePath("stanfordBunny"), loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture(new ResourcePath("white"))));

        FarmerGameMain.player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);

        light = new Light(new Vector3f(400, 400, 100), new Vector3f(1, 1, 1));

        terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture(new ResourcePath("grass")), 1, 0, false, true));
        terrain1 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture(new ResourcePath("grass")), 1, 0, false, true));

        FarmerGameMain.camera = new Camera();


        FarmerGameMain.renderer = new MasterRenderer();
    }

    public static void tick() {
        FarmerGameMain.player.move();
        //game logic
        FarmerGameMain.renderer.processEntity(FarmerGameMain.player);
        for (Entity cube : allCubes) {
            FarmerGameMain.renderer.processEntity(cube);
        }
        FarmerGameMain.renderer.processTerrain(terrain);
        FarmerGameMain.renderer.processTerrain(terrain1);
        FarmerGameMain.renderer.render(light, FarmerGameMain.camera);
        Window.updateDisplay();
    }
}
