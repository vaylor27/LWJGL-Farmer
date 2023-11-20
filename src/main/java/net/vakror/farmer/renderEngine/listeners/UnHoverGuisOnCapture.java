package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.listener.MouseCapturedListener;

import static net.vakror.farmer.FarmerGameMain.guis;

public class UnHoverGuisOnCapture implements MouseCapturedListener {
    @Override
    public void onCaptured() {
        guis.forEach(GuiTexture::unHover);
    }

    @Override
    public void onReleased() {

    }
}
