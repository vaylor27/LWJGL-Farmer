package net.vakror.farmer.renderEngine.registry.core;

import java.util.Objects;

public class RegistryLocation {
    String path;

    public RegistryLocation(String path) {
        this.path = "src/main/resources/assets/" + path;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistryLocation location)) return false;
        return Objects.equals(path, location.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
