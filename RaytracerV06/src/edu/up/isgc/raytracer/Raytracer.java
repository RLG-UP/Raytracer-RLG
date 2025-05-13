package edu.up.isgc.raytracer;

import edu.up.isgc.raytracer.lighting.Directional;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.lighting.Point;
import edu.up.isgc.raytracer.lighting.Spot;
import edu.up.isgc.raytracer.shapes.Sphere;
import edu.up.isgc.raytracer.shapes.Triangle;
import edu.up.isgc.raytracer.shapes.models.Polygon;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.world.Scene;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Main ray tracing engine that renders 3D scenes to 2D images.
 * Handles scene setup, camera configuration, and image generation.
 */
public class Raytracer {
    public static void main(String[] args) {
        // Image settings
        int width = 400;
        int height = 400;
        double nearPlane = -1000, farPlane = 1000;
        String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\Ring.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\SmallTeapot.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\shark1.obj").getAbsolutePath();

        // Create scene with objects
        Scene scene = new Scene();
        //scene.addObject(new Sphere(new Vector3D(0, 0, 0), 1, Color.RED));
        //scene.addObject(new Sphere(new Vector3D(0, 0, -2), 0.1, Color.BLUE));
        //scene.addObject(new Triangle(new Vector3D(0.4, 0, -3), new Vector3D(0.4, 0.5, -3), new Vector3D(1.1, 0, -3), Color.GREEN));
        Polygon polygon = new Polygon(path, Color.WHITE);

        scene.addPolygon( polygon );

        // Set up camera at the origin
        Camera camera = new Camera(new Vector3D(0, 0, -6), nearPlane, farPlane);
        Light light01 = new Directional(1, Color.white, new Vector3D(0,5,0), new Vector3D(0, 0, 0));
        //Light light02 = new Directional(1, Color.white, new Vector3D(0,10,1), new Vector3D(0,0,0));
        //Light light03 = new Point(1f, Color.white, new Vector3D(0, 1, -11));
        //Light light04 = new Spot(1f, Color.white, new Vector3D(0,0,1), new Vector3D(0,0,0), 1f, 1f);

        /*
        // Instead of top of sphere, pick the front of the sphere
        Vector3D surfacePoint = new Vector3D(0, 0, -4);  // slightly in front of the sphere
        Vector3D normal = new Vector3D(0, 0, 1);         // facing the camera

        // Cast shadow ray to the light behind the sphere
        Vector3D shadowRayDir = Vector3D.subtract(light03.getPosition(), surfacePoint).normalize();
        Vector3D shadowOrigin = surfacePoint.add(normal.scale(Camera.getEpsilon())); // epsilon in view direction
        Ray shadowRay = new Ray(shadowOrigin, shadowRayDir);


        // Check for shadow intersection
        Intersection shadowHit = Scene.findRayIntersection(shadowRay);

        if (shadowHit != null) {
            System.out.println("Shadow ray hit: " + shadowHit.point + " at distance " + shadowHit.distance);
        } else {
            System.out.println("No object hit by shadow ray — no shadow cast.");
        }

         */

        // Initialize image buffer (width x height x RGB)
        int[][][] image = new int[width][height][3];

        // Render each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Convert pixel coordinates to normalized camera space
                double u = (x - width / 2.0) / width;
                double v = (y - height / 2.0) / height;

                // Cast ray and find closest intersection
                Ray ray = camera.generateRay(u, v);
                Intersection intersection = scene.findClosestIntersection(ray, camera);

                if (intersection != null && intersection.color != null) {
                    // Set pixel color to intersected object's color
                    intersection.color = intersection.object.addLight(intersection.point);
                    image[x][y][0] = intersection.color.getRed();
                    image[x][y][1] = intersection.color.getGreen();
                    image[x][y][2] = intersection.color.getBlue();
                } else {
                    // Default to white background if no intersection
                    image[x][y][0] = 0;
                    image[x][y][1] = 0;
                    image[x][y][2] = 0;
                }
            }
        }

        // Save rendered image to PPM file
        savePPM("output.ppm", image, width, height);
    }

    /**
     * Saves the rendered image as a PPM (Portable PixMap) file.
     * @param filename Output file name
     * @param image 3D array containing RGB values
     * @param width Image width in pixels
     * @param height Image height in pixels
     */
    private static void savePPM(String filename, int[][][] image, int width, int height) {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write PPM header
            writer.write("P3\n" + width + " " + height + "\n255\n");

            // Write pixel data
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    writer.write(image[x][y][0] + " " + image[x][y][1] + " " + image[x][y][2] + " ");
                }
                writer.write("\n");
            }
            System.out.println("Image saved as " + filename);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }
}