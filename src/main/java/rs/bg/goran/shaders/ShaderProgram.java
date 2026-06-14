package rs.bg.goran.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import rs.bg.goran.toolbox.Const;

public abstract class ShaderProgram {

	private final int programId;
	private final int vertexShaderId;
	private final int fragmentShaderId;

	private static final FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(Const.V16);

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderId = load(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderId = load(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programId = GL20.glCreateProgram();
		GL20.glAttachShader(programId, vertexShaderId);
		GL20.glAttachShader(programId, fragmentShaderId);
		bindAttributes();
		GL20.glLinkProgram(programId);
		GL20.glValidateProgram(programId);
		uniformLocations();
	}

	private int load(String fileName, int type) {
		String path = String.format("/shaders/%s.glsl", fileName);
		InputStream is = getClass().getResourceAsStream(path);
		String code = Const.EMPTY;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
			code = br.readAllAsString();
		} catch (IOException e) {
			System.err.println("Could not read file " + path);
			e.printStackTrace();
			System.exit(Const.Vn1);
		}
		int shaderId = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderId, code);
		GL20.glCompileShader(shaderId);
		if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderId, 500));
			System.err.println("Could not compile shader " + fileName);
			System.exit(Const.Vn1);
		}
		return shaderId;
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programId, attribute, variableName);
	}

	protected abstract void uniformLocations();

	protected int getUnifimLocation(String uniformName) {
		return GL20.glGetUniformLocation(programId, uniformName);
	}

	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	protected void loadBoolean(int location, boolean value) {
		float toLoad = value ? Const.V1f : Const.V0f;
		GL20.glUniform1f(location, toLoad);
	}

	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.get(MATRIX_BUFFER);
		GL20.glUniformMatrix4fv(location, false, MATRIX_BUFFER);
	}

	public void start() {
		GL20.glUseProgram(programId);
	}

	public void stop() {
		GL20.glUseProgram(Const.V0);
	}

	public void clean() {
		stop();
		GL20.glDetachShader(programId, vertexShaderId);
		GL20.glDetachShader(programId, fragmentShaderId);
		GL20.glDeleteShader(vertexShaderId);
		GL20.glDeleteShader(fragmentShaderId);
		GL20.glDeleteProgram(programId);
	}

}