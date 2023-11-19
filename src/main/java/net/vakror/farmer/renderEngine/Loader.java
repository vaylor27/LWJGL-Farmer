package net.vakror.farmer.renderEngine;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.model.RawModel;
import net.vakror.farmer.renderEngine.texture.TextureData;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Loader {
	
	private final List<Integer> vaos = new ArrayList<Integer>();
	private final List<Integer> vbos = new ArrayList<Integer>();;
	private final List<Integer> textures = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals,int[] indices){
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}


	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}

	public int loadCubeMap(String... textureFiles) {
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texId);

		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile(new ResourcePath(textureFiles[i]));
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		textures.add(texId);
		return texId;
	}

	private TextureData decodeTextureFile(ResourcePath fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName.getImagePath());
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}

	public int loadTexture(ResourcePath filePath) {
		int textureId;
		int width, height;
		ByteBuffer image;

		String fullPath = filePath.getImagePath();

		try	(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			image = stbi_load(fullPath, w, h, comp, 4);
			if (image == null) {
				System.err.println("Could not load texture file: " + fullPath + "! Reason: " + stbi_failure_reason());
			}
			width = w.get();
			height = h.get();
		}

		textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		textures.add(textureId);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // sets MINIFICATION filtering to nearest
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // sets MAGNIFICATION filtering to nearest
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

		GL30.glGenerateMipmap(GL_TEXTURE_2D);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, Options.mipmap());
		return textureId;
	}
	
	public void cleanUp(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture: textures) {
			GL11.glDeleteTextures(texture);
		}
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
