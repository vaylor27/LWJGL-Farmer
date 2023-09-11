package net.vakror.farmer.renderEngine;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.shader.ShaderProgram;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public abstract class AbstractRenderer {
    public ShaderProgram shader;

    void prepareRenderable(RawModel model, ModelTexture texture) {
        GL30.glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        if (texture.hasTransparency()) {
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLighting(texture.useFakeLight());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        shader.loadFog(FarmerGameMain.options.fogDensity, FarmerGameMain.options.fogGradient);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
    }

    void draw(int vertexCount) {
        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
    }

    void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}