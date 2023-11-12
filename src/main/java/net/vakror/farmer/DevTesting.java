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
    public static List<Entity> entities = new ArrayList<>();

    public static void runTest(Loader loader) {


        RawModel bunnyModel = OBJLoader.loadOBJ(new ResourcePath("stanfordBunny"), loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture(new ResourcePath("white"))));

        FarmerGameMain.player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);

        light = new Light(new Vector3f(400, 400, 100), new Vector3f(1, 1, 1));

        terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture(new ResourcePath("grass")), 1, 0, false, false), new ResourcePath("heightmap"));

        generateObjects(loader);
        FarmerGameMain.camera = new Camera(FarmerGameMain.player);


        FarmerGameMain.renderer = new MasterRenderer();
    }

    public static void tick() {
        FarmerGameMain.player.move(terrain);
        FarmerGameMain.camera.tick();
        //game logic
        FarmerGameMain.renderer.processEntity(FarmerGameMain.player);
        for (Entity cube : entities) {
            FarmerGameMain.renderer.processEntity(cube);
        }
        FarmerGameMain.renderer.processTerrain(terrain);
        FarmerGameMain.renderer.render(light, FarmerGameMain.camera);
        Window.updateDisplay();
    }

    public static void generateObjects(Loader loader) {
        entities.clear();
        RawModel model = OBJLoader.loadOBJ(new ResourcePath("tree"), loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(new ResourcePath("tree")), 10, 0, false, true);
        TexturedModel staticModel = new TexturedModel(model, texture);

        RawModel rawFern = OBJLoader.loadOBJ(new ResourcePath("fern"), loader);
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture(new ResourcePath("fern1")), 10, 0, true, true);
        TexturedModel fern = new TexturedModel(rawFern, fernTexture);

        RawModel rawGrass = OBJLoader.loadOBJ(new ResourcePath("grassModel"), loader);
        ModelTexture grassTexture = new ModelTexture(loader.loadTexture(new ResourcePath("grassTexture")), 10, 0, true, true);
        TexturedModel grass = new TexturedModel(rawGrass, grassTexture);

        Random random = new Random(676452);
        for (int i = 0; i< 400; i++) {
            if (i % 20 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * +300;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
            } if (i % 5 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * +300;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grass, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));

                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * +300;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
            }
        }
    }
}
