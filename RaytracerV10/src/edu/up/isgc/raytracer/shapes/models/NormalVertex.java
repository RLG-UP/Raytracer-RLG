package edu.up.isgc.raytracer.shapes.models;

import java.util.ArrayList;

/**
 * Represents normal vectors (normals) used in 3D models.
 * <p>
 * Stores a static collection of normal vectors, each represented as an array of three Doubles
 * corresponding to the x, y, and z components of the normal vector.
 * </p>
 */
public class NormalVertex {
    /** Static list holding all normal vectors as arrays of Doubles (x, y, z). */
    private static ArrayList<Double[]> normalVertexes = new ArrayList<>();

    /**
     * Constructs a NormalVertex by adding the provided normal vector components to the static list.
     *
     * @param a the x-component of the normal vector.
     * @param b the y-component of the normal vector.
     * @param c the z-component of the normal vector.
     */
    public NormalVertex(Double a, Double b, Double c){
        NormalVertex.addNormalVertex(new Double[]{a, b, c});
    }

    /**
     * Clears all stored normal vectors.
     */
    public static void clear() {
        normalVertexes.clear();
    }

    /**
     * Adds a normal vector to the static list.
     *
     * @param vertex an array of three Doubles representing the x, y, and z components of the normal vector.
     */
    public static void addNormalVertex(Double[] vertex){
        normalVertexes.add(vertex);
    }

    /**
     * Returns the list of stored normal vectors.
     *
     * @return an ArrayList of Double arrays, each representing a normal vector.
     */
    public static ArrayList<Double[]> getNormalVertexes(){
        return NormalVertex.normalVertexes;
    }
}
