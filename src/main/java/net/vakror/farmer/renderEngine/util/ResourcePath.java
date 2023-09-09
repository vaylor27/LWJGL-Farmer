package net.vakror.farmer.renderEngine.util;

public class ResourcePath {
    private final String fileName;

    public ResourcePath(String fileName) {
        this.fileName = fileName;
    }

    public String getModelPath() {
        return "src/main/resources/assets/models/" + fileName + ".obj";
    }

    public String getImagePath() {
        return "src/main/resources/assets/textures/" + fileName + ".png";
    }

    public String getShaderPath() {
        return "src/main/resources/assets/shaders/" + fileName + ".glsl";
    }
}
