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
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double aspectRatio = (double) width / height;
                double scale = 1.0; // or use Math.tan(fov / 2)

                double px = (2 * ((x + 0.5) / width) - 1) * aspectRatio * scale;
                double py = (1 - 2 * ((y + 0.5) / height)) * scale;

                Ray ray = camera.generateRay(px, py);
                Intersection intersection = scene.findClosestIntersection(ray, camera);

                //if(intersection != null && intersection.object != null)System.out.println("Found intersection with " + intersection.object);
                Color pixelColor;
                if (intersection != null && intersection.color != null && intersection.object != null) {
                    intersection.color = intersection.object.addLight(intersection.point);
                    pixelColor = intersection.color;
                } else {
                    pixelColor = new Color(0, 0, 0);
                }

                image.setRGB(x, height - y - 1, pixelColor.getRGB());
            }
        }

        try {
            File outputfile = new File("output.png");
            ImageIO.write(image, "png", outputfile);
            System.out.println("Image saved as output.png");
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }
}
