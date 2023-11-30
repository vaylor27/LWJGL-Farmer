package net.vakror.farmer.renderEngine.particle;

import net.vakror.farmer.GameEntryPoint;
import net.vakror.farmer.renderEngine.camera.Camera;
import net.vakror.farmer.renderEngine.listener.register.AutoRegisterListener;
import net.vakror.farmer.renderEngine.listener.type.RenderListener;
import net.vakror.farmer.renderEngine.renderer.MasterRenderer;
import net.vakror.farmer.renderEngine.renderer.ParticleRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AutoRegisterListener
public class ParticleMaster implements RenderListener, GameEntryPoint {

    private static List<Particle> particles = new ArrayList<>();
    private static ParticleRenderer renderer;

    public void initialize() {
        renderer = new ParticleRenderer(MasterRenderer.projectionMatrix);
    }

    public void onRender() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            boolean stillAlive = particle.update();
            if (!stillAlive) {
                iterator.remove();
            }
        }
    }

    public static void renderParticles(Camera camera) {
        renderer.render(particles, camera);
    }

    public static void cleanUp() {
        renderer.cleanUp();
    }

    public static void addParticle(Particle particle) {
        particles.add(particle);
    }
}
