package rs.bg.goran.renderEngine;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import rs.bg.goran.entities.Camera;
import rs.bg.goran.entities.Entity;
import rs.bg.goran.entities.Light;
import rs.bg.goran.models.TexturedModel;
import rs.bg.goran.shaders.StaticShader;
import rs.bg.goran.shaders.TerainShader;
import rs.bg.goran.terains.Terain;
import rs.bg.goran.toolbox.Const;

public class MasterRenderer {

    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private final StaticShader shader;
    private final EntityRenderer renderer;
    private final TerainShader terainShader;
    private final TerainRenderer terainRenderer;

    private final Map<TexturedModel, List<Entity>> entities;
    private final Set<Terain> terains;

    private final Matrix4f projectionMatrix;

    public MasterRenderer() {
        enableCulling();
        this.shader = new StaticShader();
        this.terainShader = new TerainShader();
        this.projectionMatrix = createProjectionMatrix();
        this.renderer = EntityRenderer.of(shader, projectionMatrix);
        this.terainRenderer = new TerainRenderer(terainShader, projectionMatrix);
        this.entities = new HashMap<>();
        this.terains = new HashSet<>();
    }

    public static void enableCulling() {
        GL11.glEnable(GL_CULL_FACE);
        GL11.glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL_CULL_FACE);
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
        terainShader.start();
        terainShader.loadLight(sun);
        terainShader.loadViewMatrix(camera);
        terainRenderer.render(terains);
        terainShader.stop();
    }

    public void processEntity(Entity entity) {
        TexturedModel texturedModel = entity.getTexturedModel();
        entities.putIfAbsent(texturedModel, new ArrayList<>());
        entities.get(texturedModel).add(entity);
    }

    public void processTerain(Terain terain) {
        terains.add(terain);
    }

    private Matrix4f createProjectionMatrix() {
        float aspectRatio = (float) Const.WIDTH / (float) Const.HEIGHT;
        float y_scale = 1f / (float) Math.tan(Math.toRadians(FOV / 2f)) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        Matrix4f matrix = new Matrix4f();
        matrix.m00(x_scale);
        matrix.m11(y_scale);
        matrix.m22(-(FAR_PLANE + NEAR_PLANE) / frustum_length);
        matrix.m23(-1f);
        matrix.m32(-2 * FAR_PLANE * NEAR_PLANE / frustum_length);
        matrix.m33(0f);
        return matrix;
    }

    public void clean() {
        shader.clean();
        terainShader.clean();
    }

}
