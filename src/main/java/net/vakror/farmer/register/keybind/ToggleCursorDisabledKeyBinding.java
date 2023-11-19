package net.vakror.farmer.register.keybind;

import net.vakror.farmer.renderEngine.mouse.InputUtil;

import static net.vakror.farmer.renderEngine.mouse.InputUtil.isCursorDisabled;
import static org.lwjgl.glfw.GLFW.*;

public class ToggleCursorDisabledKeyBinding extends KeyBinding {
    public ToggleCursorDisabledKeyBinding(int key) {
        super(key);
    }

    @Override
    public void execute(int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (!isCursorDisabled) {
                InputUtil.disableCursor();
            } else {
                InputUtil.enableCursor();
            }
        }
    }
}
