package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontShader extends ShaderProgram{

	private int colorLocation;
	private int location_translation;
	
	public FontShader() {
		super(new ResourcePath("font/vertexShader"), new ResourcePath("font/fragmentShader"));
	}

	@Override
	protected void getAllUniformLocations() {
		colorLocation = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadColor(Vector3f color){
		super.loadVector3(colorLocation, color);
	}
	
	public void loadTranslation(Vector2f translation){
		super.loadVector2(location_translation, translation);
	}


}
