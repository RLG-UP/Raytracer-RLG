package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.optimization.BoundingBox;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;
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
    private Material material;
    private boolean hasMaterial = false;

    /**
     * Constructs a 3D object with the specified color.
     * @param color The color of the object
     */
    public Object3D(Color color, double refraction, double transparency) {
        this.color = color;
        this.refraction = refraction;
        this.transparency = transparency;
    }

    public Object3D(Material material) {
        this.color = material.getColor();
        this.setMaterial(material);
        this.setHasMaterial(true);
        this.refraction = material.getRefraction();
        this.transparency = material.getTransparency();
        //this.transparency = 0;
    }

    /**
     * Calculates intersections between this object and a ray.
     * @param ray The ray to test for intersection
     * @return Array of intersections (may contain null values for no intersection)
     */
    public abstract Intersection[] intersect(Ray ray);
    public abstract String type();
    //public abstract Color addLight(Vector3D point);
    public abstract Color addLight(Intersection intersection);
    public abstract Object3D returnZero();

    public Color getColor() { return color; }

    public Intersection castShadow(Vector3D point, Vector3D lightDirection) {
        Vector3D offsetPoint = point.add(lightDirection.normalize().scale(Camera.getEpsilon())); // Avoid self-intersection
        Ray shadowRay = new Ray(offsetPoint, lightDirection.normalize());
        return Scene.findRayIntersection(shadowRay, this);
    }

    public abstract BoundingBox getBB();

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean getHasMaterial() {
        return hasMaterial;
    }

    public void setHasMaterial(boolean hasMaterial) {
        this.hasMaterial = hasMaterial;
    }
}