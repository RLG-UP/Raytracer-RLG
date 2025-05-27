package edu.up.isgc.raytracer.shapes.models;

import edu.up.isgc.raytracer.lighting.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a collection of faces in a 3D model, storing vertex indices,
 * texture coordinates, normal vectors, and associated materials.
 * <p>
 * Faces are stored as arrays of indices referencing vertices, texture coordinates,
 * and normals. Materials can be mapped to faces via an index-to-material map.
 * </p>
 */
public class Face {
    /** List of vertex index arrays for faces. */
    private static ArrayList<Integer[]> faces = new ArrayList<>();
    /** List of texture coordinate index arrays for faces. */
    private static ArrayList<Integer[]> faceTextures = new ArrayList<>();
    /** List of normal vector index arrays for faces. */
    private static ArrayList<Integer[]> faceNormals = new ArrayList<>();
    /** List of all face components (vertices, textures, normals) grouped together. */
    private static ArrayList<Integer[][]> wholeFaces = new ArrayList<>();
    /** Map linking face indices to their corresponding materials. */
    private static Map<Integer, Material> materialMap = new HashMap<>();

    /**
     * Constructs a Face object by adding the provided face data to the static collections.
     *
     * @param face a 2D Integer array representing vertex indices, texture indices, and normal indices.
     */
    public Face(Integer[][] face){
        Face.addFace(face);
    }

    /**
     * Adds a face to the static collections.
     *
     * @param face a 2D Integer array where:
     *             - face[0] contains vertex indices,
     *             - face[1] contains texture coordinate indices,
     *             - face[2] contains normal vector indices.
     */
    public static void addFace(Integer[][] face){
        faces.add(face[0]);
        faceTextures.add(face[1]);
        faceNormals.add(face[2]);
        wholeFaces.add(new Integer[][] {face[0], face[1], face[2]});
    }

    /**
     * Clears all stored face data, including vertices, textures, normals, and whole faces.
     */
    public static void clear() {
        faces.clear();
        faceTextures.clear();
        faceNormals.clear();
        wholeFaces.clear();
    }

    /**
     * Clears the material mapping of faces.
     */
    public static void clearMaterialMap() {
        materialMap.clear();
    }

    /**
     * Returns the list of whole faces (vertices, textures, normals combined).
     *
     * @return the list of all faces grouped as Integer[][] arrays.
     */
    public static ArrayList<Integer[][]> getWholeFaces() {
        return Face.wholeFaces;
    }

    /**
     * Returns the list of vertex index arrays for faces.
     *
     * @return the list of vertex indices.
     */
    public static ArrayList<Integer[]> getFaces(){
        return Face.faces;
    }

    /**
     * Returns the list of texture coordinate index arrays for faces.
     *
     * @return the list of texture coordinate indices.
     */
    public static ArrayList<Integer[]> getFaceTextures(){
        return Face.faceTextures;
    }

    /**
     * Returns the list of normal vector index arrays for faces.
     *
     * @return the list of normal vector indices.
     */
    public static ArrayList<Integer[]> getFaceNormals(){
        return Face.faceNormals;
    }

    /**
     * Returns the map of face indices to their associated materials.
     *
     * @return the material map.
     */
    public static Map<Integer, Material> getMaterialMap() {
        return materialMap;
    }

    /**
     * Sets the material map associating face indices with materials.
     *
     * @param materialMap the map to set.
     */
    public static void setMaterialMap(Map<Integer, Material> materialMap) {
        Face.materialMap = materialMap;
    }

    /**
     * Finds the material associated with a given index.
     *
     * @param index the index of the face/material.
     * @return the Material assigned to the index, or null if none found.
     */
    public static Material findMaterialByIndex(int index){
        return Face.getMaterialMap().get(index);
    }
}
