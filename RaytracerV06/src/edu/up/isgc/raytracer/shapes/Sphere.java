package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.lighting.Directional;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.world.Scene;

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
        Vector3D oc = Vector3D.subtract(ray.origin, center);
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
    /*
    @Override
    public Intersection[] intersect(Ray ray) {
        Vector3D O = ray.origin;
        Vector3D D = ray.direction;
        Vector3D L = Vector3D.subtract(center, O);
        double tCA = L.dot(D);

        if(tCA < 0) return null;  // Sphere is behind ray origin
        else{
            double d = Math.sqrt((L.dot(L)) - (tCA*tCA));
            if(d < 0) return null;  // No intersection
            else{
                double tHC = Math.sqrt((radius*radius) - (d*d));
                double t0 = tCA - tHC;
                double t1 = tCA + tHC;

                Vector3D A = O.add(D.scale(t0));
                Vector3D B = O.add(D.scale(t1));
                System.out.println("t0: " + t0 + " | A: " + A);
                System.out.println("t1: " + t1 + " | B: " + B);

                // Check if points lie on the sphere's surface
                double distA = Vector3D.subtract(A, center).value;
                double distB = Vector3D.subtract(B, center).value;
                System.out.println("Distance A to center: " + distA + " (Expected: " + radius + ")");
                System.out.println("Distance B to center: " + distB + " (Expected: " + radius + ")");

                // Create intersection points
                Intersection p0 = new Intersection(A, t0, this.addLight(A));
                Intersection p1 = new Intersection(B, t1, this.addLight(B));

                // Return intersections with valid points
                return new Intersection[]{
                        p0.point.compare(O) > 0 ? p0 : null,
                        p1.point.compare(O) > 0 ? p1 : null
                };
            }
        }
    }

     */


    public Intersection[] intersect(Ray ray) {
        Vector3D O = ray.origin;
        Vector3D D = ray.direction;
        Vector3D L = Vector3D.subtract(center, O);
        double tCA = L.dot(D);

        if(tCA < 0) return null;  // Sphere is behind ray origin

        double dSquared = L.dot(L) - (tCA * tCA);
        if(dSquared > radius * radius) return null;  // No intersection

        double tHC = Math.sqrt((radius * radius) - dSquared);
        double t0 = tCA - tHC;
        double t1 = tCA + tHC;

        Vector3D A = O.add(D.scale(t0));
        Vector3D B = O.add(D.scale(t1));

        // Check if points lie on the sphere's surface
        double distA = Vector3D.subtract(A, center).value;
        double distB = Vector3D.subtract(B, center).value;

        // Create intersection points
        Intersection p0 = new Intersection(A, t0, super.getColor());
        Intersection p1 = new Intersection(B, t1, super.getColor());

        // Return intersections with valid points
        return new Intersection[]{
                t0 > 0 ? p0 : null,
                t1 > 0 ? p1 : null
        };
    }

    /*
    public Intersection[] intersect(Ray ray) {
        Vector3D O = ray.origin;
        Vector3D D = ray.direction;
        Vector3D L = Vector3D.subtract(center, O);

        double tCA = L.dot(D);
        double dSquared = L.dot(L) - (tCA * tCA);
        double rSquared = radius * radius;

        if (dSquared > rSquared) return null; // Misses the sphere

        double tHC = Math.sqrt(rSquared - dSquared);
        double t0 = tCA - tHC;
        double t1 = tCA + tHC;

        Intersection p0 = null;
        Intersection p1 = null;

        if (t0 > Camera.getEpsilon()) {
            Vector3D A = O.add(D.scale(t0));
            p0 = new Intersection(A, t0, super.getColor());
        }
        if (t1 > Camera.getEpsilon()) {
            Vector3D B = O.add(D.scale(t1));
            p1 = new Intersection(B, t1, super.getColor());
        }

        if (p0 == null && p1 == null) return null; // Both behind or too close

        return new Intersection[]{ p0, p1 };
    }

     */

    /*
    public Intersection[] intersect(Ray ray) {
        Vector3D O = ray.origin;
        Vector3D D = ray.direction.normalize();
        Vector3D L = Vector3D.subtract(center, O);
        double tCA = L.dot(D);

        double dSquared = L.dot(L) - tCA * tCA;
        double radiusSquared = radius * radius;

        if (dSquared > radiusSquared) {
            //System.out.println("Ray misses the sphere");
            return null;
        }

        double tHC = Math.sqrt(radiusSquared - dSquared);
        double t0 = tCA - tHC;
        double t1 = tCA + tHC;

        Vector3D A = O.add(D.scale(t0));
        Vector3D B = O.add(D.scale(t1));

        Intersection p0 = new Intersection(A, t0, super.getColor());
        Intersection p1 = new Intersection(B, t1, super.getColor());

        //System.out.println(" -> Sphere hit: t0 = " + t0 + ", t1 = " + t1);

        return new Intersection[] {
                t0 > Camera.getEpsilon() ? p0 : null,
                t1 > Camera.getEpsilon() ? p1 : null
        };
    }

     */


    public Vector3D normal(Vector3D point){
        return Vector3D.subtract(point, center).normalize();
    }

    //HERE IS THE ADDLIGHT BEFORE TRYING TO REWORK THE SPHERE ONE
    /*
    @Override
    public Color addLight(Vector3D point) {
        float lambertian = 0;
        Color finalColor = new Color(0,0,0);
        float lightAttenuation = 0;

        Vector3D N = this.normal(point);
        float ks = 1f;
        float p = 100;

        for(Light light : Light.getLights()){
            float Is;
            Vector3D l = Vector3D.getZero();
            if (light.type().equals("directional")) {
                l = light.getDirection();
                lambertian = (float) clamp(N.dot(l), 0.0, 1.0);
            }
            else if(light.type().equals("point") || light.type().equals("spot")){
                l = light.getDirection(point).normalize();
                lambertian = (float) clamp(N.dot(l) * light.getAttenuation(), 0.0, 1.0);
            }

            //lambertian = (float) clamp(lambertian, 0.0, 1.0);
            Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
            Is = (float) ( ks * Math.pow(clamp(N.dot(h), 0,1), p) );
            lambertian = (float) clamp(lambertian + Is, 0.0, 1.0);
            Color lightContribution = Light.shine(light.getColor(), super.getColor(), lambertian);

            finalColor = new Color(
                    clamp(finalColor.getRed()   + lightContribution.getRed(),   0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue()  + lightContribution.getBlue(),  0, 255)
            );
        }
        return finalColor;
    }

     */

    @Override
    public Color addLight(Vector3D point) {
        Color finalColor = new Color(0, 0, 0);

        Vector3D N = this.normal(point);  // Normal at point on sphere
        float ks = 1f;
        float p = 100f;
        float ka = 0.1f;  // Ambient constant
        float ambientIntensity = ka * Light.getAmbientLight();

        for (Light light : Light.getLights()) {
            Vector3D l = Vector3D.getZero();
            double lightDistance = Double.MAX_VALUE;

            if (light.type().equals("directional")) {
                l = light.getDirection().normalize();
            } else if (light.type().equals("point") || light.type().equals("spot")) {
                l = light.getDirection(point).normalize();
                lightDistance = Vector3D.subtract(light.getPosition(), point).value;
            }

            boolean inShadow = Scene.isInShadow(point, N, light, this);

            float lambertian = 0;
            float blinn = 0;

            if (!inShadow) {
                lambertian = (float) Math.max(N.dot(l) * light.getAttenuation(), 0.0);
                Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
                blinn = (float) (ks * Math.pow(Math.max(N.dot(h), 0.0), p));
            }

            // Individual components
            Color ambient  = Light.shine(light.getColor(), super.getColor(), ambientIntensity, true);
            Color diffuse  = Light.shine(light.getColor(), super.getColor(), lambertian, true);
            Color specular = Light.shine(light.getColor(), super.getColor(), blinn, false); // specular is independent of object color

            // Combine them
            Color lightContribution = new Color(
                    clamp(ambient.getRed() + diffuse.getRed() + specular.getRed(), 0, 255),
                    clamp(ambient.getGreen() + diffuse.getGreen() + specular.getGreen(), 0, 255),
                    clamp(ambient.getBlue() + diffuse.getBlue() + specular.getBlue(), 0, 255)
            );

            // Accumulate contribution
            finalColor = new Color(
                    clamp(finalColor.getRed() + lightContribution.getRed(), 0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue() + lightContribution.getBlue(), 0, 255)
            );
        }

        return finalColor;
    }


    public Object3D returnZero(){
        return new Sphere(Vector3D.getZero(), 0.0, null);
    }

    /*
    public Color addLight(Vector3D point) {
        Vector3D[] testPoints = {
                new Vector3D(1, 0, 0),   // Right side
                new Vector3D(0, 1, 0),    // Top
                new Vector3D(0, 0, 1)     // Front
        };

        for (Vector3D p : testPoints) {
            Vector3D N = normal(p);
            Vector3D L = new Vector3D(1, 1, 1).normalize();
            System.out.printf("Point %s | N·L: %.2f\n", p, N.dot(L));
        }
        float lambertian = 0;
        Color finalColor = new Color(0,0,0);
        float lightAttenuation = 0;

        Vector3D N = this.normal(point);
        float ks = 1f;
        float p = 100;

        for(Light light : Light.getLights()){
            float Is;
            Vector3D l = Vector3D.getZero();
            if (light.type().equals("directional")) {
                l = light.getDirection();
                //lambertian = (float)clamp(Light.ericson(point, this.getA(), this.getC(), this.getB(),  this.getnA(), this.getnC(), this.getnB()).dot(light.getDirection()), 0.0, 1.0);
                lambertian = (float) clamp(N.dot(l) * light.getIntensity(), 0.0, 1.0);
            }
            else if(light.type().equals("point") || light.type().equals("spot")){
                l = light.getDirection(point).normalize().scale(-1);
                lambertian = (float) clamp(N.dot(l) * light.getAttenuation(), 0.0, 1.0);
            }

            Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
            //Is = (float) ( ks * Math.pow(clamp(N.dot(h), 0,1), p) );
            lambertian = (float) clamp(lambertian , 0.0, 1.0);
            Color lightContribution = Light.shine(light.getColor(), super.getColor(), lambertian);

            finalColor = new Color(
                    clamp(finalColor.getRed()   + lightContribution.getRed(),   0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue()  + lightContribution.getBlue(),  0, 255)
            );
        }
        return finalColor;

    }

     */

    /*

    public Color addLight(Vector3D point) {
        float lambertian = 0;
        Color finalColor = new Color(0,0,0);
        float lightAttenuation = 0;

        Vector3D N = this.normal(point);
        float ks = 1f;
        float p = 100;

        for(Light light : Light.getLights()){
            float Is;
            Vector3D l = Vector3D.getZero();
            if (light.type().equals("directional")) {
                l = light.getDirection();
                //lambertian = (float)clamp(Light.ericson(point, this.getA(), this.getC(), this.getB(),  this.getnA(), this.getnC(), this.getnB()).dot(light.getDirection()), 0.0, 1.0);
                lambertian = (float) clamp(N.dot(l) * light.getIntensity(), 0.0, 1.0);
            }
            else if(light.type().equals("point") || light.type().equals("spot")){
                l = light.getDirection(point).normalize().scale(-1);
                lambertian = (float) clamp(N.dot(l) * light.getAttenuation(), 0.0, 1.0);
            }

            Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
            Is = (float) ( ks * Math.pow(clamp(N.dot(h), 0,1), p) );
            lambertian = (float) clamp(lambertian + Is, 0.0, 1.0);

            Color lightContribution = Light.shine(light.getColor(), super.getColor(), lambertian);

            finalColor = new Color(
                    clamp(finalColor.getRed()   + lightContribution.getRed(),   0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue()  + lightContribution.getBlue(),  0, 255)
            );
        }
        return finalColor;

    }

     */

    /*

        public Color addLight(Vector3D point) {
        float lambertian = 0;
        Color finalColor = new Color(0,0,0);
        float lightAttenuation = 0;

        float ks = 1f;
        float p = 100;
        float Is;
        Vector3D N = this.normal(point);

        for(Light light : Light.getLights()){
            Vector3D l = light.getDirection(point.normalize()).normalize();
            Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
            if (light.type().equals("directional")) {
                lambertian += (float) N.dot(light.getDirection()) * light.getAttenuation();
            }
            else if(light.type().equals("point") || light.type().equals("spot")){
                lambertian += (float) N.dot(l.scale(-1)) * light.getAttenuation();
            }

            Is = (float) ( ks * Math.pow(clamp(N.dot(h), 0,1), p) );
            lambertian = (float) clamp(lambertian + Is, 0.0, 1.0);
            Color lightContribution = Light.shine(light.getColor(), super.getColor(), lambertian );

            finalColor = new Color(
                    clamp(finalColor.getRed()   + lightContribution.getRed(),   0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue()  + lightContribution.getBlue(),  0, 255)
            );
        }
        return finalColor;
    }
     */






    @Override
    public String type(){ return "sphere"; }
}