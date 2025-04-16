package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

import java.awt.Color;

/**
 * Abstract base class for all 3D objects in the scene.
 * Defines common properties and requires intersection calculation.
 */
public abstract class Object3D {
    public Color color;  // The base color of the 3D object

    /**
     * Constructs a 3D object with the specified color.
     * @param color The color of the object
     */
    public Object3D(Color color) {
        this.color = color;
    }

    /**
     * Calculates intersections between this object and a ray.
     * @param ray The ray to test for intersection
     * @return Array of intersections (may contain null values for no intersection)
     */
    public abstract Intersection[] intersect(Ray ray);
}