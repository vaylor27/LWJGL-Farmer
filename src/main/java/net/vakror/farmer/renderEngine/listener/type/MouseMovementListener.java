package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.renderEngine.listener.Listener;

public interface MouseMovementListener extends Listener {
    void onMouseMove(long window, double x, double y);
}
