package rs.bg.goran.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import rs.bg.goran.entities.Camera;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3fc translation, float rx, float ry, float rz,
            float scale) {
        Matrix4f matrix = new Matrix4f();
        return matrix.identity()
                     .translate(translation)
                     .rotateX((float) Math.toRadians(rx))
                     .rotateY((float) Math.toRadians(ry))
                     .rotateZ((float) Math.toRadians(rz))
                     .scale(scale);
    }

    public static void createViewMatrix(Camera camera) {
        Matrix4f destination = camera.getDestination();
        Vector3f negativeCameraPosition = camera.getPosition().negate(new Vector3f());
        destination.identity()
                   .rotateX((float) Math.toRadians(camera.getPitch()))
                   .rotateY((float) Math.toRadians(camera.getYaw()))
                   .rotateZ((float) Math.toRadians(camera.getRoll()))
                   .translate(negativeCameraPosition);
    }
}
