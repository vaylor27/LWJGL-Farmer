package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.renderEngine.listener.*;
import net.vakror.farmer.renderEngine.water.WaterFrameBuffers;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import java.util.List;

import static net.vakror.farmer.FarmerGameMain.*;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

public class ReflectionRefractionNormalSceneFrameBufferTextureRenderer implements RenderListener {

    @Override
    public void onRender() {
        GL11.glEnable(GL_CLIP_DISTANCE0);
        fbos.forEach((height, fbo) -> {
            renderReflection(height, fbo);
            renderRefraction(height, fbo);
            renderNormal();
        });
    }

    private static void renderReflection(Float height, WaterFrameBuffers fbo) {
            fbo.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - height);
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, List.of(terrain), lights, camera, new Vector4f(0, 1, 0, -height + 1f), false);
            camera.getPosition().y += distance;
            camera.invertPitch();
    }

    private static void renderRefraction(Float height, WaterFrameBuffers fbo) {
        fbo.bindRefractionFrameBuffer();
        renderer.renderScene(entities, List.of(terrain), lights, camera, new Vector4f(0, -1, 0, height + 1f), false);
    }

    private static void renderNormal() {
        GL11.glDisable(GL_CLIP_DISTANCE0);
        WaterFrameBuffers.unbindCurrentFrameBuffer();
        renderer.renderScene(entities, List.of(terrain), lights, camera, new Vector4f(0, -1, 0, 100000), true);
    }
}
