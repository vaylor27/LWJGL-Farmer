package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.renderEngine.listener.Listener;

public interface MouseCapturedListener extends Listener {
    void onCaptured();
    void onReleased();
}
