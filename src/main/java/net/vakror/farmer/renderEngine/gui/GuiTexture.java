package net.vakror.farmer.renderEngine.gui;

import org.joml.Vector2f;

public class GuiTexture {
    private int texture;
    private Vector2f pos;
    private Vector2f scale;

    public GuiTexture(int texture, Vector2f pos, Vector2f scale) {
        this.texture = texture;
        this.pos = pos;
        this.scale = scale;
    }

    public int texture() {
        return texture;
    }

    public Vector2f pos() {
        return pos;
    }

    public Vector2f scale() {
        return scale;
    }
}
