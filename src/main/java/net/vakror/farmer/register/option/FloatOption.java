package net.vakror.farmer.register.option;

public class FloatOption extends Option<Float> {
    public FloatOption(float defaultValue) {
        super(defaultValue);
    }

    @Override
    public Float valueFromString(String value) {
        return Float.valueOf(value);
    }
}
