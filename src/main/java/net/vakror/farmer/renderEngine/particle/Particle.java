package net.vakror.farmer.renderEngine.particle;


import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Window;
import org.joml.Vector3f;

public class Particle {
    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;

    private float elapsedTime = 0;

    public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public static final float GRAVITY = 50;

    public boolean update() {
        velocity.y += GRAVITY * gravityEffect * Window.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);
        change.mul(Window.getFrameTimeSeconds());
        change.add(position, position);
        elapsedTime += Window.getFrameTimeSeconds();
        return elapsedTime < lifeLength;
    }
}
