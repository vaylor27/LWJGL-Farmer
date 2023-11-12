package net.vakror.farmer.renderEngine.texture;

public class ModelTexture {

    private int textureID;
    private float shineDamper = 1;
    private float reflectivity = 0;

    private boolean hasTransparency = false;
    private boolean useFakeLight = false;

    private int numberOfRows = 1;

    public ModelTexture(int id) {
        this.textureID = id;
    }

    public ModelTexture(int id, float shineDamper, float reflectivity, boolean hasTransparency, boolean useFakeLight) {
        this.textureID = id;
        this.shineDamper = shineDamper;
        this.reflectivity = reflectivity;
        this.hasTransparency = hasTransparency;
        this.useFakeLight = useFakeLight;
    }

    public int numberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getID() {
        return textureID;
    }

    public boolean useFakeLight() {
        return useFakeLight;
    }

    public void setUseFakeLight(boolean useFakeLight) {
        this.useFakeLight = useFakeLight;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }
}
