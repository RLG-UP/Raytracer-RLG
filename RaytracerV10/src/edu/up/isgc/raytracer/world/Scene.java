package edu.up.isgc.raytracer.world;

import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.optimization.BBTree;
import edu.up.isgc.raytracer.optimization.BoundingBox;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.optimization.RBTree;
import edu.up.isgc.raytracer.shapes.Object3D;
import edu.up.isgc.raytracer.shapes.Triangle;
import edu.up.isgc.raytracer.shapes.models.Polygon;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.clamp;

/**
 * Represents a 3D scene containing objects to be rendered by a raytracer.
 * Manages scene objects, lighting, and optical effects like reflection and refraction.
 * Uses a BBTree (Bounding Box Tree) for efficient ray intersection calculations.
 */
public class Scene {
    /** Collection of all 3D objects in the scene */
    private static ArrayList<Object3D> objects;
    /** Bounding Box Tree for spatial partitioning and accelerated ray tracing */
    public static BBTree BBTree;
    /** Default background color when rays don't hit any objects */
    public static Color background;

    /**
     * Constructs an empty scene with default background.
     */
    public Scene() {
        objects = new ArrayList<>();
        BBTree = new BBTree();
    }
    /**
     * Constructs an empty scene with specified background color.
     *
     * @param background The background color of the scene
     */
    public Scene(Color background) {
        Scene.background = background;
        objects = new ArrayList<>();
        BBTree = new BBTree();
    }


    /**
     * Adds a 3D object to the scene and updates the BBTree.
     *
     * @param obj The object to add to the scene
     */
    public void addObject(Object3D obj) {
        objects.add(obj);
        BBTree.insert(obj);
    }
    /**
     * Adds all triangles from a polygon to the scene.
     *
     * @param p The polygon whose triangles should be added
     */
    public void addPolygon(Polygon p) {
        for (Triangle triangle : p.getShape()) {
            this.addObject(triangle);
        }
    }

    /**
     * Finds the closest valid intersection between a ray and scene objects.
     *
     * @param ray The ray to test for intersections
     * @param ignoreShape Object to exclude from intersection tests
     * @return The closest valid intersection, or null if none found
     */
    public static Intersection findRayIntersection(Ray ray, Object3D ignoreShape) {
        Intersection[] allHits = Scene.BBTree.traverse(ray);
        if (allHits == null) return null;

        // Return the closest valid intersection (first in sorted array)
        for (Intersection hit : allHits) {
            if (hit != null && hit.object != ignoreShape && hit.distance > Camera.getEpsilon()) {
                return hit;
            }
        }
        return null;
    }
    /**
     * Finds the closest intersection within camera clip planes.
     *
     * @param ray The ray to test for intersections
     * @param camera The camera defining clip planes
     * @return The closest valid intersection, or null if none found
     */
    public Intersection findClosestIntersection(Ray ray, Camera camera) {
        Intersection[] allHits = Scene.BBTree.traverse(ray);
        if (allHits == null || allHits.length == 0) return null;

        double clipNear = camera.clipPlanes[0];
        double clipFar = camera.clipPlanes[1];
        Intersection closestHit = null;
        double minDist = Double.MAX_VALUE;


        for (Intersection hit : allHits) {
            if (hit != null &&
                    hit.distance >= clipNear &&
                    hit.distance <= clipFar &&
                    hit.distance < minDist) {
                minDist = hit.distance;
                closestHit = hit;
            }
        }

        return closestHit;
    }
    /**
     * Determines if a surface point is in shadow relative to a light source.
     *
     * @param surfacePoint The point being tested
     * @param normal Surface normal at the point
     * @param light The light source to test against
     * @param sourceObject Object casting the shadow
     * @return true if the point is in shadow, false otherwise
     */
    public static boolean isInShadow(Vector3D surfacePoint, Vector3D normal, Light light, Object3D sourceObject) {
        // Step 1: Calculate direction from surface point to the light
        Vector3D lightDir = Vector3D.subtract(light.getPosition(), surfacePoint).normalize().scale(-1);

        // Step 2: Offset the ray origin slightly along the normal to prevent self-intersection
        Vector3D shadowOrigin = surfacePoint.add(normal.scale(Camera.getShadowEpsilon()));

        // Step 3: Cast shadow ray toward the light
        Ray shadowRay = new Ray(shadowOrigin, lightDir);

        // Step 4: Distance from the point to the light source
        double lightDistance = Vector3D.subtract(light.getPosition(), shadowOrigin).value;

        // Step 5: Check for intersections along this ray
        //Intersection[] intersections = BBTree.traverse(shadowRay);
        //if(intersections == null || intersections.length == 0) return false;
        Intersection intersection = Scene.findRayIntersection(shadowRay, sourceObject);
        if(intersection == null) return false;
        Object3D obj = intersection.object;

        if (obj == sourceObject) return false; // Skip the object casting the shadow ray

        if (intersection.distance > Camera.getShadowEpsilon() && intersection.distance < lightDistance) {
            return true; // Object blocks the light — surface is in shadow
        }



        return false; // No object blocked the light — surface is illuminated
    }


