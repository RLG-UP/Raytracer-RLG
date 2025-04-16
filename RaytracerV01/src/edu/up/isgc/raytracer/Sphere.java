package edu.up.isgc.raytracer;

import java.awt.Color;

/**
 * Represents a sphere in 3D space, extending the Object3D class.
 * Implements sphere-ray intersection calculations.
 */
public class Sphere extends Object3D {
    public Vector3D center;  // Center point of the sphere
    public double radius;   // Radius of the sphere

    /**
     * Constructs a sphere with specified center, radius, and color.
     * @param center Center point of the sphere
     * @param radius Radius of the sphere
     * @param color Color of the sphere
     */
    public Sphere(Vector3D center, double radius, Color color) {
        super(color);
        this.center = center;
        this.radius = radius;
    }

    /**
     * Analytic solution for sphere-ray intersection.
     * @param ray The ray to test for intersection
     * @return Intersection information if exists, otherwise null
     */
    public Intersection intersectAnalytic(Ray ray) {
        Vector3D oc = ray.origin.subtract(center);
        double a = ray.direction.dot(ray.direction);
        double b = 2.0 * oc.dot(ray.direction);
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return null; // No real roots = no intersection
        } else {
            double t = (-b - Math.sqrt(discriminant)) / (2.0 * a);
            if (t > 0) {
                Vector3D hitPoint = ray.origin.add(ray.direction.scale(t));
                return new Intersection(hitPoint, t, this.color);
            }
        }
        return null;
    }

    /**
     * Geometric solution for sphere-ray intersection.
     * @param ray The ray to test for intersection
     * @return Array of potential intersections (may contain null values)
     */
    @Override
    public Intersection[] intersect(Ray ray) {
        Vector3D O = ray.origin;
        Vector3D D = ray.direction;
        Vector3D L = center.subtract(O);
        double tCA = L.dot(D);

        if(tCA < 0) return null;  // Sphere is behind ray origin
        else{
            double d = Math.sqrt((L.dot(L)) - (tCA*tCA));
            if(d < 0) return null;  // No intersection
            else{
                double tHC = Math.sqrt((radius*radius) - (d*d));
                double t0 = tCA - tHC;
                double t1 = tCA + tHC;

                // Create intersection points
                Intersection p0 = new Intersection(O.add(D.scale(t0)), t0, this.color);
                Intersection p1 = new Intersection(O.add(D.scale(t1)), t1, this.color);

                // Return intersections with valid points
                return new Intersection[]{
                        p0.point.compare(O) > 0 ? p0 : null,
                        p1.point.compare(O) > 0 ? p1 : null
                };
            }
        }
    }
}