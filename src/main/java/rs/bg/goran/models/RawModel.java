package rs.bg.goran.models;

public class RawModel {

    private final int vaoId;
    private final int vertexCount;

    public RawModel(int vaoId, int vertexCount) {
	this.vaoId = vaoId;
	this.vertexCount = vertexCount;
    }

    public static RawModel of(int vaoId, int vertexCount) {
	return new RawModel(vaoId, vertexCount);
    }

    public int getVaoId() {
	return vaoId;
    }

    public int getVertexCount() {
	return vertexCount;
    }
}
