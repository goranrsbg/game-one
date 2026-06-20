package rs.bg.goran.terains;

import java.util.Objects;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import rs.bg.goran.models.RawModel;
import rs.bg.goran.renderEngine.Loader;
import rs.bg.goran.textures.ModelTexture;
import rs.bg.goran.toolbox.Const;

public class Terain {

    private static final float SIZE = 800f;
    private static final int VERTEX_COUNT = 128;

    private final float x;
    private final float z;
    private final RawModel model;
    private final ModelTexture texture;

    private final Matrix4f transformationMatrix;
    private final Vector3f position;

    public Terain(int gridX, int gridZ, Loader loader, ModelTexture texture) {
        this.texture = texture;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.transformationMatrix = new Matrix4f();
        this.position = new Vector3f(x, Const.V0f, z);
        model = generateTerain(loader);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    private RawModel generateTerain(Loader loader) {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = 0;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;
                textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture.getTextureId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Terain other = (Terain) obj;
        return Objects.equals(texture.getTextureId(), other.texture.getTextureId());
    }

}
