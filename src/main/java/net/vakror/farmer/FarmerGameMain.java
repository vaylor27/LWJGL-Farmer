package net.vakror.farmer;

import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
import net.vakror.farmer.renderEngine.shader.StaticShader;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import org.lwjgl.glfw.GLFW;

public class FarmerGameMain {

    public static void main(String[] args) {

        Window window = new Window();
        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();

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

        RawModel model = loader.loadToVAO(vertices,indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("test"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        while(!GLFW.glfwWindowShouldClose(window.window)){
            //game logic
            renderer.prepare();
            shader.start();
            renderer.render(texturedModel);
            shader.stop();
            window.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        window.closeDisplay();

    }

}
