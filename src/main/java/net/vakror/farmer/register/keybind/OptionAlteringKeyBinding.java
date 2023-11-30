package net.vakror.farmer.register.keybind;

import net.vakror.farmer.register.option.Option;

import java.util.function.Consumer;

import static net.vakror.farmer.renderEngine.renderer.MasterRenderer.regenProjectionMatrix;

public class OptionAlteringKeyBinding<T extends Option<?>> extends KeyBinding {
    private final Consumer<T> onExecute;
    private final T optionToAlter;
    private final boolean shouldReloadMatrices;

    public OptionAlteringKeyBinding(int key, T optionToAlter, Consumer<T> onExecute, boolean shouldReloadMatrices) {
        super(key);
        this.onExecute = onExecute;
        this.optionToAlter = optionToAlter;
        this.shouldReloadMatrices = shouldReloadMatrices;
    }

    public T optionToAlter() {
        return optionToAlter;
    }

    @Override
    public void execute(int scancode, int action, int mods) {
        onExecute.accept(optionToAlter);
        if (shouldReloadMatrices) {
            regenProjectionMatrix();
        }
    }
}
