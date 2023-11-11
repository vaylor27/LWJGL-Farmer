package net.vakror.farmer.renderEngine;


import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;


import static java.lang.System.currentTimeMillis;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    public static long window;
    private static String name = "Farmer Game";
    private static String version = "0.0.1";
    private static String status = "ALPHA";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    private static long lastFrameTime;
    private static float delta;

    public static void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, name + " V" + version + " " + status, NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, Window::executeKey);



        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        // Enable v-sync
//        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        lastFrameTime = getCurrentTime();
    }


    private static void executeKey(long window, int key, int scancode, int action, int mods) {
        DefaultRegistries.KEYBINDINGS.forEach((keyBinding -> {
            if (keyBinding.key == key) {
                keyBinding.execute(scancode, action, mods);
            }
        }));
    }

    public static void closeDisplay() {
        glfwDestroyWindow(window);
    }


    public static void updateDisplay() {
        glfwSwapBuffers(window); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();

        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    private static long getCurrentTime() {
        // return Sys.getTime()*1000/Sys.getTimerResolution();
        return currentTimeMillis();
    }
}
