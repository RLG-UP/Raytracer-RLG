package edu.up.isgc.raytracer.shapes.models;

import java.util.ArrayList;

public class NormalVertex {
    private static ArrayList<Double[]> normalVertexes = new ArrayList<>();
    public NormalVertex (Double a, Double b, Double c){
        NormalVertex.addNormalVertex(new Double[]{a,b,c});
    }

    public static void clear() {
        normalVertexes.clear();
    }

    public static void addNormalVertex(Double[] vertex){ normalVertexes.add(vertex); }
    public static ArrayList<Double[]> getNormalVertexes(){ return NormalVertex.normalVertexes; }
}
