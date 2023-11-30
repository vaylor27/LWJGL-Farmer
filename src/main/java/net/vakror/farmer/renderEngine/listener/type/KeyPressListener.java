package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.renderEngine.listener.Listener;

public interface KeyPressListener extends Listener {
    void onPress(long window, int key, int scancode, int action, int mods);
}
