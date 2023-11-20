package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.listener.RenderListener;

public class TickMouseAndCamera implements RenderListener {
    @Override
    public void onRender() {
        FarmerGameMain.camera.tick();
        FarmerGameMain.picker.update();
    }
}
