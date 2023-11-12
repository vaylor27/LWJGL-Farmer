package net.vakror.farmer.renderEngine.shader.statiic;

import net.vakror.farmer.renderEngine.util.ResourcePath;

public class SpecularStaticShader extends PerPixelStaticShader {

	private int shineDamperLocation;
	private int reflectivityLocation;

	public SpecularStaticShader() {
		super(new ResourcePath("specular/vertexShader"), new ResourcePath("specular/fragmentShader"));
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
	}

	@Override
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(shineDamperLocation, damper);
		super.loadFloat(reflectivityLocation, reflectivity);
	}
}
