package net.vakror.farmer.renderEngine.terrain;

import net.vakror.farmer.renderEngine.Loader;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {
    private static final float SIZE = 1000;
    private static final int MAX_HEIGHT = 40;
    private static final int MAX_PIXEL_COLOR = 256 * 256 * 256;


    public float x;
    public float z;
    private RawModel model;
    private ModelTexture texture;

    private float[][] heights;

    public Terrain(int gridX, int gridZ, Loader loader, ModelTexture texture, ResourcePath heightMap) {
        this.texture = texture;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / (heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float height;
        Vector3f vec = new Vector3f(0, heights[gridX][gridZ], 0);
        Vector3f vec1 = new Vector3f(1, heights[gridX + 1][gridZ], 0);
        Vector3f vec2 = new Vector3f(0, heights[gridX][gridZ + 1], 1);
        Vector3f vec3 = new Vector3f(1, heights[gridX + 1][gridZ + 1], 1);
        Vector2f coordinateVector = new Vector2f(xCoord, zCoord);
        if (xCoord <= (1 - zCoord)) {
            height = Mth.barryCentricFunction(vec, vec1, vec2, coordinateVector);
        } else {
            height = Mth.barryCentricFunction(vec1, vec3, vec2, coordinateVector);
        }
        return height;
    }

    private RawModel generateTerrain(Loader loader, ResourcePath heightMap) {

        BufferedImage image;
        try {
            image = ImageIO.read(new File(heightMap.getImagePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int VERTEX_COUNT = image.getHeight();

        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j, i, image);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        float heightL = getHeight(x - 1, z, image);
        float heightD = getHeight(x + 1, z, image);
        float heightR = getHeight(x, z - 1, image);
        float heightU = getHeight(x, z + 1, image);

        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    private float getHeight(int x, int y, BufferedImage image) {
        if (x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight()) {
            return 0;
        }
        return ((image.getRGB(x, y) + (MAX_PIXEL_COLOR / 2f)) / (MAX_PIXEL_COLOR / 2f)) * MAX_HEIGHT;
    }
}
