package net.vakror.farmer;

import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Entity;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.model.TexturedModel;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.*;
import net.vakror.farmer.renderEngine.shader.PerPixelStaticShader;
import net.vakror.farmer.renderEngine.shader.SpecularStaticShader;
import net.vakror.farmer.renderEngine.texture.ModelTexture;
import net.vakror.farmer.renderEngine.util.OBJLoader;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class FarmerGameMain {
    public static Camera camera = null;
    public static Options options = new Options(true);

    public static void main(String[] args) {
        Window.init();
        Loader loader = new Loader();
        PerPixelStaticShader shader = new PerPixelStaticShader();
        if (options.useSpecularLighting) {
            shader = new SpecularStaticShader();
        }
        Renderer renderer = new Renderer(shader);

        RawModel model = OBJLoader.loadOBJ(new ResourcePath("dragon"), loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture(new ResourcePath("pink")));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        TexturedModel staticModel = new TexturedModel(model, texture);
        Entity entity = new Entity(staticModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 1, 1));

        camera = new Camera();

        while(!GLFW.glfwWindowShouldClose(Window.window)) {
            entity.increaseRotation(new Vector3f(0, 1, 0));
            //game logic
            renderer.prepare();
            shader.start();
            shader.loadViewMatrix(camera);
            shader.loadLight(light);
            renderer.render(entity, shader);
            shader.stop();
            Window.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        Window.closeDisplay();

    }

}
