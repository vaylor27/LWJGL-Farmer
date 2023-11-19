package net.vakror.farmer.renderEngine.gui;

import org.joml.Vector2f;

public abstract class GuiTexture {
    private final int texture;
    private final Vector2f pos;
    private final Vector2f scale;
    boolean isHovered = false;

    public GuiTexture(int texture, Vector2f pos, Vector2f scale) {
        this.texture = texture;
        this.pos = pos;
        this.scale = scale;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
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

    public abstract void onClick(int button, int action);

    public void unHover() {
        isHovered = false;
    }
}
