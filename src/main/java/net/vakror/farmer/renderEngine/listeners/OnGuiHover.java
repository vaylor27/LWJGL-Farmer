package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.DevTesting;
import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.listener.MouseMovementListener;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import net.vakror.farmer.renderEngine.util.GuiHelper;
import org.joml.Vector2f;

import static net.vakror.farmer.renderEngine.mouse.InputUtil.currentMousePos;

public class OnGuiHover implements MouseMovementListener {
    @Override
    public void onMouseMove(long window, double x, double y) {
        if (!InputUtil.isCursorDisabled) {
            for (GuiTexture gui : DevTesting.guis) {
                Vector2f ndcMouseCoords = currentMousePos.getAsNDC();

                gui.setHovered(GuiHelper.isWithin(gui, ndcMouseCoords));
            }
        }
    }
}
