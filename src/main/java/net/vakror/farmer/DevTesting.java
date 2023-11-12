package net.vakror.farmer;

import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.MasterRenderer;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.entity.Player;
import net.vakror.farmer.renderEngine.gui.GuiRenderer;
import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.OBJLoader;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DevTesting {
    public static Light light;
    public static Terrain terrain;
    public static List<Entity> entities = new ArrayList<>();
    public static List<GuiTexture> guis = new ArrayList<>();
    public static GuiRenderer guiRenderer;
    public static List<Light> lights = new ArrayList<>();

    public static void runTest(Loader loader) {


        RawModel bunnyModel = OBJLoader.loadOBJ(new ResourcePath("stanfordBunny"), loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture(new ResourcePath("white"))));

        FarmerGameMain.player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);

        lights.add(new Light(new Vector3f(0, 1000, -1000), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0)));
        lights.add(new Light(new Vector3f(185, 10, 293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(370, 17, 300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(293, 7, 305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));


        terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture(new ResourcePath("grass")), 1, 0, false, false), new ResourcePath("heightmap"));

        generateObjects(loader);
        FarmerGameMain.camera = new Camera(FarmerGameMain.player);

//        GuiTexture gui = new GuiTexture(loader.loadTexture(new ResourcePath("test")), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//        GuiTexture gui1 = new GuiTexture(loader.loadTexture(new ResourcePath("test")), new Vector2f(0.45f, 0.75f), new Vector2f(0.25f, 0.25f));
//        guis.add(gui);
//        guis.add(gui1);

        guiRenderer = new GuiRenderer(loader);

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
        FarmerGameMain.renderer.render(lights, FarmerGameMain.camera);
        guiRenderer.render(guis);
        Window.updateDisplay();
    }

    public static void generateObjects(Loader loader) {
        RawModel model = OBJLoader.loadOBJ(new ResourcePath("tree"), loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(new ResourcePath("tree")), 1, 0, false, true);
        TexturedModel staticModel = new TexturedModel(model, texture);

        RawModel rawFern = OBJLoader.loadOBJ(new ResourcePath("fern"), loader);
        ModelTexture fernTexture = new ModelTexture(loader.loadTexture(new ResourcePath("fern1Atlas")), 1, 0, true, true);
        fernTexture.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(rawFern, fernTexture);

        RawModel rawGrass = OBJLoader.loadOBJ(new ResourcePath("grassModel"), loader);
        ModelTexture grassTexture = new ModelTexture(loader.loadTexture(new ResourcePath("grassTextureAtlas")), 1, 0, true, true);
        grassTexture.setNumberOfRows(2);
        TexturedModel grass = new TexturedModel(rawGrass, grassTexture);

        Random random = new Random(676452);
        for (int i = 0; i< 400; i++) {
            if (i % 20 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * +300;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
            } if (i % 5 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * +300;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grass, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));

                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * +300;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
            }
        }
    }
}
