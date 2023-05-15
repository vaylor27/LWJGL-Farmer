package net.vakror.farmer.renderEngine.shader;

public class StaticShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "vertexShader";
	private static final String FRAGMENT_FILE = "fragmentShader";

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	

}
