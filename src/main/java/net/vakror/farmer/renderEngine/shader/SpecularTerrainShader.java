package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.util.ResourcePath;

public class SpecularTerrainShader extends PerPixelTerrainShader {

	private int shineDamperLocation;
	private int reflectivityLocation;

	public SpecularTerrainShader() {
		super(new ResourcePath("specular/terrain/vertexShader"), new ResourcePath("specular/terrain/fragmentShader"));
	}

	@Override
	protected void getAllUniformLoactions() {
		super.getAllUniformLoactions();
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
	}

	@Override
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(shineDamperLocation, damper);
		super.loadFloat(reflectivityLocation, reflectivity);
	}
}
