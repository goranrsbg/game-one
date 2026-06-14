package rs.bg.goran.renderEngine;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import rs.bg.goran.models.RawModel;
import rs.bg.goran.toolbox.Const;

public class Loader {

    private static final Loader INSTANCE = new Loader();

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    private Loader() {
    }

    public static Loader of() {
        return INSTANCE;
    }

    public RawModel loadToVAO(float[] positions, float[] textureCords, float[] normals, int[] indices) {
        int vaoId = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(Const.V0, Const.V3, positions);
        storeDataInAttributeList(Const.V1, Const.V2, textureCords);
        storeDataInAttributeList(Const.V2, Const.V3, normals);
        unbindVAO();
        return RawModel.of(vaoId, indices.length);
    }

    public int loadTexture(String fileName) {
        Path path = Path.of(Const.DIR_RES, fileName + Const.DOT_PNG);
        int textureId = Const.V0;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(Const.V1);
            IntBuffer height = stack.mallocInt(Const.V1);
            IntBuffer channels = stack.mallocInt(Const.V1);
            // STBImage.stbi_set_flip_vertically_on_load(Const.TRUE);
            ByteBuffer imageBuffer = STBImage.stbi_load(path.toString(), width, height, channels, Const.V4);
            if (imageBuffer == null) {
                IO.println("Failed to load texture file: " + path.toString());
                System.exit(Const.Vn1);
            }
            textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, Const.V0, GL11.GL_RGBA, width.get(Const.V0), height.get(Const.V0), Const.V0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            STBImage.stbi_image_free(imageBuffer);
        }
        textures.add(textureId);
        return textureId;
    }

    public void clean() {
        vaos.forEach(e -> {
            GL30.glDeleteVertexArrays(e);
        });
        vbos.forEach(e -> {
            GL15.glDeleteBuffers(e);
        });
        textures.forEach(e -> {
            GL11.glDeleteTextures(e);
        });
    }

    private int createVAO() {
        int vaoId = GL30.glGenVertexArrays();
        vaos.add(vaoId);
        GL30.glBindVertexArray(vaoId);
        return vaoId;
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, Const.V0, Const.V0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, Const.V0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(Const.V0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
