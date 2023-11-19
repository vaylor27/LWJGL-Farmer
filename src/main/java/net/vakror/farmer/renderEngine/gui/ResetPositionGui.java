package net.vakror.farmer.renderEngine.gui;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.camera.CameraData;
import net.vakror.farmer.renderEngine.listeners.CorrectCameraOnCapture;
import org.joml.Vector2f;

public class ResetPositionGui extends GuiTexture {
    private final int hoveredTexture;

    public ResetPositionGui(int texture, int hoveredTexture, Vector2f pos, Vector2f scale) {
        super(texture, pos, scale);
        this.hoveredTexture = hoveredTexture;
    }

    @Override
    public void onClick(int button, int action) {
        FarmerGameMain.camera.data.angleAroundFocused = 0;
        FarmerGameMain.camera.data.pitch = 20;
        FarmerGameMain.camera.currentCameraFocusPoint = new Vector2f(500, 500);
        CorrectCameraOnCapture.lastCameraData = new CameraData(FarmerGameMain.camera);
        System.out.print("click!\n");
    }

    @Override
    public int texture() {
        if (isHovered()) {
            return hoveredTexture;
        }
        return super.texture();
    }
}
