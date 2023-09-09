package net.vakror.farmer.renderEngine.shader;

import net.vakror.farmer.renderEngine.entity.Camera;
import net.vakror.farmer.renderEngine.entity.Light;
import net.vakror.farmer.renderEngine.util.Mth;
import net.vakror.farmer.renderEngine.util.ResourcePath;
import org.joml.Matrix4f;

public class SpecularStaticShader extends PerPixelStaticShader{

	private int shineDamperLocation;
	private int reflectivityLocation;

	public SpecularStaticShader() {
		super(new ResourcePath("specular/vertexShader"), new ResourcePath("specular/fragmentShader"));
	}

	@Override
	protected void getAllUniformLoactions() {
		super.getAllUniformLoactions();
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
	}

	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(shineDamperLocation, damper);
		super.loadFloat(reflectivityLocation, reflectivity);
	}
}
