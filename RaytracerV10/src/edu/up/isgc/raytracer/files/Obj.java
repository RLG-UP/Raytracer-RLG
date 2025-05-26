package edu.up.isgc.raytracer.files;

import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.shapes.Triangle;
import edu.up.isgc.raytracer.shapes.models.Face;
import edu.up.isgc.raytracer.shapes.models.Polygon;
import edu.up.isgc.raytracer.world.Scene;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Obj {
    /*
    public static void RenderObj(Scene scene, String objPath, String mtlPath){
        MTLReader.readMTL(mtlPath);
        scene.addPolygon(new Polygon(objPath));
        Face.clearMaterialMap();
    }

     */
    /*

    public static void RenderObj(Scene scene, String objPath, Material material){
        Polygon polygon = new Polygon(objPath, material);
        //polygon.rotate(0, 90, 0);
        //polygon.scale(10,10,10);
        scene.addPolygon(polygon);
        Face.clearMaterialMap();
    }

     */

    public static void RenderObj(Scene scene, String objPath, Material material, Vector3D rotate, Vector3D scale, Vector3D translate){
        Polygon polygon = new Polygon(objPath, material);
        polygon.rotate((float)rotate.x, (float)rotate.y, (float)rotate.x);
        polygon.scale((float)scale.x,(float)scale.y,(float)scale.z);
        polygon.translate((float)translate.x, (float)translate.y, (float)translate.z);
        scene.addPolygon(polygon);
        Face.clearMaterialMap();
    }

    // Updated RenderObj method
    public static void RenderObj(Scene scene, String objPath, String mtlPath, Material material) {
        // Load materials first
        if (mtlPath != null) {
            MTLReader.readMTL(mtlPath);
        }

        // Read OBJ data
        ObjData objData = readObjData(objPath);
        if (objData == null) return;

        // Create polygon from data
        Polygon polygon = material != null ? createPolygonFromObjData(objData, material) : createPolygonFromObjData(objData, null);
        if (polygon != null) {
            //polygon.rotate(0, 90, 0);
            //polygon.scale(10,10,10);
            scene.addPolygon(polygon);
        }

        // Clean up
        Face.clearMaterialMap();
    }

    public static void RenderObj(Scene scene, String objPath, Material material) { Obj.RenderObj(scene, objPath, null, material);}

    // First method: Reads OBJ file and collects data
    public static ObjData readObjData(String filePath) {
        ObjData objData = new ObjData();
        Pattern vertex = Pattern.compile("^v[^a-zA-Z]*");
        Pattern texture = Pattern.compile("^vt[^a-zA-Z]*");
        Pattern normal = Pattern.compile("^vn[^a-zA-Z]*");
        Pattern face = Pattern.compile("^f[^a-zA-Z]*");
        Pattern mtl = Pattern.compile("^usemtl\\s+.+");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.matches(vertex.pattern())) {
                    Double[] vertexData = FileRead.normalizeDouble(line);
                    if (vertexData != null) objData.vertices.add(vertexData);
                }
                else if (line.matches(texture.pattern())) {
                    Double[] texCoord = FileRead.normalizeDouble(line);
                    if (texCoord != null) objData.texCoords.add(texCoord);
                }
                else if (line.matches(normal.pattern())) {
                    Double[] normalVec = FileRead.normalizeDouble(line);
                    if (normalVec != null) objData.normals.add(normalVec);
                }
                else if (line.matches(mtl.pattern())) {
                    objData.currentMaterial = line.split("\\s+")[1];
                    System.out.println("Material: " + objData.currentMaterial);
                }
                else if (line.matches(face.pattern())) {
                    Integer[][] faceData = FileRead.normalizeInteger(line);
                    if (faceData != null) {
                        objData.faces.add(new FaceData(
                                faceData[0],
                                faceData[1],
                                faceData[2],
                                objData.currentMaterial
                        ));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return objData;
    }

    // Second method: Creates triangles from collected data
    // In your Obj class:

    // Modified createPolygonFromObjData method
    public static Polygon createPolygonFromObjData(ObjData objData, Material mat) {
        if (objData == null) return null;

        ArrayList<Triangle> triangles = new ArrayList<>();

        for (FaceData face : objData.faces) {
            Material material = mat != null ? mat : Material.findByName(face.materialName);
            if (material == null) {
                System.err.println("Warning: Material not found - " + face.materialName);
                material = Material.ALBEDO(Color.MAGENTA); // Default material
            }

            // Handle both triangles and quads
            int numTriangles = face.vertexIndices.length == 3 ? 1 : 2;

            for (int i = 0; i < numTriangles; i++) {
                int[] triVertexIndices;
                int[] triTexIndices;
                int[] triNormalIndices;

                if (numTriangles == 1) {
                    // Single triangle
                    triVertexIndices = new int[]{face.vertexIndices[0], face.vertexIndices[1], face.vertexIndices[2]};
                    triTexIndices = new int[]{face.texIndices[0], face.texIndices[1], face.texIndices[2]};
                    triNormalIndices = new int[]{face.normalIndices[0], face.normalIndices[1], face.normalIndices[2]};
                } else {
                    // Quad - split into two triangles
                    if (i == 0) {
                        triVertexIndices = new int[]{face.vertexIndices[0], face.vertexIndices[1], face.vertexIndices[2]};
                        triTexIndices = new int[]{face.texIndices[0], face.texIndices[1], face.texIndices[2]};
                        triNormalIndices = new int[]{face.normalIndices[0], face.normalIndices[1], face.normalIndices[2]};
                    } else {
                        triVertexIndices = new int[]{face.vertexIndices[0], face.vertexIndices[2], face.vertexIndices[3]};
                        triTexIndices = new int[]{face.texIndices[0], face.texIndices[2], face.texIndices[3]};
                        triNormalIndices = new int[]{face.normalIndices[0], face.normalIndices[2], face.normalIndices[3]};
                    }
                }

                // Always create triangle with texture coordinates if available
                Triangle triangle = createTriangle(
                        objData.vertices,
                        objData.texCoords,
                        objData.normals,
                        triVertexIndices,
                        triTexIndices,
                        triNormalIndices,
                        material
                );

                if (triangle != null) {
                    triangles.add(triangle);
                }
            }
        }
        return new Polygon(triangles);
    }

    // Modified createTriangle method

    private static Triangle createTriangle(
            ArrayList<Double[]> vertices,
            ArrayList<Double[]> texCoords,
            ArrayList<Double[]> normals,
            int[] vertexIndices,
            int[] texIndices,
            int[] normalIndices,
            Material material
    ) {
        Vector3D[] triVertices = new Vector3D[3];
        Vector3D[] triTexCoords = new Vector3D[3];
        Vector3D[] triNormals = new Vector3D[3];

        boolean hasTexCoords = !texCoords.isEmpty() && texIndices[0] > 0;
        boolean hasNormals = !normals.isEmpty() && normalIndices[0] > 0;

        for (int j = 0; j < 3; j++) {
            // Vertices (required)
            triVertices[j] = new Vector3D(
                    vertices.get(vertexIndices[j] - 1)[0],
                    vertices.get(vertexIndices[j] - 1)[1],
                    vertices.get(vertexIndices[j] - 1)[2]
            );

            // Texture coordinates (if available)
            if (hasTexCoords) {
                Double[] tex = texCoords.get(texIndices[j] - 1);
                triTexCoords[j] = new Vector3D(tex[0], tex[1], tex.length > 2 ? tex[2] : 0);
            }

            // Normals (if available)
            if (hasNormals) {
                Double[] norm = normals.get(normalIndices[j] - 1);
                triNormals[j] = new Vector3D(norm[0], norm[1], norm[2]);
            }
        }

        // Create triangle based on available data
        if (hasTexCoords && material != null && material.getTextureMap() != null) {
            return new Triangle(
                    triVertices[0], triVertices[1], triVertices[2],
                    hasNormals ? triNormals[0] : null,
                    hasNormals ? triNormals[1] : null,
                    hasNormals ? triNormals[2] : null,
                    triTexCoords[0], triTexCoords[1], triTexCoords[2],
                    null, // parent polygon
                    material
            );
        } else if (hasNormals) {
            if(material != null){
                return new Triangle(
                        triVertices[0], triVertices[1], triVertices[2],
                        triNormals[0], triNormals[1], triNormals[2],null, material);
            }else{
                return new Triangle(
                        triVertices[0], triVertices[1], triVertices[2],
                        triNormals[0], triNormals[1], triNormals[2], Scene.background,0, 0, null
                );
            }

        } else {
            return new Triangle(
                    triVertices[0], triVertices[1], triVertices[2],
                    material != null ? material.getColor() : Scene.background,
                    0, 0
            );
        }
    }


    // Data classes to hold intermediate OBJ data
    private static class ObjData {
        ArrayList<Double[]> vertices = new ArrayList<>();
        ArrayList<Double[]> texCoords = new ArrayList<>();
        ArrayList<Double[]> normals = new ArrayList<>();
        ArrayList<FaceData> faces = new ArrayList<>();
        String currentMaterial;
    }

    private static class FaceData {
        Integer[] vertexIndices;
        Integer[] texIndices;
        Integer[] normalIndices;
        String materialName;

        FaceData(Integer[] vertexIndices, Integer[] texIndices, Integer[] normalIndices, String materialName) {
            this.vertexIndices = vertexIndices;
            this.texIndices = texIndices;
            this.normalIndices = normalIndices;
            this.materialName = materialName;
        }
    }

}
