package edu.up.isgc.raytracer.files;

import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.shapes.models.Face;
import edu.up.isgc.raytracer.shapes.models.NormalVertex;
import edu.up.isgc.raytracer.shapes.models.Texture;
import edu.up.isgc.raytracer.shapes.models.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for reading and parsing OBJ 3D model files.
 * Handles vertex, texture, normal, and face data extraction from OBJ files.
 */
public class FileRead {

    /**
     * Reads an OBJ file and separates its components into different lists.
     *
     * @param filePath Path to the OBJ file to read
     * @return ArrayList containing separate lists for vertices, faces, textures, and normals
     */
    public static ArrayList<ArrayList<String>> readFile(String filePath) {
        ArrayList<String> vertexList = new ArrayList<>();
        ArrayList<String> faceList = new ArrayList<>();
        ArrayList<String> textureList = new ArrayList<>();
        ArrayList<String> vertexNormalList = new ArrayList<>();
        Map<Integer, Material> materialMap = new HashMap<>();

        // Patterns for matching different OBJ file components
        Pattern vertex = Pattern.compile("^v[^a-zA-Z]*");
        Pattern texture = Pattern.compile("^vt[^a-zA-Z]*");
        Pattern normal = Pattern.compile("^vn[^a-zA-Z]*");
        Pattern face = Pattern.compile("^f[^a-zA-Z]*");
        Pattern mtl = Pattern.compile("^usemtl\\s+.+");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int mtlFaceCount = 0;
            String currentMaterialName = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.matches(vertex.pattern())) {
                    vertexList.add(line);
                } else if (line.matches(texture.pattern())) {
                    textureList.add(line);
                } else if (line.matches(normal.pattern())) {
                    vertexNormalList.add(line);
                } else if (line.matches(mtl.pattern())) {
                    currentMaterialName = line.split("\\s+")[1]; // store latest usemtl
                    System.out.println("Material: " + currentMaterialName);
                } else if (line.matches(face.pattern())) {
                    faceList.add(line);

                    // Get current material (or default if none specified)
                    Material material = Material.findByName(currentMaterialName);
                    if (material == null) {
                        material = null; // Default material
                        System.err.println("Warning: Using default material for face " + mtlFaceCount);
                    }

                    // Count triangles this face produces
                    String[] parts = line.split("\\s+");
                    boolean isQuad = (parts.length - 1 == 4);

                    // Assign material to each generated triangle
                    materialMap.put(mtlFaceCount, material);
                    if (isQuad) {
                        materialMap.put(mtlFaceCount + 1, material);
                    }

                    // Increment counter (by 1 for triangle, 2 for quad)
                    mtlFaceCount += isQuad ? 2 : 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayList<String>> objLines = new ArrayList<>();
        objLines.add(vertexList);
        objLines.add(faceList);
        objLines.add(textureList);
        objLines.add(vertexNormalList);

        Face.setMaterialMap(materialMap);

        return objLines;
    }

    /**
     * Creates a map of vertex indices to their coordinates.
     *
     * @param lines List of vertex lines from the OBJ file
     * @return HashMap mapping vertex indices to their 3D coordinates
     */
    public static HashMap<Integer, Double[]> createVertexMap(ArrayList<String> lines) {
        HashMap<Integer, Double[]> map = new HashMap<>();
        int index = 0;
        for(String line : lines) {
            Double[] coords = normalizeDouble(line);
            if (coords != null) {
                map.put(Integer.valueOf(index++), coords); // Use sequential index as key
            }
        }
        return map;
    }

