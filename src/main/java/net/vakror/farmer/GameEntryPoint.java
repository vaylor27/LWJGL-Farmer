package net.vakror.farmer;

public interface GameEntryPoint {
    void initialize();
    default Priority getPriority() {
        return Priority.NORMAL;
    }
}
