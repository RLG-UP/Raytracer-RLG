package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.optimization.BoundingBox;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

import java.awt.Color;

import static java.lang.Math.clamp;

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
    public Sphere(Vector3D center, double radius, Color color, double refraction, double transparency) {
        super(color, refraction, transparency);
        this.center = center;
        this.radius = radius;
    }

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

        // Check if points lie on the sphere's surface
        double distA = Vector3D.subtract(A, center).value;
        double distB = Vector3D.subtract(B, center).value;

        // Create intersection points
        Intersection p0 = new Intersection(A, t0, super.getColor(), this.normal(A));
        Intersection p1 = new Intersection(B, t1, super.getColor(), this.normal(B));

        // Return intersections with valid points
        return new Intersection[]{
                t0 > 0 ? p0 : null,
                t1 > 0 ? p1 : null
        };
    }


    /*
    @Override
    public Intersection[] intersect(Ray ray) {
        Vector3D L = Vector3D.subtract(this.center, ray.origin);
        double tca = L.dot(ray.direction.scale(-1));
        double L2 = Math.pow(L.value, 2);
        double d2 = L2 - Math.pow(tca, 2);
        if(d2 >= 0){
            double d = Math.sqrt(d2);
            double t0 = tca - Math.sqrt(Math.pow(this.radius, 2) - Math.pow(d, 2));
            double t1 = tca + Math.sqrt(Math.pow(this.radius, 2) - Math.pow(d, 2));

            double distance = Math.min(t0, t1);
            Vector3D position = ray.origin.add(ray.direction.scale(distance));
            //Vector3D normal = Vector3D.subtract(position, this.center).normalize();
            //return new Intersection(position, distance, normal, this);

            Intersection p0 = new Intersection(position, t0, super.getColor(), this.normal(position));

            // Return intersections with valid points
            return new Intersection[]{
                    t0 > 0 ? p0 : null,
                    null
            };
        }

        return null;
    }

     */

    public Vector3D normal(Vector3D point){
        return Vector3D.subtract(point, center).normalize();
    }


    /*
    @Override
    public Color addLight(Vector3D point) {
        Vector3D N = this.normal(point);
        return Light.calculateColor(N, point, this);
    }

     */

    @Override
    public Color addLight(Intersection intersection) {
        Vector3D N = this.normal(intersection.point);
        return Light.calculateColor(N, intersection.point, this, intersection);
    }


    public Object3D returnZero(){
        return new Sphere(Vector3D.getZero(), 0.0, null, 0, 0);
    }

    @Override
    public BoundingBox getBB(){
        float r = (float)this.radius;
        Vector3D origin = this.center;
        Vector3D min = new Vector3D(origin.x - r, origin.y - r, origin.z - r);
        Vector3D max = new Vector3D(origin.x + r, origin.y + r, origin.z + r);
        return new BoundingBox(min, max);
    }

    @Override
    public String type(){ return "sphere"; }
}