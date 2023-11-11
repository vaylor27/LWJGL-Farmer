package net.vakror.farmer.register.option;

public class BooleanOption extends Option<Boolean> {
    public BooleanOption(boolean defaultValue) {
        super(defaultValue);
    }

    @Override
    public Boolean valueFromString(String value) {
        return Boolean.valueOf(value);
    }
}
