package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.DevTesting;
import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.listener.MouseButtonListener;
import net.vakror.farmer.renderEngine.mouse.InputUtil;

public class OnGuiClick implements MouseButtonListener {

    @Override
    public void onClick(long window, int button, int action) {
        if (!InputUtil.isCursorDisabled) {
            for (GuiTexture gui : DevTesting.guis) {
                if (gui.isHovered()) {
                    gui.onClick(button, action);
                }
            }
        }
    }
}