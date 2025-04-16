package edu.up.isgc.raytracer.shapes.models;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.files.FileRead;
import edu.up.isgc.raytracer.shapes.Object3D;
import edu.up.isgc.raytracer.shapes.Triangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Polygon {
    private ArrayList<Triangle> shape;
    private Color color;

    public Polygon(ArrayList<Triangle> shape){
        this.setShape(shape);
    }

    public Polygon(String filePath, Color color){
        FileRead.compileObj(filePath);
        ArrayList<Triangle> shape = new ArrayList<>();

        //System.out.println("Total vertices: " + Vertex.getVertexes().size());
        //System.out.println("Total faces: " + Face.getFaces().size());

        for(Integer[] face : Face.getFaces()){
            if(face != null) {
                try {
                    int index1 = face[0] - 1;
                    int index2 = face[1] - 1;
                    int index3 = face[2] - 1;

                    // Debug output
                    //System.out.println("Processing face with indices: " + index1 + ", " + index2 + ", " + index3);

                    Double[] v1 = Vertex.getVertexes().get(index1);
                    Double[] v2 = Vertex.getVertexes().get(index2);
                    Double[] v3 = Vertex.getVertexes().get(index3);

                    shape.add(new Triangle(
                            new Vector3D(v1[0], v1[1], v1[2]),
                            new Vector3D(v2[0], v2[1], v2[2]),
                            new Vector3D(v3[0], v3[1], v3[2]),
                            color
                    ));
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
}

