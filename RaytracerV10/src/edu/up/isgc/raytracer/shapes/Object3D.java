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
 * Abstract base class for all 3D objects in the raytracing scene.
 * Provides common properties and requires concrete implementations
 * to define object-specific intersection and lighting calculations.
 * Supports both simple colored objects and materials with advanced properties.
 */
public abstract class Object3D {
    /** The base color of the object, used when no material is specified */
    public Color color;
    /** Index of refraction for transparent materials */
    public double refraction;
    /** Transparency level (0 = opaque, 1 = fully transparent) */
    public double transparency;
    private Material material;
    private boolean hasMaterial = false;

    /**
     * Constructs a 3D object with basic optical properties.
     *
     * @param color The base color of the object
     * @param refraction The index of refraction for the material
     * @param transparency The transparency level (0-1)
     */
    public Object3D(Color color, double refraction, double transparency) {
        this.color = color;
        this.refraction = refraction;
        this.transparency = transparency;
    }

    /**
     * Constructs a 3D object with a complete material definition.
     *
     * @param material The material containing all surface properties
     */
    public Object3D(Material material) {
        this.color = material.getColor();
        this.setMaterial(material);
        this.setHasMaterial(true);
        this.refraction = material.getRefraction();
        this.transparency = material.getTransparency();
    }

    /**
     * Calculates intersections between this object and a ray.
     * Must be implemented by concrete subclasses.
     *
     * @param ray The ray to test for intersection
     * @return Array of Intersection objects, or null if no intersection occurs
     */
    public abstract Intersection[] intersect(Ray ray);

    /**
     * Returns the type identifier of the object.
     * Must be implemented by concrete subclasses.
     *
     * @return String representing the object type
     */
    public abstract String type();

    /**
     * Calculates lighting contribution at an intersection point.
     * Must be implemented by concrete subclasses.
     *
     * @param intersection The intersection data including position and normal
     * @return The calculated color with lighting applied
     */
    public abstract Color addLight(Intersection intersection);

    /**
     * Returns a zero-state instance of the object.
     * Must be implemented by concrete subclasses.
     *
     * @return A default instance of the object
     */
    public abstract Object3D returnZero();

    /**
     * Gets the base color of the object.
     *
     * @return The object's color
     */
    public Color getColor() { return color; }

    /**
     * Checks if this object casts a shadow from a light source.
     *
     * @param point The point being shaded
     * @param lightDirection Direction to the light source
     * @return Intersection data if shadow ray hits another object, null otherwise
     */
    public Intersection castShadow(Vector3D point, Vector3D lightDirection) {
        Vector3D offsetPoint = point.add(lightDirection.normalize().scale(Camera.getEpsilon()));
        Ray shadowRay = new Ray(offsetPoint, lightDirection.normalize());
        return Scene.findRayIntersection(shadowRay, this);
    }

    /**
     * Gets the bounding box for this object.
     * Must be implemented by concrete subclasses.
     *
     * @return The object's bounding box
     */
    public abstract BoundingBox getBB();

    /**
     * Gets the material assigned to this object.
     *
     * @return The object's material, or null if not set
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material for this object.
     *
     * @param material The material to assign
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Checks if this object has a material assigned.
     *
     * @return true if the object has a material, false otherwise
     */
    public boolean getHasMaterial() {
        return hasMaterial;
    }

    /**
     * Sets whether this object has a material assigned.
     *
     * @param hasMaterial Flag indicating material presence
     */
    public void setHasMaterial(boolean hasMaterial) {
        this.hasMaterial = hasMaterial;
    }
}