package net.vakror.farmer.register.keybind;

import net.vakror.farmer.renderEngine.util.InputUtil;

import static net.vakror.farmer.renderEngine.util.InputUtil.isCursorDisabled;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

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
