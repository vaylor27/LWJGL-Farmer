package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.renderEngine.font.render.TextMaster;
import net.vakror.farmer.renderEngine.listener.RenderListener;

public class RenderText implements RenderListener {
    @Override
    public void onRender() {
        TextMaster.render();
    }
}
