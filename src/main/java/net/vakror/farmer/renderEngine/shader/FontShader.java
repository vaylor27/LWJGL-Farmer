package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontShader extends ShaderProgram{
	public FontShader() {
		super(new ResourcePath("font/vertexShader"), new ResourcePath("font/fragmentShader"));
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadColor(Vector3f color){
		super.loadVector3("color", color);
	}

	public void loadFontValues(float fontSize) {
		super.loadFloat("width", (float) (0.4));
		super.loadFloat("edge", 0.2f);
	}

	public void loadTranslation(Vector2f translation){
		super.loadVector2("translation", translation);
	}


}
