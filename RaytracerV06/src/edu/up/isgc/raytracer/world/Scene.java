package edu.up.isgc.raytracer.world;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.shapes.Object3D;
import edu.up.isgc.raytracer.shapes.Triangle;
import edu.up.isgc.raytracer.shapes.models.Polygon;

import java.util.ArrayList;

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

    public static boolean isInShadow(Vector3D surfacePoint, Vector3D normal, Light light, Scene scene) {
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


}