package edu.up.isgc.raytracer.shapes.models;

import java.util.ArrayList;

public class Face {
    private static ArrayList<Integer[]> faces = new ArrayList<>();
    public Face (Integer a, Integer b, Integer c){
        Face.addFace(new Integer[]{a,b,c});
    }

    public static void addFace(Integer[] face){ faces.add(face); }
    public static ArrayList<Integer[]> getFaces(){ return Face.faces; }
}
