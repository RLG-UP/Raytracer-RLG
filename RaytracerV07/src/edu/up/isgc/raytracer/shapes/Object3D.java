package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.BoundingBox;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.world.Scene;

import java.awt.Color;

/**
 * Abstract base class for all 3D objects in the scene.
 * Defines common properties and requires intersection calculation.
 */
public abstract class Object3D {
    public Color color;  // The base color of the 3D object
    public double refraction, transparency;

    /**
     * Constructs a 3D object with the specified color.
     * @param color The color of the object
     */
    public Object3D(Color color, double refraction, double transparency) {
        this.color = color;
        this.refraction = refraction;
        this.transparency = transparency;
    }

    /**
     * Calculates intersections between this object and a ray.
     * @param ray The ray to test for intersection
     * @return Array of intersections (may contain null values for no intersection)
     */
    public abstract Intersection[] intersect(Ray ray);
    public abstract String type();
    public abstract Color addLight(Vector3D point);
    public abstract Object3D returnZero();

    public Color getColor() { return color; }

    public Intersection castShadow(Vector3D point, Vector3D lightDirection) {
        Vector3D offsetPoint = point.add(lightDirection.normalize().scale(Camera.getEpsilon())); // Avoid self-intersection
        Ray shadowRay = new Ray(offsetPoint, lightDirection.normalize());
        return Scene.findRayIntersection(shadowRay, this);
    }

    public abstract BoundingBox getBB();
}