package rs.bg.goran.models;

import java.util.Objects;

import rs.bg.goran.textures.ModelTexture;

public class TexturedModel {

    private final RawModel rawModel;
    private final ModelTexture modelTexture;

    public TexturedModel(RawModel rawModel, ModelTexture modelTexture) {
        this.rawModel = rawModel;
        this.modelTexture = modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelTexture.getTextureId(), rawModel.getVaoId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TexturedModel other = (TexturedModel) obj;
        return Objects.equals(modelTexture.getTextureId(), other.modelTexture.getTextureId())
                && Objects.equals(rawModel.getVaoId(), other.rawModel.getVaoId());
    }

}
