package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.font.render.TextMaster;
import net.vakror.farmer.renderEngine.listener.CloseGameListener;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;

import static net.vakror.farmer.FarmerGameMain.*;

public class CleanUp implements CloseGameListener {
    @Override
    public void onGameClose() {
        fbos.values().forEach(WaterFrameBuffers::cleanUp);
        renderer.cleanUp();
        loader.cleanUp();
        TextMaster.cleanUp();

        Options.save();
    }
}
