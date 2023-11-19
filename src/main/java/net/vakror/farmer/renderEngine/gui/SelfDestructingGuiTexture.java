package net.vakror.farmer.renderEngine.gui;

import net.vakror.farmer.DevTesting;
import org.joml.Vector2f;

public class SelfDestructingGuiTexture extends GuiTexture{

    public SelfDestructingGuiTexture(int texture, Vector2f pos, Vector2f scale) {
        super(texture, pos, scale);
    }

    @Override
    public void onClick(int button, int action) {
        DevTesting.guis.remove(this);
    }
}
