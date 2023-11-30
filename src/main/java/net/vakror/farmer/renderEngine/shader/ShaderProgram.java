package net.vakror.farmer.renderEngine.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static net.vakror.farmer.FarmerGameMain.LOGGER;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public abstract class ShaderProgram {
	
	private final int programID;
	private final int vertexShaderID;
	private final int fragmentShaderID;

	private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(ResourcePath vertexFile, ResourcePath fragmentFile){
		vertexShaderID = loadShader(vertexFile.getShaderPath(), GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile.getShaderPath(), GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
	}

	public int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	public void loadFloat(String uniformName, float value) {
		GL20.glUniform1f(getUniformLocation(uniformName), value);
	}

	public void loadInt(String uniformName, int value) {
		GL20.glUniform1i(getUniformLocation(uniformName), value);
	}

	public void loadVector4(String uniformName, Vector4f vector) {
		GL20.glUniform4f(getUniformLocation(uniformName), vector.x, vector.y, vector.z, vector.w);
	}

	public void loadVector3(String uniformName, Vector3f vector) {
		GL20.glUniform3f(getUniformLocation(uniformName), vector.x, vector.y, vector.z);
	}

	public void loadVector2(String uniformName, Vector2f vector) {
		GL20.glUniform2f(getUniformLocation(uniformName), vector.x, vector.y);
	}


	public void loadBoolean(String uniformName, boolean value) {
		float toLoad = value ? 1: 0;
		GL20.glUniform1f(getUniformLocation(uniformName), toLoad);
	}

	public void loadMatrix(String uniformName, Matrix4f matrix) {
		store(matrixBuffer, matrix);
		matrixBuffer.flip();
		glUniformMatrix4fv(getUniformLocation(uniformName), false, matrixBuffer);
	}

	public void store(FloatBuffer matrixBuffer, Matrix4f matrix) {
		matrixBuffer.clear();

		matrixBuffer.put(matrix.m00());
		matrixBuffer.put(matrix.m01());
		matrixBuffer.put(matrix.m02());
		matrixBuffer.put(matrix.m03());


		matrixBuffer.put(matrix.m10());
		matrixBuffer.put(matrix.m11());
		matrixBuffer.put(matrix.m12());
		matrixBuffer.put(matrix.m13());

		matrixBuffer.put(matrix.m20());
		matrixBuffer.put(matrix.m21());
		matrixBuffer.put(matrix.m22());
		matrixBuffer.put(matrix.m23());

		matrixBuffer.put(matrix.m30());
		matrixBuffer.put(matrix.m31());
		matrixBuffer.put(matrix.m32());
		matrixBuffer.put(matrix.m33());
	}
	
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	public abstract void bindAttributes();
	
	public void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	@SuppressWarnings("all")
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			LOGGER.info(GL20.glGetShaderInfoLog(shaderID, 500));
			LOGGER.error("Could not compile shader {}!", file);
			System.exit(-1);
		}
		return shaderID;
	}

	public void loadShineVariables(float damper, float reflectivity) {}

	public void loadNumberOfRows(int numberOfRows) {}

	public void loadOffset(float x, float y) {}

	public void loadFakeLighting(boolean useFakeLighting) {}

}
