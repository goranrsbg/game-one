package rs.bg.goran.shaders;

import org.joml.Matrix4f;

import rs.bg.goran.entities.Camera;
import rs.bg.goran.entities.Light;
import rs.bg.goran.textures.ModelTexture;
import rs.bg.goran.toolbox.Const;
import rs.bg.goran.toolbox.Maths;

public class TerainShader extends ShaderProgram {
    private static final TerainShader INSTANCE = new TerainShader();

    private static final String VERTEX_FILE = "terainVertexShader";
    private static final String FRAGMENT_FILE = "terainFragmentShader";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDumper;
    private int location_reflectiviti;

    public TerainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public static TerainShader of() {
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
        location_shineDumper = getUnifimLocation("shineDumper");
        location_reflectiviti = getUnifimLocation("reflectiviti");
    }

    public void loadShineVariables(ModelTexture texture) {
        loadFloat(location_shineDumper, texture.getShineDumper());
        loadFloat(location_reflectiviti, texture.getReflectivity());
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
