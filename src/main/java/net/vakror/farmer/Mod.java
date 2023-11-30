package net.vakror.farmer;

public abstract class Mod {
    String id;
    public Mod() {
        id = getId();
    }

    public abstract String getId();

    @Override
    public String toString() {
        return id;
    }
}
