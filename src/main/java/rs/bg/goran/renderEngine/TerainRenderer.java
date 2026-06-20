package rs.bg.goran.renderEngine;

import java.util.Set;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import rs.bg.goran.models.RawModel;
import rs.bg.goran.shaders.TerainShader;
import rs.bg.goran.terains.Terain;
import rs.bg.goran.toolbox.Const;
import rs.bg.goran.toolbox.Maths;

public class TerainRenderer {

    private final TerainShader shader;

    public TerainRenderer(TerainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Set<Terain> terains) {
        for (Terain terain : terains) {
            prepareTerain(terain);
            loadModelMatirx(terain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, Const.V0L);
            unbindTexturedModel();
        }
    }

    private void prepareTerain(Terain terain) {
        RawModel rawModel = terain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoId());
        GL20.glEnableVertexAttribArray(Const.V0);
        GL20.glEnableVertexAttribArray(Const.V1);
        GL20.glEnableVertexAttribArray(Const.V2);
        shader.loadShineVariables(terain.getTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terain.getTexture().getTextureId());
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(Const.V0);
        GL20.glDisableVertexAttribArray(Const.V1);
        GL20.glDisableVertexAttribArray(Const.V2);
        GL30.glBindVertexArray(Const.V0);
    }

    private void loadModelMatirx(Terain terain) {
        Maths.createTransformationMatrix(terain);
        shader.loadTransformationMatrix(terain.getTransformationMatrix());
    }

}
