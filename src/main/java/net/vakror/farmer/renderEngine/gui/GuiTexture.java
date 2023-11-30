package net.vakror.farmer.renderEngine.gui;

import net.vakror.farmer.renderEngine.listener.register.AutoRegisterComplexListener;
import net.vakror.farmer.renderEngine.listener.type.MouseButtonListener;
import net.vakror.farmer.renderEngine.listener.type.MouseCapturedListener;
import net.vakror.farmer.renderEngine.listener.type.MouseMovementListener;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import net.vakror.farmer.renderEngine.util.GuiHelper;
import org.joml.Vector2f;

import static net.vakror.farmer.renderEngine.mouse.InputUtil.currentMousePos;

@AutoRegisterComplexListener
public abstract class GuiTexture implements MouseButtonListener, MouseMovementListener, MouseCapturedListener {
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

    public abstract void onGuiClicked(int button, int action);

    public void unHover() {
        isHovered = false;
    }

    @Override
    public void onClick(long window, int button, int action) {
        if (!InputUtil.isCursorDisabled) {
            if (isHovered()) {
                onGuiClicked(button, action);
            }
        }
    }

    @Override
    public void onMouseMove(long window, double x, double y) {
        if (!InputUtil.isCursorDisabled) {
            boolean isHoveringOverGui = false;
            Vector2f ndcMouseCoords = currentMousePos.getAsNDC();
            if (GuiHelper.isWithin(this, ndcMouseCoords)) {
                isHoveringOverGui = true;
                this.setHovered(true);
            } else {
                this.setHovered(false);
            }
            InputUtil.isHoveringOverGui = isHoveringOverGui;
        }
    }


    @Override
    public void onCaptured() {
        unHover();
    }

    @Override
    public void onReleased() {
    }
}