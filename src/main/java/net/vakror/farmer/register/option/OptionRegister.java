package net.vakror.farmer.register.option;

import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.registry.SimpleRegister;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import net.vakror.farmer.renderEngine.registry.registries.OptionRegistry;

public class OptionRegister {
    public static final SimpleRegister<OptionRegistry, Option<?>> REGISTER = SimpleRegister.create(DefaultRegistries.OPTIONS);


    public static void registerOptions() {
        REGISTER.register("useSpecularLighting", new BooleanOption(true));
        REGISTER.register("useAmbientLight", new BooleanOption(true));
        REGISTER.register("ambientLight", new FloatOption(0.2f));
        REGISTER.register("fov", new FloatOption(70));
        REGISTER.register("nearPlane", new FloatOption(0.1f));
        REGISTER.register("farPlane", new FloatOption(1000));
        REGISTER.register("fogDensity", new FloatOption(0.0035f));
        REGISTER.register("fogGradient", new FloatOption(5f));
        REGISTER.register("skyColor", new Vector3fOption(0.5f, 0.5f, 0.5f));
        REGISTER.register("runSpeed", new FloatOption(20));
        REGISTER.register("turnSpeed", new FloatOption(160));
        REGISTER.register("gravity", new FloatOption(50));
        REGISTER.register("jumpPower", new FloatOption(30));
        REGISTER.register("mipMapLevel", new FloatOption(-0.4f));

        Options.read();
    }
}
