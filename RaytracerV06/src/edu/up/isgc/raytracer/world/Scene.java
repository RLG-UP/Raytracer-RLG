package edu.up.isgc.raytracer.world;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.shapes.Object3D;
import edu.up.isgc.raytracer.shapes.Triangle;
import edu.up.isgc.raytracer.shapes.models.Polygon;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.clamp;

/**
 * Represents a 3D scene containing multiple objects to be rendered.
 * Manages object collection and intersection calculations.
 */
public class Scene {
    private static ArrayList<Object3D> objects;  // Collection of 3D objects in the scene

    /**
     * Constructs an empty scene.
     */
    public Scene() {
        objects = new ArrayList<>();
    }

    /**
     * Adds a 3D object to the scene.
     * @param obj The object to add
     */
    public void addObject(Object3D obj) {
        objects.add(obj);
    }

    public void addPolygon(Polygon p) {
        for(Triangle triangle : p.getShape()){
            this.addObject(triangle);
        }
    }

    /**
     * Finds the closest intersection between a ray and any object in the scene.
     * @param ray The ray to test for intersections
     * @return The closest intersection, or null if no intersection found
     */
    public Intersection findClosestIntersection(Ray ray, Camera camera) {
        Intersection closestIntersection = null;
        double minDist = Double.MAX_VALUE;
        Intersection[] intersections;

        for (Object3D obj : objects) {
            // Get potential intersections with current object
            intersections = obj.intersect(ray);
                if(intersections != null) {
                    Intersection intersection = intersections[0] != null ? intersections[0] :
                            intersections[1] != null ? intersections[1] : null;

                    //&& (intersection.distance >= camera.clipPlanes[0] && intersection.distance <= camera.clipPlanes[0])
                    // Update closest intersection if this one is closer
                    if (intersection != null && intersection.distance < minDist && (intersection.distance >= camera.clipPlanes[0] && intersection.distance <= camera.clipPlanes[1])) {
                        minDist = intersection.distance;
                        closestIntersection = intersection;
                        closestIntersection.object = obj;
                    }
                }
        }

        return closestIntersection;
    }

    /*
    public static Intersection findRayIntersection(Ray ray) {
        Intersection closestIntersection = null;
        double minDist = Double.MAX_VALUE;
        Intersection[] intersections;

        for (Object3D obj : Scene.objects) {
            // Get potential intersections with current object
            intersections = obj.intersect(ray);
                if(intersections != null) {
                    Intersection intersection = intersections[0] != null ? intersections[0] :
                            intersections[1] != null ? intersections[1] : null;

                    //&& (intersection.distance >= camera.clipPlanes[0] && intersection.distance <= camera.clipPlanes[0])
                    // Update closest intersection if this one is closer
                    if (intersection != null && intersection.distance < minDist) {
                        minDist = intersection.distance;
                        closestIntersection = intersection;
                        closestIntersection.object = obj;
                    }
                }
        }


        return closestIntersection;
    }

     */

    /*
    public static Intersection findRayIntersection(Ray ray) {
        Intersection closestIntersection = null;
        double minDist = Double.MAX_VALUE;

        for (Object3D obj : Scene.objects) {

            Intersection[] intersections = obj.intersect(ray);

            if (intersections != null) {
                for (Intersection intersection : intersections) {
                    //System.out.println("Intersection: " + intersection);
                    if (intersection != null && intersection.distance < minDist && intersection.distance > Camera.getEpsilon()) {
                        System.out.println("Testing intersection with object: " + obj.getClass().getSimpleName());
                        System.out.println("Intersection at: " + intersection.point + ", distance = " + intersection.distance);

                        System.out.println(" -> Valid hit at distance: " + intersection.distance);
                        minDist = intersection.distance;
                        closestIntersection = intersection;
                        closestIntersection.object = obj;
                    }
                }
            }
        }

        return closestIntersection;
    }

     */

    /*
    public static Intersection findRayIntersection(Ray ray, Object3D ignoreShape) {
        Intersection closestHit = null;

        for (Object3D shape : Scene.objects) {
            if (shape == ignoreShape) continue;
            if(shape == null) continue;

            Intersection hit = shape.intersect(ray)[0];

            if (hit != null && (closestHit == null || hit.distance < closestHit.distance)) {
                closestHit = hit;
            }
        }

        return closestHit;
    }

     */
    public static Intersection findRayIntersection(Ray ray, Object3D ignoreShape) {
        Intersection closestHit = null;

        for (Object3D shape : Scene.objects) {
            if (shape == null || shape == ignoreShape) continue;

            Intersection[] hits = shape.intersect(ray);
            if (hits == null) continue;

            for (Intersection hit : hits) {
                if (hit == null) continue;

                if (hit.distance > Camera.getEpsilon() &&
                        (closestHit == null || hit.distance < closestHit.distance)) {
                    closestHit = hit;
                    closestHit.object = shape;
                }

            }
        }
        return closestHit;
    }




