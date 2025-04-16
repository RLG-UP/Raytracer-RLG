package edu.up.isgc.raytracer.shapes.models;

import java.util.ArrayList;

public class Vertex {
    private static ArrayList<Double[]> vertexes = new ArrayList<>();
    public Vertex (Double a, Double b, Double c){
        Vertex.addVertex(new Double[]{a,b,c});
    }

    public static void addVertex(Double[] vertex){ vertexes.add(vertex); }
    public static ArrayList<Double[]> getVertexes(){ return Vertex.vertexes; }
}
