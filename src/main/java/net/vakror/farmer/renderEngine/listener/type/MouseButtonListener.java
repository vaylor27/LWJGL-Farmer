package net.vakror.farmer.renderEngine.listener.type;

import net.vakror.farmer.renderEngine.listener.Listener;

public interface MouseButtonListener extends Listener {
    void onClick(long window, int button, int action);
}
