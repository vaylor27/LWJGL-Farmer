package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.listener.CloseGameListener;

import static net.vakror.farmer.FarmerGameMain.*;

public class CleanUp implements CloseGameListener {
    @Override
    public void onGameClose() {
        fbos.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();

        Options.save();
    }
}
