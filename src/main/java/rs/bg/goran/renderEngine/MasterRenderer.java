package rs.bg.goran.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.bg.goran.entities.Camera;
import rs.bg.goran.entities.Entity;
import rs.bg.goran.entities.Light;
import rs.bg.goran.models.TexturedModel;
import rs.bg.goran.shaders.StaticShader;

public class MasterRenderer {

    private final StaticShader shader;
    private final Renderer renderer;

    private final Map<TexturedModel, List<Entity>> entities;

    public MasterRenderer() {
        this.shader = new StaticShader();
        this.renderer = Renderer.of(shader);
        this.entities = new HashMap<>();
    }

    public void move() {
        entities.forEach((_, entities) -> {
            for (Entity entity : entities) {
                entity.increaseRotation(0f, 0.5f, 0f);
            }
        });

    }

    public void render(Light sun, Camera camera) {
        shader.start();
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
    }

    public void processEntity(Entity entity) {
        TexturedModel texturedModel = entity.getTexturedModel();
        entities.putIfAbsent(texturedModel, new ArrayList<>());
        entities.get(texturedModel).add(entity);
    }

    public void clean() {
        shader.clean();
    }

}
