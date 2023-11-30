package net.vakror.farmer.renderEngine.gui;

import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.camera.CameraData;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import org.joml.Vector2f;

public class ResetPositionGui extends GuiTexture {
    private final int hoveredTexture;

    public ResetPositionGui(int texture, int hoveredTexture, Vector2f pos, Vector2f scale) {
        super(texture, pos, scale);
        this.hoveredTexture = hoveredTexture;
    }

    @Override
    public void onGuiClicked(int button, int action) {
        Camera.nextCameraFocusPoint = new Vector2f(Terrain.SIZE / 2, Terrain.SIZE / 2);
        Camera.distanceToNextFocus = 0;
        Camera.alpha = 1;
        Camera.lastCameraData = new CameraData();
    }

    @Override
    public int texture() {
        if (isHovered()) {
            return hoveredTexture;
        }
        return super.texture();
    }
}
