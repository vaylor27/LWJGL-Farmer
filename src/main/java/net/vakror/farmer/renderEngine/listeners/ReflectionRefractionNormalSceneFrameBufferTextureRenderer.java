package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.renderEngine.listener.*;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.List;

import static net.vakror.farmer.FarmerGameMain.*;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

public class ReflectionRefractionNormalSceneFrameBufferTextureRenderer implements RenderListener {

    @Override
    public void onRender() {
        GL11.glEnable(GL_CLIP_DISTANCE0);
        renderReflection();
        renderRefraction();
        renderNormal();
    }

    private static void renderReflection() {
        fbos.bindReflectionFrameBuffer();
        float distance = 2 * (camera.getPosition().y - waterTiles.get(0).getHeight());
        camera.getPosition().y -= distance;
        camera.invertPitch();
        renderer.renderScene(entities, List.of(terrain), lights, camera, new Vector4f(0, 1, 0, -waterTiles.get(0).getHeight() + 1f), false);
        camera.getPosition().y += distance;
        camera.invertPitch();
    }

    private static void renderRefraction() {
        fbos.bindRefractionFrameBuffer();
        renderer.renderScene(entities, List.of(terrain), lights, camera, new Vector4f(0, -1, 0, waterTiles.get(0).getHeight() + 1f), false);
    }

    private static void renderNormal() {
        GL11.glDisable(GL_CLIP_DISTANCE0);
        fbos.unbindCurrentFrameBuffer();
        renderer.renderScene(entities, List.of(terrain), lights, camera, new Vector4f(0, -1, 0, 100000), true);
    }
}
