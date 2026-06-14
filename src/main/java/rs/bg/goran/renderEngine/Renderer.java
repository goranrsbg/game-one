package rs.bg.goran.renderEngine;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import rs.bg.goran.entities.Entity;
import rs.bg.goran.models.RawModel;
import rs.bg.goran.models.TexturedModel;
import rs.bg.goran.shaders.StaticShader;
import rs.bg.goran.toolbox.Const;
import rs.bg.goran.toolbox.Maths;

public class Renderer {

    private static Renderer INSTANCE = null;

    private static final float FOV = 70f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Matrix4f projectionMatrix;

    private Renderer(StaticShader shader) {
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public static Renderer of(StaticShader staticShader) {
        if (INSTANCE == null) {
            INSTANCE = new Renderer(staticShader);
        }
        return INSTANCE;
    }

    public void render(Entity entity, StaticShader shader) {
        TexturedModel texturedModel = entity.getTexturedModel();
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoId());
        GL20.glEnableVertexAttribArray(Const.V0);
        GL20.glEnableVertexAttribArray(Const.V1);
        GL20.glEnableVertexAttribArray(Const.V2);
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getTextureId());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, Const.V0L);
        GL20.glDisableVertexAttribArray(Const.V0);
        GL20.glDisableVertexAttribArray(Const.V1);
        GL20.glDisableVertexAttribArray(Const.V2);
        GL30.glBindVertexArray(Const.V0);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Const.WIDTH / (float) Const.HEIGHT;
        float y_scale = 1f / (float) Math.tan(Math.toRadians(FOV / 2f)) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-(FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23(-1f);
        projectionMatrix.m32(-2 * FAR_PLANE * NEAR_PLANE / frustum_length);
        projectionMatrix.m33(0f);
    }

}
