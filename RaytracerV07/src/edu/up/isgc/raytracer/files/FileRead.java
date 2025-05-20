package edu.up.isgc.raytracer.files;

import edu.up.isgc.raytracer.shapes.models.Face;
import edu.up.isgc.raytracer.shapes.models.NormalVertex;
import edu.up.isgc.raytracer.shapes.models.Texture;
import edu.up.isgc.raytracer.shapes.models.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRead {
    public static ArrayList<ArrayList<String>> readFile(String filePath) {
        ArrayList<String> vertexList = new ArrayList<>();
        ArrayList<String> faceList = new ArrayList<>();
        ArrayList<String> textureList = new ArrayList<>();
        ArrayList<String> vertexNormalList = new ArrayList<>();

        File file = new File(filePath);
        Pattern vertex = Pattern.compile("^v[^a-zA-Z]*");
        Pattern texture = Pattern.compile("^vt[^a-zA-Z]*");
        Pattern normal = Pattern.compile("^vn[^a-zA-Z]*");
        Pattern face = Pattern.compile("^f[^a-zA-Z]*");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                if(line.matches(vertex.pattern())) { vertexList.add(line); }
                else if(line.matches(face.pattern())){ faceList.add(line); }
                else if(line.matches(texture.pattern())){ textureList.add(line); }
                else if(line.matches(normal.pattern())){ vertexNormalList.add(line); }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<ArrayList<String>> objLines = new ArrayList<>();
        objLines.add(vertexList);
        objLines.add(faceList);
        objLines.add(textureList);
        objLines.add(vertexNormalList);

        return objLines;
    }
    public static HashMap<Integer, Double[]> createVertexMap(ArrayList<String> lines) {
        HashMap<Integer, Double[]> map = new HashMap<>();
        int index = 0;
        for(String line : lines) {
            Double[] coords = normalizeDouble(line);
            if (coords != null) {
                //System.out.println("Vertex " + (index+1) + ": " + coords[0] + " " + coords[1] + " " + coords[2]);
                map.put(Integer.valueOf(index++), coords); // Use sequential index as key
            }
        }
        return map;
    }

    public static HashMap<Integer, Integer[][]> createFaceMap(ArrayList<String> lines) {
        HashMap<Integer, Integer[][]> map = new HashMap<>();
        Integer[] coords;
        Integer[] textures;
        Integer[] normals;

        for(String line : lines) {
            coords = Objects.requireNonNull(FileRead.normalizeInteger(line))[0];
            textures = Objects.requireNonNull(FileRead.normalizeInteger(line))[1];
            normals = Objects.requireNonNull(FileRead.normalizeInteger(line))[2];

            //if(!(map.containsKey(Arrays.hashCode(coords)))){
                //System.out.println(coords[0] + " " + coords[1] + " " + coords[2]);
                if(coords.length == 3) map.put(Integer.valueOf(Arrays.hashCode(coords)), new Integer[][]{coords, textures, normals});
                if(coords.length == 4){
                    Integer[] coords1 = new Integer[]{coords[0], coords[1], coords[2]};
                    Integer[] coords2 = new Integer[]{coords[0], coords[2], coords[3]};

                    Integer[] textures1 = new Integer[]{textures[0], textures[1], textures[2]};
                    Integer[] textures2 = new Integer[]{textures[0], textures[2], textures[3]};

                    Integer[] normals1 = new Integer[]{normals[0], normals[1], normals[2]};
                    Integer[] normals2 = new Integer[]{normals[0], normals[2], normals[3]};

                    map.put(Integer.valueOf(Arrays.hashCode(coords1)), new Integer[][]{coords1, textures1, normals1});
                    map.put(Integer.valueOf(Arrays.hashCode(coords2)), new Integer[][]{coords2, textures2, normals2});
                }
            //}

        }
        return map;
    }

    public static Double[] normalizeDouble(String line) {
        Pattern pattern = Pattern.compile("^v\\s+(-?\\d*\\.\\d+)\\s+(-?\\d*\\.\\d+)\\s+(-?\\d*\\.\\d+)");
        Pattern patternNormal = Pattern.compile("^vn\\s+(-?\\d*\\.\\d+)\\s+(-?\\d*\\.\\d+)\\s+(-?\\d*\\.\\d+)");
        //Pattern patternTexture = Pattern.compile("^vt\\s+(-?\\d*\\.\\d+)\\s+(-?\\d*\\.\\d+)\\s+(-?\\d*\\.\\d+)");
        Pattern patternTexture = Pattern.compile(
                "^vt\\s+(-?\\d+(?:\\.\\d+)?)\\s+(-?\\d+(?:\\.\\d+)?)(?:\\s+(-?\\d+(?:\\.\\d+)?))?"
        );


        Matcher matcher = pattern.matcher(line);
        Matcher matcherNormal = patternNormal.matcher(line);
        Matcher matcherTexture = patternTexture.matcher(line);

        if (matcher.find()) {
            Double v1 = (Double) Double.parseDouble(matcher.group(1));
            Double v2 = (Double) Double.parseDouble(matcher.group(2));
            Double v3 = (Double) Double.parseDouble(matcher.group(3));

            //System.out.println("Vertex Values: " + v1 + ", " + v2 + ", " + v3);
            return new Double[]{v1, v2, v3};
        } else if (matcherNormal.find()) {
            Double v1 = (Double) Double.parseDouble(matcherNormal.group(1));
            Double v2 = (Double) Double.parseDouble(matcherNormal.group(2));
            Double v3 = (Double) Double.parseDouble(matcherNormal.group(3));

            //System.out.println("Vertex Values: " + v1 + ", " + v2 + ", " + v3);
            return new Double[]{v1, v2, v3};
        } else if (matcherTexture.find()) {
            Double v1 = (Double) Double.parseDouble(matcherTexture.group(1));
            Double v2 = (Double) Double.parseDouble(matcherTexture.group(2));
            Double v3 = (Double) Double.parseDouble(matcherTexture.group(3));

            //System.out.println("Vertex Values: " + v1 + ", " + v2 + ", " + v3);
            return new Double[]{v1, v2, v3};
        } else {
            System.err.println("////NO MATCH for line: " + line);
            return null;
        }
    }

    public static Integer[][] normalizeInteger(String line) {
        Pattern pattern = Pattern.compile("^f\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?");
        Pattern patternQuad = Pattern.compile("^f\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?\\s+(\\d+)(?:/(\\d*)(?:/(\\d*))?)?");
        Matcher matcher = pattern.matcher(line.trim());
        Matcher matcherQuad = patternQuad.matcher(line.trim());
        if(matcherQuad.find()){
            Integer v1 = (Integer) Integer.parseInt(matcherQuad.group(1));
            Integer v2 = (Integer) Integer.parseInt(matcherQuad.group(4));
            Integer v3 = (Integer) Integer.parseInt(matcherQuad.group(7));
            Integer v4 = (Integer) Integer.parseInt(matcherQuad.group(10));

            Integer t1 = (Integer) Integer.parseInt( FileRead.toParsable(matcherQuad.group(2)) );
            Integer t2 = (Integer) Integer.parseInt( FileRead.toParsable(matcherQuad.group(5)) );
            Integer t3 = (Integer) Integer.parseInt( FileRead.toParsable(matcherQuad.group(8)) );
            Integer t4 = (Integer) Integer.parseInt( FileRead.toParsable(matcherQuad.group(11)) );

            Integer nV1 = (Integer) Integer.parseInt( FileRead.toParsable(matcherQuad.group(3)) );
            Integer nV2 = (Integer) Integer.parseInt( FileRead.toParsable(matcherQuad.group(6)) );
            Integer nV3 = (Integer) Integer.parseInt( FileRead.toParsable(matcherQuad.group(9)) );
            Integer nV4 = (Integer) Integer.parseInt( FileRead.toParsable(matcherQuad.group(12)) );

            return new Integer[][]{{v1, v2, v3, v4},{t1, t2, t3, t4}, {nV1, nV2, nV3, nV4}};
        }
        else if (matcher.find()) {
            Integer v1 = (Integer) Integer.parseInt(matcher.group(1));
            Integer v2 = (Integer) Integer.parseInt(matcher.group(4));
            Integer v3 = (Integer) Integer.parseInt(matcher.group(7));

            Integer t1 = (Integer) Integer.parseInt( FileRead.toParsable(matcher.group(2)) );
            Integer t2 = (Integer) Integer.parseInt( FileRead.toParsable(matcher.group(5)) );
            Integer t3 = (Integer) Integer.parseInt( FileRead.toParsable(matcher.group(8)) );

            Integer nV1 = (Integer) Integer.parseInt( FileRead.toParsable(matcher.group(3)) );
            Integer nV2 = (Integer) Integer.parseInt( FileRead.toParsable(matcher.group(6)) );
            Integer nV3 = (Integer) Integer.parseInt( FileRead.toParsable(matcher.group(9)) );

            return new Integer[][]{{v1, v2, v3},{t1, t2, t3}, {nV1, nV2, nV3}};
        } else {
            System.err.println("NO MATCH for line: " + line);
            return null;
        }
    }

    public static String toParsable(String line) {
        return !Objects.equals(line, "")? line : "0";
    }

    public static void extractComponents(ArrayList<ArrayList<String>> list){
        // Clear previous data

        Vertex.clear();
        NormalVertex.clear();
        Face.clear();
        Texture.clear();

        // Store vertices in order (index matches OBJ index - 1)
        for(Double[] vertex : createVertexMap(list.getFirst()).values()){
            Vertex.addVertex(vertex);
        }

        //System.out.println("Vertex count: " + Vertex.getVertexes().size());


        // Store faces

        for(Double[] texture : createVertexMap(list.get(2)).values()){
            Texture.addTexture(texture);
        }

        for(Double[] normalVertex : createVertexMap(list.getLast()).values()){
            NormalVertex.addNormalVertex(normalVertex);
        }

        for(Integer[][] face : createFaceMap(list.get(1)).values()){
            Face.addFace(face);
        }
    }

    public static void compileObj(String filePath) {
        extractComponents(readFile(filePath));
    }
}