    /**
     * Creates a map of face hashes to their vertex, texture, and normal indices.
     *
     * @param lines List of face lines from the OBJ file
     * @return HashMap mapping face hashes to their component indices
     */
    public static HashMap<Integer, Integer[][]> createFaceMap(ArrayList<String> lines) {
        HashMap<Integer, Integer[][]> map = new HashMap<>();
        Integer[] coords;
        Integer[] textures;
        Integer[] normals;

        for(String line : lines) {
            coords = Objects.requireNonNull(FileRead.normalizeInteger(line))[0];
            textures = Objects.requireNonNull(FileRead.normalizeInteger(line))[1];
            normals = Objects.requireNonNull(FileRead.normalizeInteger(line))[2];

            if(coords.length == 3) {
                map.put(Integer.valueOf(Arrays.hashCode(coords)), new Integer[][]{coords, textures, normals});
            }
            if(coords.length == 4) {
                // Split quad into two triangles
                Integer[] coords1 = new Integer[]{coords[0], coords[1], coords[2]};
                Integer[] coords2 = new Integer[]{coords[0], coords[2], coords[3]};

                Integer[] textures1 = new Integer[]{textures[0], textures[1], textures[2]};
                Integer[] textures2 = new Integer[]{textures[0], textures[2], textures[3]};

                Integer[] normals1 = new Integer[]{normals[0], normals[1], normals[2]};
                Integer[] normals2 = new Integer[]{normals[0], normals[2], normals[3]};

                map.put(Integer.valueOf(Arrays.hashCode(coords1)), new Integer[][]{coords1, textures1, normals1});
                map.put(Integer.valueOf(Arrays.hashCode(coords2)), new Integer[][]{coords2, textures2, normals2});
            }
        }
        return map;
    }

    /**
     * Parses a line from an OBJ file into vertex, normal, or texture coordinates.
     *
     * @param line The line to parse
     * @return Array of Double coordinates, or null if line doesn't match any pattern
     */
    public static Double[] normalizeDouble(String line) {
        Pattern pattern = Pattern.compile("^v\\s+(-?\\d*\\.\\d+)\\s+(-?\\d*\\.\\d+)\\s+(-?\\d*\\.\\d+)");
        Pattern patternNormal = Pattern.compile("^vn\\s+(-?\\d+(?:\\.\\d+)?)\\s+(-?\\d+(?:\\.\\d+)?)\\s+(-?\\d+(?:\\.\\d+)?)");
        Pattern patternTexture = Pattern.compile(
                "^vt\\s+(-?\\d+(?:\\.\\d+)?)\\s+(-?\\d+(?:\\.\\d+)?)(?:\\s+(-?\\d+(?:\\.\\d+)?))?"
        );

        Matcher matcher = pattern.matcher(line);
        Matcher matcherNormal = patternNormal.matcher(line);
        Matcher matcherTexture = patternTexture.matcher(line);

        if (matcher.find()) {
            Double v1 = Double.parseDouble(matcher.group(1));
            Double v2 = Double.parseDouble(matcher.group(2));
            Double v3 = Double.parseDouble(matcher.group(3));
            return new Double[]{v1, v2, v3};
        } else if (matcherNormal.find()) {
            Double v1 = Double.parseDouble(matcherNormal.group(1));
            Double v2 = Double.parseDouble(matcherNormal.group(2));
            Double v3 = Double.parseDouble(matcherNormal.group(3));
            return new Double[]{v1, v2, v3};
        } else if (matcherTexture.find()) {
            Double v1 = Double.parseDouble(matcherTexture.group(1));
            Double v2 = Double.parseDouble(matcherTexture.group(2));
            Double v3 = 0.0;

            if (matcherTexture.group(3) != null && !matcherTexture.group(3).isEmpty()) {
                v3 = Double.parseDouble(matcherTexture.group(3));
            }

            return new Double[]{v1, v2, v3};
        } else {
            System.err.println("////NO MATCH for line: " + line);
            return null;
        }
    }

