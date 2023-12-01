package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.renderEngine.listener.Listener;

public interface WindowFocusListener extends Listener {
    void onWindowFocus();
    void onWindowUnFocus();
}
