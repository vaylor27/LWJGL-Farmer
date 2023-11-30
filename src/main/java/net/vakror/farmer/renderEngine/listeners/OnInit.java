package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.GameEntryPoint;
import net.vakror.farmer.Priority;
import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.listener.*;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.OBJLoader;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;
import net.vakror.farmer.renderEngine.water.WaterTile;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.Random;

import static net.vakror.farmer.FarmerGameMain.*;

public class OnInit implements GameEntryPoint {
    public void initialize() {
        DefaultRegistries.registerDefaults();
        setupObjects();
        setupLight();
        setupListeners();
    }

    private void setupWater() {
        addWater(new WaterTile(Terrain.SIZE / 2, Terrain.SIZE / 2, -2f));
    }

    public static void addWater(WaterTile tile) {
        waterTiles.add(tile);
        fbos.computeIfAbsent(tile.getHeight(), WaterFrameBuffers::new);
    }

    private void setupObjects() {
        RawModel model = OBJLoader.loadOBJ(new ResourcePath("tree"));
        ModelTexture texture = new ModelTexture(Loader.loadTexture(new ResourcePath("tree")), 1, 0, false, true);
        TexturedModel staticModel = new TexturedModel(model, texture);

        RawModel rawFern = OBJLoader.loadOBJ(new ResourcePath("fern"));
        ModelTexture fernTexture = new ModelTexture(Loader.loadTexture(new ResourcePath("fern1Atlas")), 1, 0, true, true);
        fernTexture.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(rawFern, fernTexture);

        RawModel rawGrass = OBJLoader.loadOBJ(new ResourcePath("grassModel"));
        ModelTexture grassTexture = new ModelTexture(Loader.loadTexture(new ResourcePath("grassTextureAtlas")), 1, 0, true, true);
        grassTexture.setNumberOfRows(2);
        TexturedModel grass = new TexturedModel(rawGrass, grassTexture);

        Random random = new Random(676452);
        for (int i = 0; i< 400; i++) {
            if (i % 20 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * +300;
                Vector2i coord = Terrain.getGridCoord(x, z);
                float y = terrains[coord.x][coord.y].getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
            } if (i % 5 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * +300;
                Vector2i coord = Terrain.getGridCoord(x, z);
                float y = terrains[coord.x][coord.y].getHeightOfTerrain(x, z);
                entities.add(new Entity(grass, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));

                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * +300;
                Vector2i coord1 = Terrain.getGridCoord(x, z);
                y = terrains[coord.x][coord.y].getHeightOfTerrain(x, z);
                entities.add(new Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
            }
        }
    }

    public static void setupListeners() {
        guis.forEach(Listeners::addListener);
    }

    public static void setupLight() {
        lights.add(new Light(new Vector3f(0, 1000, -1000), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0))); // sun
        lights.add(new Light(new Vector3f(185, 10, 293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(370, 17, 300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(293, 7, 305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
    }

    @Override
    public Priority getPriority() {
        return Priority.LOWEST;
    }
}