    /**
     * Calculates reflection color for a surface point using recursive ray tracing.
     *
     * @param surfacePoint The point of reflection
     * @param normal Surface normal at the point
     * @param ignoreShape Object to exclude from intersection tests
     * @param recursionLimit Maximum recursion depth
     * @return The calculated reflection color
     */
    public static Color castReflection(Vector3D surfacePoint, Vector3D normal, Object3D ignoreShape, int recursionLimit) {
        if (recursionLimit <= 0) {
            return Scene.background;
        }

        // Step 1: Compute reflected direction
        Vector3D incoming = Vector3D.subtract(surfacePoint, Camera.getCameraPosition()).normalize().scale(-1);

        if (incoming.dot(normal) > 0) {
            normal = normal.scale(-1);
        }

        Vector3D reflected = Vector3D.subtract(incoming, normal.scale(2 * incoming.dot(normal))).normalize();

        // Step 2: Offset origin to avoid self-hit
        Vector3D rayOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon()));
        Ray reflectionRay = new Ray(rayOrigin, reflected);

        // Step 3: Check intersection
        Intersection hit = Scene.findRayIntersection(reflectionRay, ignoreShape);
        boolean shadowFound = false;


        if (hit != null && hit.object != null) {
            // Get base color of object hit
            //Color localColor = hit.object.getColor(); // Or calculate with lighting, if available
            Vector3D lD = null;
            float lightDistance = 0;
            float ks = 0.0f;
            float p = 1000f;
            float ka = 0.1f;

            if (hit.object.getHasMaterial()) {
                Material material = hit.object.getMaterial();
                ks = material.getSpecular();
                p = material.getShininess();
                ka = material.getAmbient();
            }

            float lambertian = 0;
            float blinn = 0;
            float ambientIntensity = ka * Light.getAmbientLight();
            Color finalColor = Color.BLACK;

            for(Light l : Light.getLights()) {
                if(isInShadow(hit.point, hit.getNormal(), l, ignoreShape)) {
                    shadowFound = true;
                    break;
                }else{
                    if (l.type().equals("directional")) {
                        lD = l.getDirection().normalize().scale(1);
                    } else if (l.type().equals("point") || l.type().equals("spot")) {
                        lD = l.getDirection(hit.point).normalize().scale(-1);
                        lightDistance = (float)Vector3D.subtract(l.getPosition(), hit.point).value;
                    }

                    Vector3D N = hit.getNormal();
                    lambertian = (float) Math.max(N.dot(lD) * l.calculateAttenuation(hit.point), 0.0);
                    Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), hit.point).normalize().add(lD).normalize();
                    blinn = (float) (ks * Math.pow(Math.max(N.dot(h), 0.0), p));

                    Color ambient = Light.shine(l.getColor(), hit.color, ambientIntensity, true);
                    Color diffuse = Light.shine(l.getColor(), hit.color, lambertian, true);
                    Color specular = Light.shine(l.getColor(), hit.color, blinn, false);

                    Color lightContribution = new Color(
                            clamp(ambient.getRed() + diffuse.getRed() + specular.getRed(), 0, 255),
                            clamp(ambient.getGreen() + diffuse.getGreen() + specular.getGreen(), 0, 255),
                            clamp(ambient.getBlue() + diffuse.getBlue() + specular.getBlue(), 0, 255)
                    );

                    finalColor = new Color(
                            clamp(finalColor.getRed() + lightContribution.getRed(), 0, 255),
                            clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                            clamp(finalColor.getBlue() + lightContribution.getBlue(), 0, 255)
                    );
                }

            }

            Color localColor = !shadowFound? finalColor : new Color((int) (finalColor.getRed()*0.1), (int) (finalColor.getGreen()*0.1), (int) (finalColor.getBlue()*0.1)); // Or calculate with lighting, if available

            // Recurse
            Color reflectedColor = castReflection(hit.point, hit.getNormal(), hit.object, recursionLimit - 1);

            // Combine local and reflected colors

            int r = clamp((int) (localColor.getRed() * 0.9 + reflectedColor.getRed() * 0.9), 0, 255);
            int g = clamp((int) (localColor.getGreen() * 0.9 + reflectedColor.getGreen() * 0.9), 0, 255);
            int b = clamp((int) (localColor.getBlue() * 0.9 + reflectedColor.getBlue() * 0.9), 0, 255);

            return new Color(r, g, b);
        }

        return Scene.background;
    }

    /**
     * Calculates refraction color for a surface point using recursive ray tracing.
     *
     * @param surfacePoint The point of refraction
     * @param normal Surface normal at the point
     * @param ignoreShape Object to exclude from intersection tests
     * @param recursionLimit Maximum recursion depth
     * @return The calculated refraction color
     */
    public static Color castRefraction(Vector3D surfacePoint, Vector3D normal, Object3D ignoreShape, int recursionLimit) {
        if (recursionLimit <= 0 || ignoreShape.transparency <= 0) {
            return Scene.background;
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

        boolean shadowFound = false;
        if (hit != null && hit.object != null) {

            Vector3D viewDir = Vector3D.subtract(Camera.getCameraPosition(), surfacePoint).normalize();
            float cosTheta = Math.max(0f, (float)viewDir.dot(normal.normalize()));
            float fresnel = Light.schlick(cosTheta, (float)ignoreShape.refraction);
            
            //Color localColor = hit.object.getColor();
            Vector3D lD = null;
            float lightDistance = 0;
            float ks = 0.0f;
            float p = 1000f;
            float ka = 0.1f;

            if (hit.object.getHasMaterial()) {
                Material material = hit.object.getMaterial();
                ks = material.getSpecular();
                p = material.getShininess();
                ka = material.getAmbient();
            }

            float lambertian = 0;
            float blinn = 0;
            float ambientIntensity = ka * Light.getAmbientLight();
            Color finalColor = Color.BLACK;

            for(Light l : Light.getLights()) {
                if(isInShadow(hit.point, hit.getNormal(), l, ignoreShape)) {
                    shadowFound = true;
                    break;
                }else{
                    if (l.type().equals("directional")) {
                        lD = l.getDirection().normalize().scale(1);
                    } else if (l.type().equals("point") || l.type().equals("spot")) {
                        lD = l.getDirection(hit.point).normalize().scale(-1);
                        lightDistance = (float)Vector3D.subtract(l.getPosition(), hit.point).value;
                    }

                    Vector3D N = hit.getNormal();
                    lambertian = (float) Math.max(N.dot(lD) * l.calculateAttenuation(hit.point), 0.0);
                    Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), hit.point).normalize().add(lD).normalize();
                    blinn = (float) (ks * Math.pow(Math.max(N.dot(h), 0.0), p));

                    Color ambient = Light.shine(l.getColor(), hit.color, ambientIntensity, true);
                    Color diffuse = Light.shine(l.getColor(), hit.color, lambertian, true);
                    Color specular = Light.shine(l.getColor(), hit.color, blinn, false);

                    Color lightContribution = new Color(
                            clamp(ambient.getRed() + diffuse.getRed() + specular.getRed(), 0, 255),
                            clamp(ambient.getGreen() + diffuse.getGreen() + specular.getGreen(), 0, 255),
                            clamp(ambient.getBlue() + diffuse.getBlue() + specular.getBlue(), 0, 255)
                    );

                    finalColor = new Color(
                            clamp(finalColor.getRed() + lightContribution.getRed(), 0, 255),
                            clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                            clamp(finalColor.getBlue() + lightContribution.getBlue(), 0, 255)
                    );
                }

            }

            Color localColor = !shadowFound? finalColor : new Color((int) (finalColor.getRed()*0.1), (int) (finalColor.getGreen()*0.1), (int) (finalColor.getBlue()*0.1)); // Or calculate with lighting, if available

            //Color localColor = ignoreShape.getColor();
            Color refractedColor = castRefraction(hit.point, hit.getNormal(), hit.object, recursionLimit - 1);

            //double transparency = ignoreShape.transparency;
            double transparency = hit.object.transparency;

            int r = clamp((int)(localColor.getRed() * (1 - transparency) + refractedColor.getRed() * transparency), 0, 255);
            int g = clamp((int)(localColor.getGreen() * (1 - transparency) + refractedColor.getGreen() * transparency), 0, 255);
            int b = clamp((int)(localColor.getBlue() * (1 - transparency) + refractedColor.getBlue() * transparency), 0, 255);

            return new Color(r, g, b);
        }

        return Scene.background;
    }



}