package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.listener.MouseButtonListener;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import net.vakror.farmer.renderEngine.terrain.Terrain;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class OnTerrainClick implements MouseButtonListener {
    @Override
    public void onClick(long window, int button, int action) {
        if (!InputUtil.isHoveringOverGui && !InputUtil.isCursorDisabled) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
                Vector3f terrainPoint = FarmerGameMain.picker.getCurrentTerrainPoint();
                if (terrainPoint != null && terrainPoint.x > 0 && terrainPoint.x < Terrain.SIZE && terrainPoint.z > 0 && terrainPoint.z < Terrain.SIZE) {
                    FarmerGameMain.camera.nextCameraFocusPoint = new Vector2f(terrainPoint.x, terrainPoint.z);
                    FarmerGameMain.camera.distanceToNextFocus = 0;
                    FarmerGameMain.camera.alpha = 1;
                }
            }
        }
    }
}
