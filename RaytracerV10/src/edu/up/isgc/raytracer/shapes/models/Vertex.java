package edu.up.isgc.raytracer.shapes.models;

import java.util.ArrayList;

/**
 * The Vertex class manages 3D vertex coordinates for geometric shapes.
 * It provides static methods to store, retrieve, and clear vertex data
 * in a centralized location for the raytracer.
 */
public class Vertex {
    private static ArrayList<Double[]> vertexes = new ArrayList<>();

    /**
     * Constructs a new Vertex with the specified 3D coordinates.
     *
     * @param a The x-coordinate of the vertex
     * @param b The y-coordinate of the vertex
     * @param c The z-coordinate of the vertex
     */
    public Vertex(Double a, Double b, Double c) {
        Vertex.addVertex(new Double[]{a, b, c});
    }

    /**
     * Clears all stored vertex data.
     * This is typically called when loading a new model to prevent
     * mixing vertex data from different objects.
     */
    public static void clear() {
        vertexes.clear();
    }

    /**
     * Adds a vertex to the vertex collection.
     *
     * @param vertex The vertex coordinates as a Double array of length 3 (x,y,z)
     * @throws IllegalArgumentException if vertex array doesn't contain exactly 3 elements
     */
    public static void addVertex(Double[] vertex) {
        vertexes.add(vertex);
    }

    /**
     * Retrieves all stored vertex coordinates.
     *
     * @return An ArrayList containing all vertex coordinates, where each vertex
     *         is represented as a Double array of length 3 (x,y,z)
     */
    public static ArrayList<Double[]> getVertexes() {
        return Vertex.vertexes;
    }
}