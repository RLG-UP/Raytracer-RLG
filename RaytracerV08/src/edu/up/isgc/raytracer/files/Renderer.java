package edu.up.isgc.raytracer.files;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.world.Scene;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class Renderer {
    public static void renderScene(int width, int height, Camera camera, Scene scene) {
        // Start timing
        long startTime = System.currentTimeMillis();
        System.out.println("Rendering started at: " + startTime + " ms");

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Optional: Print progress for large renders
        System.out.println("Rendering " + width + "x" + height + " image...");
        int lastProgress = -1;

        for (int y = 0; y < height; y++) {
            // Calculate and print progress percentage
            int progress = (int)((y / (double)height) * 100);
            if (progress != lastProgress && progress % 10 == 0) {
                System.out.println("Progress: " + progress + "%");
                lastProgress = progress;
            }

            for (int x = 0; x < width; x++) {
                double aspectRatio = (double) width / height;
                double scale = 1.0;

                double px = (2 * ((x + 0.5) / width) - 1) * aspectRatio * scale;
                double py = (1 - 2 * ((y + 0.5) / height)) * scale;

                Ray ray = camera.generateRay(px, py);
                Intersection intersection = scene.findClosestIntersection(ray, camera);

                Color pixelColor;
                if (intersection != null && intersection.color != null && intersection.object != null) {
                    intersection.color = intersection.object.addLight(intersection);
                    pixelColor = intersection.color;
                } else {
                    pixelColor = Scene.background;
                }

                image.setRGB(x, height - y - 1, pixelColor.getRGB());
            }
        }

        try {
            File outputfile = new File("output.png");
            ImageIO.write(image, "png", outputfile);

            // End timing and calculate duration
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Convert to minutes and seconds if over 60 seconds
            if (duration > 60000) {
                long minutes = duration / 60000;
                long seconds = (duration % 60000) / 1000;
                System.out.println("Rendering completed in: " + minutes + "m " + seconds + "s");
            } else if (duration > 1000) {
                double seconds = duration / 1000.0;
                System.out.println("Rendering completed in: " + String.format("%.2f", seconds) + "s");
            } else {
                System.out.println("Rendering completed in: " + duration + "ms");
            }

            System.out.println("Image saved as output.png");
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }
}