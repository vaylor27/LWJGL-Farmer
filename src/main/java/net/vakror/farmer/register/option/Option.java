package net.vakror.farmer.register.option;

import net.vakror.farmer.renderEngine.registry.core.RegistryLocation;
import net.vakror.farmer.renderEngine.registry.core.RegistryObject;

public abstract class Option<T> extends RegistryObject {
    T value;
    RegistryLocation registryName;

    public Option(T defaultValue) {
        this.value = defaultValue;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    @Override
    public RegistryLocation getRegistryName() {
        return registryName;
    }

    @Override
    public void setRegistryName(RegistryLocation name) {
        this.registryName = name;
    }

    public String getValueString() {
        return value.toString();
    }

    public abstract T valueFromString(String value);

    public BooleanOption getAsBooleanOption() {
        return (BooleanOption) this;
    }

    public FloatOption getAsFloatOption() {
        return (FloatOption) this;
    }
}
