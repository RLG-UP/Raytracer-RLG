package edu.up.isgc.raytracer;

import java.awt.Color;
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

        // Create scene with objects
        Scene scene = new Scene();
        scene.addObject(new Sphere(new Vector3D(-0.5, 0, -3), 0.3, Color.RED));
        scene.addObject(new Sphere(new Vector3D(0.4, 0, -3), 0.1, Color.BLUE));

        // Set up camera at the origin
        Camera camera = new Camera(new Vector3D(0, 0, 0));

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
                Intersection intersection = scene.findClosestIntersection(ray);

                if (intersection != null) {
                    // Set pixel color to intersected object's color
                    image[x][y][0] = intersection.color.getRed();
                    image[x][y][1] = intersection.color.getGreen();
                    image[x][y][2] = intersection.color.getBlue();
                } else {
                    // Default to white background if no intersection
                    image[x][y][0] = 255;
                    image[x][y][1] = 255;
                    image[x][y][2] = 255;
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