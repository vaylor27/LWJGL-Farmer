package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.listener.MouseMovementListener;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import net.vakror.farmer.renderEngine.util.GuiHelper;
import org.joml.Vector2f;

import static net.vakror.farmer.FarmerGameMain.guis;
import static net.vakror.farmer.renderEngine.mouse.InputUtil.currentMousePos;

public class OnGuiHover implements MouseMovementListener {
    @Override
    public void onMouseMove(long window, double x, double y) {
        if (!InputUtil.isCursorDisabled) {
            boolean isHoveringOverGui = false;
            for (GuiTexture gui : guis) {
                Vector2f ndcMouseCoords = currentMousePos.getAsNDC();
                if (GuiHelper.isWithin(gui, ndcMouseCoords)) {
                    isHoveringOverGui = true;
                    gui.setHovered(true);
                } else {
                    gui.setHovered(false);
                }
            }
            InputUtil.isHoveringOverGui = isHoveringOverGui;
        }
    }
}
