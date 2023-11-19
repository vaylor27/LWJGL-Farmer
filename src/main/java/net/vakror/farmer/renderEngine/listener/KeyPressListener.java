package net.vakror.farmer.renderEngine.listener;

public interface KeyPressListener extends Listener {
    void onPress(long window, int key, int scancode, int action, int mods);
}
