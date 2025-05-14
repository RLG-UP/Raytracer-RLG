package edu.up.isgc.raytracer.shapes.models;

import java.util.ArrayList;

public class Face {
    private static ArrayList<Integer[]> faces = new ArrayList<>();
    private static ArrayList<Integer[]> faceTextures = new ArrayList<>();
    private static ArrayList<Integer[]> faceNormals = new ArrayList<>();
    private static ArrayList<Integer[][]> wholeFaces = new ArrayList<>();
    public Face (Integer[][] face){
        Face.addFace(face);
    }

    public static void addFace(Integer[][] face){
        faces.add(face[0]);
        faceTextures.add(face[1]);
        faceNormals.add(face[2]);
        wholeFaces.add(new Integer[][] {face[0], face[1], face[2]});
    }

    public static void clear() {
        faces.clear();
        faceTextures.clear();
        faceNormals.clear();
        wholeFaces.clear();
    }

    public static ArrayList<Integer[][]> getWholeFaces() { return Face.wholeFaces; }
    public static ArrayList<Integer[]> getFaces(){ return Face.faces; }
    public static ArrayList<Integer[]> getFaceTextures(){ return Face.faceTextures; }
    public static ArrayList<Integer[]> getFaceNormals(){ return Face.faceNormals; }
}
