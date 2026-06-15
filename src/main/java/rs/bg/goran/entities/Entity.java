package rs.bg.goran.entities;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import rs.bg.goran.models.TexturedModel;

public class Entity {

    private TexturedModel texturedModel;
    private Vector3f position;
    private float rx, ry, rz;
    private float scale;
    private final Matrix4f transformationMatrix;

    public Entity(TexturedModel texturedModel, Vector3f position, float rx, float ry, float rz, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
        this.transformationMatrix = new Matrix4f();
    }

    public void increasePosition(float dx, float dy, float dz) {
        position.add(dx, dy, dz);
    }

    public void increaseRotation(float dx, float dy, float dz) {
        rx += dx;
        ry += dy;
        rz += dz;
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
