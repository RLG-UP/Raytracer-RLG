package edu.up.isgc.raytracer;

import java.util.ArrayList;

/**
 * Represents a 3D scene containing multiple objects to be rendered.
 * Manages object collection and intersection calculations.
 */
public class Scene {
    private ArrayList<Object3D> objects;  // Collection of 3D objects in the scene

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

    /**
     * Finds the closest intersection between a ray and any object in the scene.
     * @param ray The ray to test for intersections
     * @return The closest intersection, or null if no intersection found
     */
    public Intersection findClosestIntersection(Ray ray, Double[] clipPlanes) {
        Intersection closestIntersection = null;
        double minDist = Double.MAX_VALUE;

        for (Object3D obj : objects) {
            Intersection[] intersections = obj.intersect(ray);
            if (intersections != null) {
                // Get potential intersections with current object
                Intersection intersection = obj.intersect(ray)[0] != null ? obj.intersect(ray)[0] :
                        obj.intersect(ray)[1] != null ? obj.intersect(ray)[1] : null;

                if (intersection != null) System.out.println(intersection.distance);
                // Update closest intersection if this one is closer
                if (intersection != null && intersection.distance < minDist && (intersection.distance >= clipPlanes[0] || intersection.distance <= clipPlanes[1])) {
                    System.out.println(intersection);
                    minDist = intersection.distance;
                    closestIntersection = intersection;
                }
            }
        }
        return closestIntersection;
    }
}