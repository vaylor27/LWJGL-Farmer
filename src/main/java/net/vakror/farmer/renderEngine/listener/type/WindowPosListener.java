package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.renderEngine.listener.Listener;

public interface WindowPosListener extends Listener {
    void onWindowMove(int x, int y);
}
