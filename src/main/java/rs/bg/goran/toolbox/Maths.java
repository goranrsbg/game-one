package rs.bg.goran.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import rs.bg.goran.entities.Camera;
import rs.bg.goran.entities.Entity;
import rs.bg.goran.terains.Terain;

public class Maths {

    public static void createTransformationMatrix(Entity entity) {
        createTransformationMatrix(entity.getTransformationMatrix(), entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
    }

    public static void createTransformationMatrix(Terain terain) {
        createTransformationMatrix(terain.getTransformationMatrix(), terain.getPosition(), Const.V0f, Const.V0f, Const.V0f, Const.V1f);
    }

    public static void createTransformationMatrix(Matrix4f destination, Vector3fc translation, float rx, float ry,
            float rz, float scale) {
        destination.identity()
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
