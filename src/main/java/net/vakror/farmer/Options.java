package net.vakror.farmer;

import org.joml.Vector3f;

public class Options {
    public boolean useSpecularLighting;
    public float ambientLight;
    public float fov = 70;
    public float nearPlane = 0.1f;
    public float farPlane = 1000f;
    public float fogDensity = 0.007f;
    public float fogGradient = 1.5f;
    public Vector3f skyColor = new Vector3f(0.5f, 0.5f, 0.5f);

    public Options(boolean useSpecularLighting, float ambientLight) {
        this.useSpecularLighting = useSpecularLighting;
        this.ambientLight = ambientLight;
    }
}
