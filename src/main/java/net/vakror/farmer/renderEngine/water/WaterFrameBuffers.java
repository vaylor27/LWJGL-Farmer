package net.vakror.farmer.renderEngine.water;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.renderEngine.listener.Listener;
import net.vakror.farmer.renderEngine.listener.register.AutoRegisterComplexListener;
import net.vakror.farmer.renderEngine.listener.register.ListenerProvider;
import net.vakror.farmer.renderEngine.listener.type.CloseGameListener;
import net.vakror.farmer.renderEngine.listener.type.WindowResizeListener;
import net.vakror.farmer.renderEngine.util.InputUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glGetIntegerv;

@AutoRegisterComplexListener
public class WaterFrameBuffers implements WindowResizeListener, CloseGameListener, ListenerProvider {

	private int reflectionFrameBuffer;
	private int reflectionTexture;
	private int reflectionDepthBuffer;
	
	private int refractionFrameBuffer;
	private int refractionTexture;
	private int refractionDepthTexture;

	public WaterFrameBuffers() { //call when loading the game; argument is only here to make java happy
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}

	public WaterFrameBuffers(float height) { //call when loading the game; argument is only here to make java happy
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}

	@Override
	public void onWindowResize(int width, int height) {
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}

	public void onGameClose() { //call when closing the game
		GL30.glDeleteFramebuffers(reflectionFrameBuffer);
		GL11.glDeleteTextures(reflectionTexture);
		GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
		GL30.glDeleteFramebuffers(refractionFrameBuffer);
		GL11.glDeleteTextures(refractionTexture);
		GL11.glDeleteTextures(refractionDepthTexture);
	}

	public void bindReflectionFrameBuffer() {//call before rendering to this FBO
		bindFrameBuffer(reflectionFrameBuffer, InputUtil.getWindowWidth() / 4, InputUtil.getWindowHeight() / 4);
	}
	
	public void bindRefractionFrameBuffer() {//call before rendering to this FBO
		bindFrameBuffer(refractionFrameBuffer, InputUtil.getWindowWidth(), InputUtil.getWindowHeight());
	}
	
	public static void unbindCurrentFrameBuffer() {//call to switch to default frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		int[] vp = new int[4];
		glGetIntegerv(GL_VIEWPORT, vp);
		GL11.glViewport(vp[0], vp[1], vp[2], vp[3]);
	}

	public int getReflectionTexture() {//get the resulting texture
		return reflectionTexture;
	}
	
	public int getRefractionTexture() {//get the resulting texture
		return refractionTexture;
	}
	
	public int getRefractionDepthTexture(){//get the resulting depth texture
		return refractionDepthTexture;
	}

	private void initialiseReflectionFrameBuffer() {
		reflectionFrameBuffer = createFrameBuffer();
		reflectionTexture = createTextureAttachment(InputUtil.getWindowWidth() / 4, InputUtil.getWindowHeight() / 4);
		reflectionDepthBuffer = createDepthBufferAttachment(InputUtil.getWindowWidth() / 4, InputUtil.getWindowHeight() / 4);
		unbindCurrentFrameBuffer();
	}
	
	private void initialiseRefractionFrameBuffer() {
		int screenWidth = InputUtil.getWindowWidth();
		int screenHeight = InputUtil.getWindowHeight();
		refractionFrameBuffer = createFrameBuffer();
		refractionTexture = createTextureAttachment(screenWidth, screenHeight);
		refractionDepthTexture = createDepthTextureAttachment(screenWidth, screenHeight);
		unbindCurrentFrameBuffer();
	}
	
	private void bindFrameBuffer(int frameBuffer, int width, int height){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); //To make sure the texture isn't bound
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		//generate name for frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		//create the framebuffer
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		//indicate that we will always render to color attachment 0
		return frameBuffer;
	}

	private int createTextureAttachment(int width, int height) {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
				0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
				texture, 0);
		return texture;
	}
	
	private int createDepthTextureAttachment(int width, int height){
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,
				0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
				texture, 0);
		return texture;
	}

	private int createDepthBufferAttachment(int width, int height) {
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
				height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
				GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}

	@Override
	public Listener getListener() {
		return null;
	}

	@Override
	public Set<Listener> getListeners() {
		return new HashSet<>(FarmerGameMain.fbos.values());
	}
}
