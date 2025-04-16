package edu.up.isgc.raytracer;

import java.awt.Color;

/**
 * Represents an intersection point between a ray and a 3D object.
 * Stores information about the intersection point, distance, and object color.
 */
public class Intersection {
    public Vector3D point;     // The 3D coordinates of the intersection point
    public double distance;    // Distance from ray origin to intersection point
    public Color color;        // Color of the intersected object at this point

    /**
     * Constructs an intersection with the specified properties.
     * @param point The 3D coordinates of the intersection
     * @param distance Distance from ray origin to intersection
     * @param color Color of the intersected object
     */
    public Intersection(Vector3D point, double distance, Color color) {
        this.point = point;
        this.distance = distance;
        this.color = color;
    }
}