    /**
     * Parses a face line from an OBJ file into vertex, texture, and normal indices.
     *
     * @param line The face line to parse
     * @return 2D array containing vertex, texture, and normal indices for each face
     */
    public static Integer[][] normalizeInteger(String line) {
        Pattern pattern = Pattern.compile("^f\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?");
        Pattern patternQuad = Pattern.compile("^f\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?");
        Matcher matcher = pattern.matcher(line.trim());
        Matcher matcherQuad = patternQuad.matcher(line.trim());

        if(matcherQuad.find()) {
            // Handle quad face (4 vertices)
            Integer v1 = Integer.parseInt(matcherQuad.group(1));
            Integer v2 = Integer.parseInt(matcherQuad.group(4));
            Integer v3 = Integer.parseInt(matcherQuad.group(7));
            Integer v4 = Integer.parseInt(matcherQuad.group(10));

            Integer t1 = Integer.parseInt(toParsable(matcherQuad.group(2)));
            Integer t2 = Integer.parseInt(toParsable(matcherQuad.group(5)));
            Integer t3 = Integer.parseInt(toParsable(matcherQuad.group(8)));
            Integer t4 = Integer.parseInt(toParsable(matcherQuad.group(11)));

            Integer nV1 = Integer.parseInt(toParsable(matcherQuad.group(3)));
            Integer nV2 = Integer.parseInt(toParsable(matcherQuad.group(6)));
            Integer nV3 = Integer.parseInt(toParsable(matcherQuad.group(9)));
            Integer nV4 = Integer.parseInt(toParsable(matcherQuad.group(12)));

            return new Integer[][]{{v1, v2, v3, v4},{t1, t2, t3, t4}, {nV1, nV2, nV3, nV4}};
        }
        else if (matcher.find()) {
            // Handle triangular face (3 vertices)
            Integer v1 = Integer.parseInt(matcher.group(1));
            Integer v2 = Integer.parseInt(matcher.group(4));
            Integer v3 = Integer.parseInt(matcher.group(7));

            Integer t1 = Integer.parseInt(toParsable(matcher.group(2)));
            Integer t2 = Integer.parseInt(toParsable(matcher.group(5)));
            Integer t3 = Integer.parseInt(toParsable(matcher.group(8)));

            Integer nV1 = Integer.parseInt(toParsable(matcher.group(3)));
            Integer nV2 = Integer.parseInt(toParsable(matcher.group(6)));
            Integer nV3 = Integer.parseInt(toParsable(matcher.group(9)));

            return new Integer[][]{{v1, v2, v3},{t1, t2, t3}, {nV1, nV2, nV3}};
        } else {
            System.err.println("NO MATCH for line: " + line);
            return null;
        }
    }

    /**
     * Converts an empty string to "0" for parsing purposes.
     *
     * @param line The string to check
     * @return "0" if input is empty, otherwise the original string
     */
    public static String toParsable(String line) {
        return !Objects.equals(line, "") ? line : "0";
    }

    /**
     * Extracts and stores all components from the OBJ file data.
     *
     * @param list ArrayList containing the separated OBJ file components
     */
    public static void extractComponents(ArrayList<ArrayList<String>> list) {
        // Clear previous data
        Vertex.clear();
        NormalVertex.clear();
        Face.clear();
        Texture.clear();

        // Store vertices in order (index matches OBJ index - 1)
        for(Double[] vertex : createVertexMap(list.getFirst()).values()) {
            Vertex.addVertex(vertex);
        }

        // Store texture coordinates
        for(Double[] texture : createVertexMap(list.get(2)).values()) {
            Texture.addTexture(texture);
        }

        // Store vertex normals
        for(Double[] normalVertex : createVertexMap(list.getLast()).values()) {
            NormalVertex.addNormalVertex(normalVertex);
        }

        // Store faces
        for(Integer[][] face : createFaceMap(list.get(1)).values()) {
            Face.addFace(face);
        }
    }

    /**
     * Compiles an OBJ file by reading and extracting all its components.
     *
     * @param filePath Path to the OBJ file to compile
     */
    public static void compileObj(String filePath) {
        extractComponents(readFile(filePath));
    }
}