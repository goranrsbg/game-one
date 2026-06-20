package rs.bg.goran.renderEngine;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import rs.bg.goran.entities.Entity;
import rs.bg.goran.models.RawModel;
import rs.bg.goran.models.TexturedModel;
import rs.bg.goran.shaders.StaticShader;
import rs.bg.goran.textures.ModelTexture;
import rs.bg.goran.toolbox.Const;
import rs.bg.goran.toolbox.Maths;

public class EntityRenderer {

    private static EntityRenderer INSTANCE = null;

    private StaticShader shader;

    private EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public static EntityRenderer of(StaticShader staticShader, Matrix4f projectionMatrix) {
        if (INSTANCE == null) {
            INSTANCE = new EntityRenderer(staticShader, projectionMatrix);
        }
        return INSTANCE;
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        entities.forEach((model, entites) -> {
            prepareTexturedModel(model);
            for (Entity entity : entites) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel()
                                                            .getVertexCount(), GL11.GL_UNSIGNED_INT, Const.V0L);
            }
            unbindTexturedModel();
        });
    }

    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoId());
        GL20.glEnableVertexAttribArray(Const.V0);
        GL20.glEnableVertexAttribArray(Const.V1);
        GL20.glEnableVertexAttribArray(Const.V2);
        ModelTexture texture = model.getModelTexture();
        if (texture.isTransparent()) {
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLightingVariable(texture.isFakeLighting());
        shader.loadShineVariables(texture);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(Const.V0);
        GL20.glDisableVertexAttribArray(Const.V1);
        GL20.glDisableVertexAttribArray(Const.V2);
        GL30.glBindVertexArray(Const.V0);
    }

    private void prepareInstance(Entity entity) {
        Maths.createTransformationMatrix(entity);
        shader.loadTransformationMatrix(entity.getTransformationMatrix());
    }

}
