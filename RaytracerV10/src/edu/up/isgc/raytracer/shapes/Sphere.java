package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.optimization.BoundingBox;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

import java.awt.Color;

/**
 * Represents a sphere in 3D space, implementing ray-sphere intersection
 * calculations and lighting effects. Extends the Object3D base class.
 */
public class Sphere extends Object3D {
    /** The center point of the sphere in 3D space */
    public Vector3D center;
    /** The radius of the sphere */
    public double radius;

    /**
     * Constructs a sphere with basic optical properties.
     *
     * @param center The center point of the sphere
     * @param radius The radius of the sphere
     * @param color The base color of the sphere
     * @param refraction The index of refraction for the material
     * @param transparency The transparency level (0 = opaque, 1 = fully transparent)
     */
    public Sphere(Vector3D center, double radius, Color color, double refraction, double transparency) {
        super(color, refraction, transparency);
        this.center = center;
        this.radius = radius;
    }

    /**
     * Constructs a sphere with complete material properties.
     *
     * @param center The center point of the sphere
     * @param radius The radius of the sphere
     * @param material The material defining the sphere's surface properties
     */
    public Sphere(Vector3D center, double radius, Material material) {
        super(material);
        this.center = center;
        this.radius = radius;
        this.setHasMaterial(true);
    }

    /**
     * Calculates intersections between this sphere and a ray.
     * Implements the geometric solution for ray-sphere intersection.
     *
     * @param ray The ray to test for intersection
     * @return An array of Intersection objects (size 2) containing:
     *         - First element: closest intersection point (or null if behind ray origin)
     *         - Second element: farthest intersection point (or null if behind ray origin)
     *         Returns null if no intersection occurs
     */
    public Intersection[] intersect(Ray ray) {
        Vector3D O = ray.origin;
        Vector3D D = ray.direction.normalize().scale(-1);
        Vector3D L = Vector3D.subtract(center, O);
        double tCA = L.dot(D);

        if(tCA < 0) return null;  // Sphere is behind ray origin

        double dSquared = L.dot(L) - (tCA * tCA);
        if(dSquared < 0) return null;  // No intersection

        double tHC = Math.sqrt((radius * radius) - dSquared);
        double t0 = tCA - tHC;
        double t1 = tCA + tHC;

        if(t0 > t1){
            double copy = t0;
            t0 = t1;
            t1 = copy;
        }

        Vector3D A = O.add(D.scale(t0));
        Vector3D B = O.add(D.scale(t1));

        // Verify points lie exactly on sphere surface
        double distA = Vector3D.subtract(A, center).value;
        double distB = Vector3D.subtract(B, center).value;

        Color color = super.getColor();
        if(this.getHasMaterial()){
            color = this.getMaterial().getColor();
        }

        Intersection p0 = new Intersection(A, t0, color, this.normal(A));
        Intersection p1 = new Intersection(B, t1, color, this.normal(B));

        return new Intersection[]{
                t0 > 0 ? p0 : null,
                t1 > 0 ? p1 : null
        };
    }

    /**
     * Calculates the surface normal at a given point on the sphere.
     *
     * @param point The point on the sphere's surface
     * @return The normalized normal vector at the given point
     */
    public Vector3D normal(Vector3D point){
        return Vector3D.subtract(point, center).normalize();
    }

    /**
     * Calculates the lighting contribution at an intersection point.
     *
     * @param intersection The intersection data containing position and normal
     * @return The calculated color with lighting applied
     */
    @Override
    public Color addLight(Intersection intersection) {
        Vector3D N = this.normal(intersection.point);
        return Light.calculateColor(N, intersection.point, this, intersection);
    }

    /**
     * Returns a default zero-state sphere instance.
     *
     * @return A sphere centered at origin with zero radius and null properties
     */
    public Object3D returnZero(){
        return new Sphere(Vector3D.getZero(), 0.0, null, 0, 0);
    }

    /**
     * Generates an axis-aligned bounding box for this sphere.
     *
     * @return A BoundingBox that completely contains the sphere
     */
    @Override
    public BoundingBox getBB(){
        float r = (float)this.radius;
        Vector3D origin = this.center;
        Vector3D min = new Vector3D(origin.x - r, origin.y - r, origin.z - r);
        Vector3D max = new Vector3D(origin.x + r, origin.y + r, origin.z + r);
        return new BoundingBox(min, max);
    }

    /**
     * Returns the type identifier of this object.
     *
     * @return The string "sphere"
     */
    @Override
    public String type(){ return "sphere"; }
}