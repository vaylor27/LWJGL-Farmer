package net.vakror.farmer.renderEngine.util;

import net.vakror.farmer.renderEngine.gui.GuiTexture;
import org.joml.Vector2f;

import static net.vakror.farmer.renderEngine.util.CoordinateUtil.screenPercentToNDC;

public class GuiHelper {
    public static boolean isWithin(GuiTexture gui, Vector2f coordinate) {
        Vector2f guiDimensions = new Vector2f(gui.scale().x * 2, gui.scale().y * 2);
        return isMouseOver(gui.pos(), coordinate, guiDimensions.x, guiDimensions.y);
    }


    public static boolean isMouseOver(Vector2f ndcGuiPos, Vector2f ndcMouseCoords, float ndcWidth, float ndcHeight) {
        float guiXMin = ndcGuiPos.x - (ndcWidth / 2);
        float guiXMax = ndcGuiPos.x + (ndcWidth / 2);
        float guiYMax = ndcGuiPos.y + (ndcHeight / 2);
        float guiYMin = ndcGuiPos.y - (ndcHeight / 2);

        float mouseX = ndcMouseCoords.x;
        float mouseY = ndcMouseCoords.y;

        return mouseX >= guiXMin && mouseX <= guiXMax && mouseY >= guiYMin && mouseY <= guiYMax;
    }


}
