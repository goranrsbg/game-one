package rs.bg.goran.renderEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joml.Vector2f;
import org.joml.Vector3f;

import rs.bg.goran.models.RawModel;
import rs.bg.goran.toolbox.Const;

public class OBJLoader {

    public static final Pattern F_PATTERN = Pattern.compile("(?<x>[0-9]+)\\/(?<y>[0-9]+)\\/(?<z>[0-9]+)");
    public static final String GX = "x";
    public static final String GY = "y";
    public static final String GZ = "z";

    public static RawModel loadObjModel(String fileName, Loader loader) {
        Path filePath = Path.of(Const.DIR_RES, fileName + Const.DOT_OBJ);
        File file = filePath.toFile();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;
        int[] vertexData = new int[3];
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String next = scanner.next();
                switch (next) {
                case "v":
                    Vector3f vector = new Vector3f(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat());
                    vertices.add(vector);
                    break;
                case "vt":
                    Vector2f texture = new Vector2f(scanner.nextFloat(), scanner.nextFloat());
                    textures.add(texture);
                    break;
                case "vn":
                    Vector3f normal = new Vector3f(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat());
                    normals.add(normal);
                    break;
                case "f":
                    if (textureArray == null) {
                        textureArray = new float[vertices.size() * 2];
                        normalsArray = new float[vertices.size() * 3];
                    }
                    String first = scanner.next();
                    String second = scanner.next();
                    String third = scanner.next();
                    Matcher matcher = F_PATTERN.matcher(first);
                    if (matcher.matches()) {
                        procesVertexData(vertexData, matcher);
                        procesVertex(vertexData, indices, textures, normals, textureArray, normalsArray);
                    } else {
                        System.out.print("Failed to match: " + first);
                    }
                    matcher = F_PATTERN.matcher(second);
                    if (matcher.matches()) {
                        procesVertexData(vertexData, matcher);
                        procesVertex(vertexData, indices, textures, normals, textureArray, normalsArray);
                    } else {
                        System.out.print("Failed to match: " + second);
                    }
                    matcher = F_PATTERN.matcher(third);
                    if (matcher.matches()) {
                        procesVertexData(vertexData, matcher);
                        procesVertex(vertexData, indices, textures, normals, textureArray, normalsArray);
                    } else {
                        System.out.print("Failed to match: " + third);
                    }
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Res file not found: " + fileName);
        }
        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];
        int i = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[i++] = vertex.x;
            verticesArray[i++] = vertex.y;
            verticesArray[i++] = vertex.z;
        }
        i = 0;
        for (int indice : indices) {
            indicesArray[i++] = indice;
        }
        return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }

    private static void procesVertexData(int[] vertexData, Matcher matcher) {
        String sx = matcher.group(GX);
        String sy = matcher.group(GY);
        String sz = matcher.group(GZ);
        vertexData[0] = Integer.parseInt(sx) - 1;
        vertexData[1] = Integer.parseInt(sy) - 1;
        vertexData[2] = Integer.parseInt(sz) - 1;
    }

    private static void procesVertex(int[] vertexData, List<Integer> indices, List<Vector2f> textures,
            List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        indices.add(vertexData[0]);
        Vector2f currentTexture = textures.get(vertexData[1]);
        textureArray[vertexData[0] * 2] = currentTexture.x;
        textureArray[vertexData[0] * 2 + 1] = 1f - currentTexture.y;
        Vector3f currentNorm = normals.get(vertexData[2]);
        normalsArray[vertexData[0] * 3] = currentNorm.x;
        normalsArray[vertexData[0] * 3 + 1] = currentNorm.y;
        normalsArray[vertexData[0] * 3 + 2] = currentNorm.z;
    }

}
