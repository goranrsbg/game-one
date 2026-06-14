package rs.bg.goran.shaders;

import org.joml.Matrix4f;

import rs.bg.goran.entities.Camera;
import rs.bg.goran.entities.Light;
import rs.bg.goran.toolbox.Const;
import rs.bg.goran.toolbox.Maths;

public class StaticShader extends ShaderProgram {

    private static final StaticShader INSTANCE = new StaticShader();

    private static final String VERTEX_FILE = "vertexShader";
    private static final String FRAGMENT_FILE = "fragmentShader";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public static StaticShader of() {
        return INSTANCE;
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(Const.V0, "position");
        bindAttribute(Const.V1, "textureCoords");
        bindAttribute(Const.V2, "normal");
    }

    @Override
    protected void uniformLocations() {
        location_transformationMatrix = getUnifimLocation("transformationMatrix");
        location_projectionMatrix = getUnifimLocation("projectionMatrix");
        location_viewMatrix = getUnifimLocation("viewMatrix");
        location_lightPosition = getUnifimLocation("lightPosition");
        location_lightColor = getUnifimLocation("lightColor");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLight(Light light) {
        loadVector(location_lightPosition, light.getPosition());
        loadVector(location_lightColor, light.getColor());
    }

    public void loadViewMatrix(Camera camera) {
        Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, camera.getDestination());
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        loadMatrix(location_projectionMatrix, matrix);
    }
}
