package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.listener.MouseCapturedListener;
import net.vakror.farmer.renderEngine.camera.CameraData;
import net.vakror.farmer.renderEngine.mouse.InputUtil;

import static net.vakror.farmer.renderEngine.camera.Camera.lastValueX;
import static net.vakror.farmer.renderEngine.camera.Camera.lastValueY;
import static net.vakror.farmer.renderEngine.mouse.InputUtil.currentMousePos;

public class CorrectCameraOnCapture implements MouseCapturedListener {
    public static CameraData lastCameraData;

    @Override
    public void onCaptured() {
        FarmerGameMain.camera.setData(lastCameraData);
        lastValueX = InputUtil.previousCapturedPos.getAsScreenCoords().x;
        lastValueY = InputUtil.previousCapturedPos.getAsScreenCoords().y;
        currentMousePos.setScreenCoordinates(InputUtil.previousCapturedPos.getAsScreenCoords().x, InputUtil.previousCapturedPos.getAsScreenCoords().y);
    }

    @Override
    public void onReleased() {
        lastCameraData = new CameraData(FarmerGameMain.camera);
    }
}
