package net.vakror.farmer.renderEngine.gui;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.listener.register.AutoRegisterComplexListener;
import net.vakror.farmer.renderEngine.listener.Listener;
import net.vakror.farmer.renderEngine.listener.register.ListenerProvider;

import java.util.HashSet;
import java.util.Set;

@AutoRegisterComplexListener
public class GuiListenerProvider implements ListenerProvider {
    @Override
    public Set<Listener> getListeners() {
        return new HashSet<>(FarmerGameMain.guis);
    }

    @Override
    public Listener getListener() {
        return null;
    }
}
