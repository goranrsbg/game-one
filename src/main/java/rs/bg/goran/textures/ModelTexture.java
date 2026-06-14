package rs.bg.goran.textures;

public class ModelTexture {

    private final int textureId;
    private float shineDumper;
    private float reflectivity;

    public ModelTexture(int id) {
        this.textureId = id;
        this.shineDumper = 1f;
        this.reflectivity = 0f;
    }

    public int getTextureId() {
        return textureId;
    }

    public float getShineDumper() {
        return shineDumper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setShineDumper(float shineDumper) {
        this.shineDumper = shineDumper;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
