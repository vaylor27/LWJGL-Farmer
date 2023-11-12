package net.vakror.farmer.renderEngine.entity;

import net.vakror.farmer.renderEngine.model.TexturedModel;
import org.joml.Vector3f;

public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    private int textureIndex = 0;

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public float getTextureXOffset() {
        int column = textureIndex % model.getTexture().numberOfRows();
        return (float) column/(float) model.getTexture().numberOfRows();
    }

    public float getTextureYOffset() {
        int row = textureIndex / model.getTexture().numberOfRows();
        return (float) row/(float) model.getTexture().numberOfRows();
    }

    public Entity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.textureIndex = index;
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(Vector3f vector) {
        this.position.add(vector);
    }

    public void increaseRotation(Vector3f vector) {
        this.rotX += vector.x;
        this.rotY += vector.y;
        this.rotZ += vector.z;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
