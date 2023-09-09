package net.vakror.farmer;

import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
import net.vakror.farmer.renderEngine.shader.StaticShader;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class FarmerGameMain {
    public static Camera camera = null;

    public static void main(String[] args) {
        Window.init();
        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        float[] vertices = {
                -0.5f,0.5f,0,	//V0
                -0.5f,-0.5f,0,	//V1
                0.5f,-0.5f,0,	//V2
                0.5f,0.5f,0		//V3
        };

        int[] indices = {
                0,1,3,	//Top left triangle (V0,V1,V3)
                3,1,2	//Bottom right triangle (V3,V1,V2)
        };

        float[] textureCoords = {
                0,0, //V0
                0,1, //V1
                1,1, //V2
                1,0 //V3
        };

        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("test"));
        TexturedModel staticModel = new TexturedModel(model, texture);
        Entity entity = new Entity(staticModel, new Vector3f(0, 0, -1), 0, 0, 0, 1);

        camera = new Camera();

        while(!GLFW.glfwWindowShouldClose(Window.window)) {
//            entity.increasePosition(new Vector3f(0, 0, -0.1f));
            //game logic
            renderer.prepare();
            shader.start();
            shader.loadViewMatrix(camera);
            renderer.render(entity, shader);
            shader.stop();
            Window.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        Window.closeDisplay();

    }

}