    /*
    public static boolean isInShadow(Vector3D surfacePoint, Vector3D normal, Light light) {
        Vector3D lightDir = Vector3D.subtract(light.getPosition(), surfacePoint).normalize();
        Vector3D shadowOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon()));
        Ray shadowRay = new Ray(shadowOrigin, lightDir);

        Intersection shadowHit = Scene.findRayIntersection(shadowRay);

        if (shadowHit != null) {
            double lightDistance = Vector3D.subtract(light.getPosition(), surfacePoint).value;
            return shadowHit.distance < lightDistance; // only count it if it's between point and light
        }

        return false;
    }

     */

    public static boolean isInShadow(Vector3D surfacePoint, Vector3D normal, Light light, Object3D ignoreShape) {
        Vector3D lightDir = Vector3D.subtract(light.getPosition(), surfacePoint).normalize();
        Vector3D shadowOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon()));
        Ray shadowRay = new Ray(shadowOrigin, lightDir);

        Intersection shadowHit = Scene.findRayIntersection(shadowRay, ignoreShape);

        if (shadowHit != null && shadowHit.object != ignoreShape) {
            //System.out.println("The object " + ignoreShape + " casted a ray and hit " + shadowHit.object);
            double lightDistance = Vector3D.subtract(light.getPosition(), surfacePoint).value;
            return shadowHit.distance < lightDistance - Camera.getEpsilon(); // small margin
        }

        return false;
    }

    /*
    public static Color castReflection(Vector3D surfacePoint, Vector3D normal, Object3D ignoreShape, int recursionLimit) {
        Color c = Color.black;
        Vector3D d = Vector3D.subtract(Camera.getCameraPosition(), surfacePoint).normalize();
        Vector3D rayOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon()));
        Ray reflectionRay = new Ray(rayOrigin, d);

        Intersection rayHit = Scene.findRayIntersection(reflectionRay, ignoreShape);

        if (rayHit != null && rayHit.object != ignoreShape && recursionLimit > 0) {
            recursionLimit--;
            Color reflectionColor = castReflection(rayHit.point, rayHit.getNormal(), rayHit.object, recursionLimit)
            c = new Color(clamp(c.getRed() + reflectionColor.getRed(), 0, 255),
                    clamp(c.getGreen() + reflectionColor.getGreen(), 0, 255),
                    clamp(c.getBlue() + reflectionColor.getBlue(), 0, 255));
            return c;

            //System.out.println("The object " + ignoreShape + " casted a ray and hit " + shadowHit.object);
            //double D = Vector3D.subtract(Camera.getCameraPosition(), surfacePoint).value;
            //return shadowHit.distance < D - Camera.getEpsilon(); // small margin
        }

        return c;
        //return false;
    }

     */

    public static Color castReflection(Vector3D surfacePoint, Vector3D normal, Object3D ignoreShape, int recursionLimit) {
        if (recursionLimit <= 0) {
            return Color.BLACK;
        }

        // Step 1: Compute reflected direction
        Vector3D incoming = Vector3D.subtract(surfacePoint, Camera.getCameraPosition()).normalize().scale(-1);
        Vector3D reflected = Vector3D.subtract(incoming, normal.scale(2 * incoming.dot(normal))).normalize();

        // Step 2: Offset origin to avoid self-hit
        Vector3D rayOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon()));
        Ray reflectionRay = new Ray(rayOrigin, reflected);

        // Step 3: Check intersection
        Intersection hit = Scene.findRayIntersection(reflectionRay, ignoreShape);

        if (hit != null && hit.object != null) {
            // Get base color of object hit
            Color localColor = hit.object.getColor(); // Or calculate with lighting, if available

            // Recurse
            Color reflectedColor = castReflection(hit.point, hit.getNormal(), hit.object, recursionLimit - 1);

            // Combine local and reflected colors
            int r = clamp((int)(localColor.getRed() * 0.5 + reflectedColor.getRed() * 0.5), 0, 255);
            int g = clamp((int)(localColor.getGreen() * 0.5 + reflectedColor.getGreen() * 0.5), 0, 255);
            int b = clamp((int)(localColor.getBlue() * 0.5 + reflectedColor.getBlue() * 0.5), 0, 255);

            return new Color(r, g, b);
        }

        return Color.BLACK;
    }




}