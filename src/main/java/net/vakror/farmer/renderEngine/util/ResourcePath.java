package net.vakror.farmer.renderEngine.util;

import static net.vakror.farmer.FarmerGameMain.appDirPath;

public class ResourcePath {
    private final String fileName;

    public ResourcePath(String fileName) {
        this.fileName = fileName;
    }

    public String getModelPath() {
        return appDirPath + "/assets/models/" + fileName + ".obj";
    }

    public String getImagePath() {
        return appDirPath + "/assets/textures/" + fileName + ".png";
    }

    public String getShaderPath() {
        return appDirPath + "/assets/shaders/" + fileName + ".glsl";
    }
}
