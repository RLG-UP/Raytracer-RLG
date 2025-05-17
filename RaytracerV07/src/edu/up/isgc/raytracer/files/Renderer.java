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
                double u = (x - width / 2.0) / width;
                double v = (y - height / 2.0) / height;

                Ray ray = camera.generateRay(u, v);
                Intersection intersection = scene.findClosestIntersection(ray, camera);

                Color pixelColor;
                if (intersection != null && intersection.color != null) {
                    intersection.color = intersection.object.addLight(intersection.point);
                    pixelColor = intersection.color;
                } else {
                    pixelColor = new Color(0, 0, 0); // background
                }

                image.setRGB(x, y, pixelColor.getRGB());
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
