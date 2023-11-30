package net.vakror.farmer.renderEngine.listener.register;

import net.vakror.farmer.renderEngine.listener.Listener;

import java.util.Set;

// ALL INSTANCES MUST HAVE ONE EMPTY CONSTRUCTOR THAT INSTANTIATES THE CLASS EMPTY SO IT CAN RETURN THE ACTUAL INSTANCE
public interface ListenerProvider {
    Listener getListener();
    default Set<Listener> getListeners() {
        return null;
    }
}
