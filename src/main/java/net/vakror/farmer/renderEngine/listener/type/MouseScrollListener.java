package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.renderEngine.listener.Listener;

public interface MouseScrollListener extends Listener {
    void onScroll(long window, double x, double y);
}
