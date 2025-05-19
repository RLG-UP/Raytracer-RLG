package edu.up.isgc.raytracer;

import edu.up.isgc.raytracer.shapes.Object3D;

import java.awt.Color;

/**
 * Represents an intersection point between a ray and a 3D object.
 * Stores information about the intersection point, distance, and object color.
 */
public class Intersection {
    public Vector3D point;     // The 3D coordinates of the intersection point
    public double distance;    // Distance from ray origin to intersection point
    public Color color;        // Color of the intersected object at this point
    public Object3D object;
    private Vector3D normal = Vector3D.getZero();

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

    public Intersection(Vector3D point, double distance, Color color, Vector3D normal) {
        this.point = point;
        this.distance = distance;
        this.color = color;
        this.setNormal(normal);
    }

    public Intersection(Vector3D point, double distance, Color color, Vector3D normal, Object3D object) {
        this.point = point;
        this.distance = distance;
        this.color = color;
        this.setNormal(normal);
        this.object = object;
    }

    public static Intersection[] nullIntersection() { return new Intersection[]{null, null}; }

    public void setNormal(Vector3D normal) { this.normal = normal.normalize(); }
    public Vector3D getNormal() { return this.normal; }
}