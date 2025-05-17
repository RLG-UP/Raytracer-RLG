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
     *
     * @param obj The object to add
     */
    public void addObject(Object3D obj) {
        objects.add(obj);
    }

    public void addPolygon(Polygon p) {
        for (Triangle triangle : p.getShape()) {
            this.addObject(triangle);
        }
    }

    /**
     * Finds the closest intersection between a ray and any object in the scene.
     *
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
            if (intersections != null) {
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

    public static Intersection findRefractionIntersection(Ray ray, Object3D ignoreShape) {
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

    public static Intersection findShadowIntersection(Ray ray, Object3D ignoreShape) {
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
    public static boolean isInShadow(Vector3D point, Vector3D normal, Light light, Object3D sourceObject) {
        // 1. Calculate direction to light and distance
        Vector3D toLight = Vector3D.subtract(light.getPosition(), point);
        double lightDistance = toLight.value;
        Vector3D lightDir = toLight.normalize();

        // 2. Offset the ray origin to avoid self-intersection
        Vector3D shadowRayOrigin = point.add(normal.scale(Camera.getEpsilon()));

        // 3. Create shadow ray
        Ray shadowRay = new Ray(shadowRayOrigin, lightDir);

        // 4. Check for intersections with scene objects
        for (Object3D obj : objects) {
            if (obj == sourceObject || obj == null) continue;

            Intersection[] intersections = obj.intersect(shadowRay);
            if (intersections == null) continue;

            for (Intersection intersection : intersections) {
                if (intersection == null) continue;

                // 5. Valid shadow hit if:
                //    - Intersection is in front of the point (distance > epsilon)
                //    - Intersection is closer than the light
                if (intersection.distance > Camera.getEpsilon() &&
                        intersection.distance < lightDistance) {
                    return true;
                }
            }
        }

        return false;
    }

     */




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

    /*

    public static boolean isInShadow(Vector3D surfacePoint, Vector3D normal, Light light, Object3D ignoreShape) {
        Vector3D lightDir = Vector3D.subtract(light.getPosition(), surfacePoint).normalize().scale(1);

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

     */

    /*
    public static boolean isInShadow(Vector3D point, Vector3D normal, Light light, Object3D sourceObject) {
        Vector3D lightDir = Vector3D.subtract(light.getPosition(), point).normalize();
        Vector3D origin = point.add(normal.scale(Camera.getEpsilon())); // Offset to avoid self-hit

        Ray shadowRay = new Ray(origin, lightDir);
        double lightDistance = Vector3D.subtract(light.getPosition(), origin).value;

        for (Object3D obj : Scene.objects) {
            if (obj == sourceObject) continue; // Avoid hitting itself
            Intersection[] intersection = obj.intersect(shadowRay);
            if (intersection == null) continue;
            double t = intersection.length;
            if (t > 0 && t < lightDistance) {
                return true; // In shadow
            }
        }
        return false;
    }

     */


    public static boolean isInShadow(Vector3D surfacePoint, Vector3D normal, Light light, Object3D sourceObject) {
        // Step 1: Calculate direction from surface point to the light
        Vector3D lightDir = Vector3D.subtract(light.getPosition(), surfacePoint).normalize().scale(-1);

        // Step 2: Offset the ray origin slightly along the normal to prevent self-intersection
        Vector3D shadowOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon()));

        // Step 3: Cast shadow ray toward the light
        Ray shadowRay = new Ray(shadowOrigin, lightDir);

        // Step 4: Distance from the point to the light source
        double lightDistance = Vector3D.subtract(light.getPosition(), shadowOrigin).value;

        // Step 5: Check for intersections along this ray
        for (Object3D obj : objects) {
            if (obj == sourceObject) continue; // Skip the object casting the shadow ray

            Intersection[] intersections = obj.intersect(shadowRay);
            if (intersections == null) continue;

            for (Intersection hit : intersections) {
                if (hit == null) continue;

                if (hit.distance > Camera.getEpsilon() && hit.distance < lightDistance) {
                    return true; // Object blocks the light — surface is in shadow
                }
            }
        }

        return false; // No object blocked the light — surface is illuminated
    }





    /*
    public static boolean isInShadow(Vector3D surfacePoint, Vector3D normal, Light light, Object3D ignoreShape) {
        Vector3D incoming = Vector3D.subtract(surfacePoint, light.getPosition()).normalize();
        Vector3D reflected = Vector3D.subtract(incoming, normal.scale(2 * incoming.dot(normal))).normalize().scale(-1);

        // Step 2: Offset origin to avoid self-hit
        Vector3D rayOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon()));
        Ray reflectionRay = new Ray(rayOrigin, reflected);

        // Step 3: Check intersection
        Intersection hit = Scene.findRayIntersection(reflectionRay, ignoreShape);
        return hit != null && hit.distance > Camera.getEpsilon();
    }

     */



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
            int r = clamp((int) (localColor.getRed() * 0.5 + reflectedColor.getRed() * 0.5), 0, 255);
            int g = clamp((int) (localColor.getGreen() * 0.5 + reflectedColor.getGreen() * 0.5), 0, 255);
            int b = clamp((int) (localColor.getBlue() * 0.5 + reflectedColor.getBlue() * 0.5), 0, 255);

            return new Color(r, g, b);
        }

        return Color.BLACK;
    }


    public static Color castRefraction(Vector3D surfacePoint, Vector3D normal, Object3D ignoreShape, int recursionLimit) {
        if (recursionLimit <= 0 || ignoreShape.transparency <= 0) {
            return Color.BLACK;
        }

        double n1 = 1.0;
        double n2 = ignoreShape.refraction;

        Vector3D incoming = Vector3D.subtract(surfacePoint, Camera.getCameraPosition()).normalize().scale(-1);
        double c1 = normal.dot(incoming);

        if (c1 < 0) {
            c1 = -c1;
        }else{
            normal = normal.scale(-1);
            n1 = n2;
            n2 = 1.0;
        }

        double eta = n1 / n2;
        double theta = Math.acos(c1);
        double k = 1 - (eta * eta) * (1 - Math.pow(Math.sin(theta), 2));

        if (k < 0) {
            // Total internal reflection
            return Scene.castReflection(surfacePoint, normal, ignoreShape, recursionLimit - 1);
        }

        double c2 = Math.sqrt(k);
        double c3 = (eta * c1) - c2;
        Vector3D refracted = incoming.scale(eta).add(normal.scale(c3)).normalize();

        Vector3D rayOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon()));
        Ray refractionRay = new Ray(rayOrigin, refracted);

        Intersection hit = Scene.findRayIntersection(refractionRay, ignoreShape);

        if (hit != null && hit.object != null) {
            // Optionally compute lighting here instead of raw color
            Color localColor = hit.object.getColor();

            Color refractedColor = castRefraction(hit.point, hit.getNormal(), hit.object, recursionLimit - 1);

            double transparency = hit.object.transparency;

            Vector3D viewDir = Vector3D.subtract(Camera.getCameraPosition(), surfacePoint).normalize();
            float cosTheta = Math.max(0f, (float)viewDir.dot(normal.normalize()));
            float fresnel = Light.schlick(cosTheta, (float)ignoreShape.refraction);


            int r = clamp((int) (localColor.getRed() * (1 - transparency) +
                    transparency * (localColor.getRed() * (1 - fresnel) + refractedColor.getRed() * fresnel)), 0, 255);
            int g = clamp((int) (localColor.getGreen() * (1 - transparency) +
                    transparency * (localColor.getGreen() * (1 - fresnel) + refractedColor.getGreen() * fresnel)), 0, 255);
            int b = clamp((int) (localColor.getBlue() * (1 - transparency) +
                    transparency * (localColor.getBlue() * (1 - fresnel) + refractedColor.getBlue() * fresnel)), 0, 255);

            return new Color(r, g, b);
        }

        return Color.BLACK;
    }



}