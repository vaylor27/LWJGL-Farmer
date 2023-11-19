package net.vakror.farmer.renderEngine.gui;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.camera.CameraData;
import net.vakror.farmer.renderEngine.listeners.CorrectCameraOnCapture;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import org.joml.Vector2f;

public class ResetPositionGui extends GuiTexture {
    private final int hoveredTexture;

    public ResetPositionGui(int texture, int hoveredTexture, Vector2f pos, Vector2f scale) {
        super(texture, pos, scale);
        this.hoveredTexture = hoveredTexture;
    }

    @Override
    public void onClick(int button, int action) {
        FarmerGameMain.camera.nextCameraFocusPoint = new Vector2f(Terrain.SIZE / 2, Terrain.SIZE / 2);
        FarmerGameMain.camera.distanceToNextFocus = 0;
        FarmerGameMain.camera.alpha = 1;
        CorrectCameraOnCapture.lastCameraData = new CameraData(FarmerGameMain.camera);
    }

    @Override
    public int texture() {
        if (isHovered()) {
            return hoveredTexture;
        }
        return super.texture();
    }
}
