package net.vakror.farmer.register.option;

import org.joml.Vector3f;

public class Vector3fOption extends Option<Vector3f> {
    public Vector3fOption(Vector3f defaultValue) {
        super(defaultValue);
    }

    public Vector3fOption(float defaultValue) {
        this(defaultValue, defaultValue, defaultValue);
    }

    public Vector3fOption(float defaultValue, float defaultValue1, float defaultValue2) {
        this(new Vector3f(defaultValue, defaultValue1, defaultValue2));
    }

    @Override
    public String getValueString() {
        return value.x + ", " + value.y + ", " + value.z;
    }

    @Override
    public Vector3f valueFromString(String value) {
        String[] array = value.split(", ");
        if (array.length != 3) {
            throw new IllegalStateException("\"%s\" is not a vector type");
        }
        return new Vector3f(Float.parseFloat(array[0]), Float.parseFloat(array[1]), Float.parseFloat(array[2]));
    }
}
