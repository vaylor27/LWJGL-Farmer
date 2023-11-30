package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.renderEngine.listener.Listener;

public interface WindowResizeListener extends Listener {
    void onWindowResize(int width, int height);
}
