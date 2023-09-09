package net.vakror.farmer;

public class Options {
    public boolean useSpecularLighting;
    public float ambientLight;
    public float fov = 70;
    public float nearPlane = 0.1f;
    public float farPlane = 1000f;

    public Options(boolean useSpecularLighting, float ambientLight) {
        this.useSpecularLighting = useSpecularLighting;
        this.ambientLight = ambientLight;
    }
}
