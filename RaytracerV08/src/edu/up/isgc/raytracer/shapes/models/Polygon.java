package edu.up.isgc.raytracer.shapes.models;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.files.FileRead;
import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.shapes.Object3D;
import edu.up.isgc.raytracer.shapes.Triangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Polygon {
    private ArrayList<Triangle> shape;
    private ArrayList<Double[]> vertexes = new ArrayList<>();
    private ArrayList<Double[]> normalVertexes = new ArrayList<>();
    private ArrayList<Double[]> textures = new ArrayList<>();

    private double refraction;

    private BufferedImage textureMap;

    private Color color;
    private Vector3D position = Vector3D.getZero();

    public Polygon(ArrayList<Triangle> shape){
        this.color = Color.magenta;
        this.setShape(shape);
        for(Triangle t : shape){
            t.setParent(this);
        }
    }

    public Polygon(String filePath, Material material){
        FileRead.compileObj(filePath);
        ArrayList<Triangle> shape = new ArrayList<>();

        //System.out.println("Total vertices: " + Vertex.getVertexes().size());
        //System.out.println("Total faces: " + Face.getFaces().size());
        int faceCount = 0;

        for(Integer[][] face : Face.getWholeFaces()){
            if(face != null) {
                try {
                    int coord1 = face[0][0] - 1;
                    int coord2 = face[0][1] - 1;
                    int coord3 = face[0][2] - 1;
                    int coord4 = face[0].length==4? face[0][3]-1 : -1;

                    int texture1 = face[1][0] - 1;
                    int texture2 = face[1][1] - 1;
                    int texture3 = face[1][2] - 1;
                    int texture4 = face[0].length==4? face[0][3]-1 : -1;

                    int normal1 = face[2][0] - 1;
                    int normal2 = face[2][1] - 1;
                    int normal3 = face[2][2] - 1;
                    int normal4 = face[0].length==4? face[0][3]-1 : -1;

                    // Debug output
                    //System.out.println("Processing face with indices: " + index1 + ", " + index2 + ", " + index3);

                    Double[] v1 = Vertex.getVertexes().get(coord1);
                    Double[] v2 = Vertex.getVertexes().get(coord2);
                    Double[] v3 = Vertex.getVertexes().get(coord3);

                    if(!Texture.getTexture().isEmpty()) {
                        if(!NormalVertex.getNormalVertexes().isEmpty()) {
                            Double[] nV1 = NormalVertex.getNormalVertexes().get(normal1);
                            Double[] nV2 = NormalVertex.getNormalVertexes().get(normal2);
                            Double[] nV3 = NormalVertex.getNormalVertexes().get(normal3);

                            //System.out.println("Face: " + faceCount + " // Material: " + Face.findMaterialByIndex(faceCount).getName());
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    new Vector3D(nV1[0], nV1[1], nV1[2]),
                                    new Vector3D(nV2[0], nV2[1], nV2[2]),
                                    new Vector3D(nV3[0], nV3[1], nV3[2]),
                                    this,
                                    material
                            ));
                            if(coord4 != -1){
                                Double[] v4 = Vertex.getVertexes().get(coord4);
                                Double[] nV4 = NormalVertex.getNormalVertexes().get(normal4);
                                shape.add(new Triangle(
                                        new Vector3D(v1[0], v1[1], v1[2]),
                                        new Vector3D(v3[0], v3[1], v3[2]),
                                        new Vector3D(v4[0], v4[1], v4[2]),
                                        new Vector3D(nV1[0], nV1[1], nV1[2]),
                                        new Vector3D(nV3[0], nV3[1], nV3[2]),
                                        new Vector3D(nV4[0], nV4[1], nV4[2]),
                                        this,
                                        material
                                ));
                            }
                        }
                    }else{

                        if (!NormalVertex.getNormalVertexes().isEmpty()) {
                            Double[] nV1 = NormalVertex.getNormalVertexes().get(normal1);
                            Double[] nV2 = NormalVertex.getNormalVertexes().get(normal2);
                            Double[] nV3 = NormalVertex.getNormalVertexes().get(normal3);
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    new Vector3D(nV1[0], nV1[1], nV1[2]),
                                    new Vector3D(nV2[0], nV2[1], nV2[2]),
                                    new Vector3D(nV3[0], nV3[1], nV3[2]),
                                    this,
                                    material
                            ));
                            if(coord4 != -1){
                                Double[] v4 = Vertex.getVertexes().get(coord4);
                                Double[] nV4 = NormalVertex.getNormalVertexes().get(normal4);
                                shape.add(new Triangle(
                                        new Vector3D(v1[0], v1[1], v1[2]),
                                        new Vector3D(v3[0], v3[1], v3[2]),
                                        new Vector3D(v4[0], v4[1], v4[2]),
                                        new Vector3D(nV1[0], nV1[1], nV1[2]),
                                        new Vector3D(nV3[0], nV3[1], nV3[2]),
                                        new Vector3D(nV4[0], nV4[1], nV4[2]),
                                        this,
                                        material
                                ));
                            }
                        } else {
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    color,
                                    0,
                                    0
                            ));
                        }
                    }
                    faceCount++;

                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Invalid vertex index in face: " +
                            Arrays.toString(face));
                    throw e;
                }
            }
        }
        this.setShape(shape);
    }

    public Polygon(String filePath, Color color, double refraction, double transparency){
        FileRead.compileObj(filePath);
        ArrayList<Triangle> shape = new ArrayList<>();

        //System.out.println("Total vertices: " + Vertex.getVertexes().size());
        //System.out.println("Total faces: " + Face.getFaces().size());
        int faceCount = 0;

        for(Integer[][] face : Face.getWholeFaces()){
            if(face != null) {
                try {
                    int coord1 = face[0][0] - 1;
                    int coord2 = face[0][1] - 1;
                    int coord3 = face[0][2] - 1;

                    int texture1 = face[1][0] - 1;
                    int texture2 = face[1][1] - 1;
                    int texture3 = face[1][2] - 1;

                    int normal1 = face[2][0] - 1;
                    int normal2 = face[2][1] - 1;
                    int normal3 = face[2][2] - 1;

                    // Debug output
                    //System.out.println("Processing face with indices: " + index1 + ", " + index2 + ", " + index3);

                    Double[] v1 = Vertex.getVertexes().get(coord1);
                    Double[] v2 = Vertex.getVertexes().get(coord2);
                    Double[] v3 = Vertex.getVertexes().get(coord3);

                    if(!Texture.getTexture().isEmpty()) {
                        Double[] t1 = Texture.getTexture().get(texture1);
                        Double[] t2 = Texture.getTexture().get(texture2);
                        Double[] t3 = Texture.getTexture().get(texture3);
                        /*
                        if(!NormalVertex.getNormalVertexes().isEmpty()) {
                            Double[] nV1 = NormalVertex.getNormalVertexes().get(normal1);
                            Double[] nV2 = NormalVertex.getNormalVertexes().get(normal2);
                            Double[] nV3 = NormalVertex.getNormalVertexes().get(normal3);
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    new Vector3D(nV1[0], nV1[1], nV1[2]),
                                    new Vector3D(nV2[0], nV2[1], nV2[2]),
                                    new Vector3D(nV3[0], nV3[1], nV3[2]),
                                    new Vector3D(t1[0], t1[1], t1[2]),
                                    new Vector3D(t2[0], t2[1], t2[2]),
                                    new Vector3D(t3[0], t3[1], t3[2]),
                                    color,
                                    refraction,
                                    transparency,
                                    this
                            ));
                        }

                         */
                        if(!NormalVertex.getNormalVertexes().isEmpty()) {
                            Double[] nV1 = NormalVertex.getNormalVertexes().get(normal1);
                            Double[] nV2 = NormalVertex.getNormalVertexes().get(normal2);
                            Double[] nV3 = NormalVertex.getNormalVertexes().get(normal3);


                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    new Vector3D(nV1[0], nV1[1], nV1[2]),
                                    new Vector3D(nV2[0], nV2[1], nV2[2]),
                                    new Vector3D(nV3[0], nV3[1], nV3[2]),
                                    new Vector3D(t1[0], t1[1], t1[2]),
                                    new Vector3D(t2[0], t2[1], t2[2]),
                                    new Vector3D(t3[0], t3[1], t3[2]),
                                    this,
                                    Face.findMaterialByIndex(faceCount)
                            ));
                        }
                    }else{

                        if (!NormalVertex.getNormalVertexes().isEmpty()) {
                            Double[] nV1 = NormalVertex.getNormalVertexes().get(normal1);
                            Double[] nV2 = NormalVertex.getNormalVertexes().get(normal2);
                            Double[] nV3 = NormalVertex.getNormalVertexes().get(normal3);
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    new Vector3D(nV1[0], nV1[1], nV1[2]),
                                    new Vector3D(nV2[0], nV2[1], nV2[2]),
                                    new Vector3D(nV3[0], nV3[1], nV3[2]),
                                    color,
                                    refraction,
                                    transparency,
                                    this
                            ));
                        } else {
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    color,
                                    refraction,
                                    transparency
                            ));
                        }
                    }

                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Invalid vertex index in face: " +
                            Arrays.toString(face));
                    throw e;
                }
            }
            faceCount++;
        }
        this.setShape(shape);
    }

    public Polygon(String filePath, Color color, double refraction, double transparency, BufferedImage textureMap){
        FileRead.compileObj(filePath);
        ArrayList<Triangle> shape = new ArrayList<>();
        this.setTextureMap(textureMap);

        //System.out.println("Total vertices: " + Vertex.getVertexes().size());
        //System.out.println("Total faces: " + Face.getFaces().size());

        for(Integer[][] face : Face.getWholeFaces()){
            if(face != null) {
                try {
                    int coord1 = face[0][0] - 1;
                    int coord2 = face[0][1] - 1;
                    int coord3 = face[0][2] - 1;

                    int texture1 = face[1][0] - 1;
                    int texture2 = face[1][1] - 1;
                    int texture3 = face[1][2] - 1;

                    int normal1 = face[2][0] - 1;
                    int normal2 = face[2][1] - 1;
                    int normal3 = face[2][2] - 1;

                    // Debug output
                    //System.out.println("Processing face with indices: " + index1 + ", " + index2 + ", " + index3);

                    Double[] v1 = Vertex.getVertexes().get(coord1);
                    Double[] v2 = Vertex.getVertexes().get(coord2);
                    Double[] v3 = Vertex.getVertexes().get(coord3);

                    if(!Texture.getTexture().isEmpty()) {
                        Double[] t1 = Texture.getTexture().get(texture1);
                        Double[] t2 = Texture.getTexture().get(texture2);
                        Double[] t3 = Texture.getTexture().get(texture3);

                        if(!NormalVertex.getNormalVertexes().isEmpty()) {
                            Double[] nV1 = NormalVertex.getNormalVertexes().get(normal1);
                            Double[] nV2 = NormalVertex.getNormalVertexes().get(normal2);
                            Double[] nV3 = NormalVertex.getNormalVertexes().get(normal3);
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    new Vector3D(nV1[0], nV1[1], nV1[2]),
                                    new Vector3D(nV2[0], nV2[1], nV2[2]),
                                    new Vector3D(nV3[0], nV3[1], nV3[2]),
                                    new Vector3D(t1[0], t1[1], t1[2]),
                                    new Vector3D(t2[0], t2[1], t2[2]),
                                    new Vector3D(t3[0], t3[1], t3[2]),
                                    color,
                                    refraction,
                                    transparency,
                                    this
                            ));
                        }
                    }else{

                        if (!NormalVertex.getNormalVertexes().isEmpty()) {
                            Double[] nV1 = NormalVertex.getNormalVertexes().get(normal1);
                            Double[] nV2 = NormalVertex.getNormalVertexes().get(normal2);
                            Double[] nV3 = NormalVertex.getNormalVertexes().get(normal3);
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    new Vector3D(nV1[0], nV1[1], nV1[2]),
                                    new Vector3D(nV2[0], nV2[1], nV2[2]),
                                    new Vector3D(nV3[0], nV3[1], nV3[2]),
                                    color,
                                    refraction,
                                    transparency,
                                    this
                            ));
                        } else {
                            shape.add(new Triangle(
                                    new Vector3D(v1[0], v1[1], v1[2]),
                                    new Vector3D(v2[0], v2[1], v2[2]),
                                    new Vector3D(v3[0], v3[1], v3[2]),
                                    color,
                                    refraction,
                                    transparency
                            ));
                        }
                    }

                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Invalid vertex index in face: " +
                            Arrays.toString(face));
                    throw e;
                }
            }
        }
        this.setShape(shape);
    }



    public ArrayList<Triangle> getShape() {
        return shape;
    }

    public void setShape(ArrayList<Triangle> shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public void translate(float tX, float tY, float tZ){
        for(Triangle t : shape){
            t.getA().translate(tX, tY, tZ);
            t.getB().translate(tX, tY, tZ);
            t.getC().translate(tX, tY, tZ);

            this.getPosition().translate(tX, tY, tZ);
        }
    }

    public void scale(float sX, float sY, float sZ){
        for(Triangle t : shape){
            t.getA().scale(sX, sY, sZ);
            t.getB().scale(sX, sY, sZ);
            t.getC().scale(sX, sY, sZ);

            float nSX = 1/sX, nSY = 1/sY, nSZ = 1/sZ;
            t.getnA().scale(nSX, nSY, nSZ);
            t.getnB().scale(nSX, nSY, nSZ);
            t.getnC().scale(nSX, nSY, nSZ);

            t.setnA(t.getnA().normalize());
            t.setnB(t.getnB().normalize());
            t.setnC(t.getnC().normalize());

            /*
            t.getTA().scale(sX, sY, sZ);
            t.getTB().scale(sX, sY, sZ);
            t.getTC().scale(sX, sY, sZ);

             */

            /*
            t.getTA().scale(nSX, nSY, nSZ);
            t.getTB().scale(nSX, nSY, nSZ);
            t.getTC().scale(nSX, nSY, sZ);
            */
        }
    }

    public void rotateInPlace(float rX, float rY, float rZ, boolean inRadians){
        float x = (float) this.getPosition().x;
        float y = (float) this.getPosition().y;
        float z = (float) this.getPosition().z;

        for(Triangle t : shape){

            t.getA().translate(-x, -y, -z);
            t.getB().translate(-x, -y, -z);
            t.getC().translate(-x, -y, -z);

            t.getA().rotate(rX, rY, rZ, inRadians);
            t.getB().rotate(rX, rY, rZ, inRadians);
            t.getC().rotate(rX, rY, rZ, inRadians);

            t.getnA().rotate(rX, rY, rZ);
            t.getnB().rotate(rX, rY, rZ);
            t.getnC().rotate(rX, rY, rZ);

            t.setnA(t.getnA().normalize());
            t.setnB(t.getnB().normalize());
            t.setnC(t.getnC().normalize());

            t.getA().translate(x, y, z);
            t.getB().translate(x, y, z);
            t.getC().translate(x, y, z);
        }
    }

    public void rotateInPlace(float rX, float rY, float rZ){ this.rotateInPlace(rX, rY, rZ, false); }

    public void rotate(float rX, float rY, float rZ, boolean inRadians){
        for(Triangle t : shape){
            t.getA().rotate(rX, rY, rZ, inRadians);
            t.getB().rotate(rX, rY, rZ, inRadians);
            t.getC().rotate(rX, rY, rZ, inRadians);

            t.getnA().rotate(rX, rY, rZ, inRadians);
            t.getnB().rotate(rX, rY, rZ, inRadians);
            t.getnC().rotate(rX, rY, rZ, inRadians);

            t.setnA(t.getnA().normalize());
            t.setnB(t.getnB().normalize());
            t.setnC(t.getnC().normalize());

            /*
            t.getTA().rotate(rX, rY, rZ, inRadians);
            t.getTB().rotate(rX, rY, rZ, inRadians);
            t.getTC().rotate(rX, rY, rZ, inRadians);
             */
        }
    }

    public void rotate(float rX, float rY, float rZ){ this.rotate(rX, rY, rZ, false); }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void addNormalVertex(Double[] vertex){ this.normalVertexes.add(vertex); }
    public ArrayList<Double[]> getNormalVertexes(){ return this.normalVertexes; }

    public void addTexture(Double[] vertex){ this.textures.add(vertex); }
    public ArrayList<Double[]> getTexture(){ return this.textures; }

    public void addVertex(Double[] vertex){ this.vertexes.add(vertex); }
    public ArrayList<Double[]> getVertexes(){ return this.vertexes; }

    public BufferedImage getTextureMap() {
        return textureMap;
    }

    public void setTextureMap(BufferedImage textureMap) {
        this.textureMap = textureMap;
    }
}

