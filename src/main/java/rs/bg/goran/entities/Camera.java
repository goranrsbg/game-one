package rs.bg.goran.entities;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import rs.bg.goran.toolbox.Const;

public class Camera {

    private Vector3f position = new Vector3f(Const.V0f);
    private float pitch;
    private float yaw;
    private float roll;
    private float speed;
    private final long window;
    private final Matrix4f destination;

    public Camera(long window) {
        this.window = window;
        this.speed = 0.1f;
        this.destination = new Matrix4f();
    }

    public void move() {
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            position.z += -speed;
        } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            position.z += speed;
        } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            position.x += -speed;
        } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            position.x += speed;
        } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
            position.y += speed;
        } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
            position.y += -speed;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public Matrix4f getDestination() {
        return destination;
    }

}
