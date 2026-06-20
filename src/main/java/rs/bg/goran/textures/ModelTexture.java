package rs.bg.goran.textures;

public class ModelTexture {

    private final int textureId;
    private float shineDumper;
    private float reflectivity;

    private boolean transparency;
    private boolean fakeLighting;

    public ModelTexture(int id) {
        this.textureId = id;
        this.shineDumper = 1f;
        this.reflectivity = 0f;
        this.transparency = false;
        this.fakeLighting = false;
    }

    public boolean isTransparent() {
        return transparency;
    }

    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    public boolean isFakeLighting() {
        return fakeLighting;
    }

    public void setFakeLighting(boolean fakeLighting) {
        this.fakeLighting = fakeLighting;
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
