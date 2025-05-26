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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Renderer {
    //private static int totalY = 0, lastProgress=-1;
    private static final AtomicInteger renderedPixels = new AtomicInteger(0);
    private static int lastProgress = -1;
    private static final Object progressLock = new Object();

    public static void renderScene(int width, int height, Camera camera, Scene scene) {
        // Start timing
        long startTime = System.currentTimeMillis();
        System.out.println("Rendering started at: " + startTime + " ms");

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Optional: Print progress for large renders
        System.out.println("Rendering " + width + "x" + height + " image...");
        int lastProgress = -1;

        int N_THREADS = (int)(Runtime.getRuntime().availableProcessors()*0.8);
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        int splitWidth = width / N_THREADS;
        int splitHeight = height / N_THREADS;
        List<Future<?>> futures = new ArrayList<>();


        for (int y = 0; y < height; y+=splitHeight) {
            for (int x = 0; x < width; x+=splitWidth) {
                final int tileX = x;
                final int tileY = y;
                final int tileW = Math.min(splitWidth, width-x);
                final int tileH = Math.min(splitHeight, height-y);

                futures.add(executor.submit(() -> {Renderer.paintSplit(image, camera, scene, tileX, tileY, tileW, tileH); }));
            }
        }
        for(Future<?> future : futures) {
            try{
                future.get();
            }catch(InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

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
    public static void paintSplit(BufferedImage renderImage, Camera camera, Scene scene, int x, int y, int tileW, int tileH){
        double aspectRatio = (double) renderImage.getWidth() / renderImage.getHeight();
        double tanFov = Math.tan(Math.toRadians(camera.getFov()) / 2.0);
        for(int tY= 0; tY<tileH; tY++){

            for(int tX=0; tX<tileW; tX++){

                int pX = tX + x;
                int pY = tY + y;

                double ndcX = (2.0 * (pX + 0.5) / renderImage.getWidth() - 1.0);
                double ndcY = (1.0 - 2.0 * (pY + 0.5) / renderImage.getHeight());

                // Apply aspect ratio and FOV scaling
                double u = ndcX * aspectRatio * tanFov;
                double v = ndcY * tanFov;


                //Ray ray = camera.generateRay(px, py);
                Ray ray = camera.generateRay(u, v);
                Intersection intersection = scene.findClosestIntersection(ray, camera);

                Color pixelColor;
                if (intersection != null && intersection.color != null && intersection.object != null) {
                    intersection.color = intersection.object.addLight(intersection);
                    pixelColor = intersection.color;
                } else {
                    pixelColor = Scene.background;
                }

                renderImage.setRGB(pX, renderImage.getHeight() - pY -1, pixelColor.getRGB());
                int pixelsRendered = renderedPixels.incrementAndGet();
                updateProgress(pixelsRendered, renderImage.getWidth()*renderImage.getHeight());
            }
        }
    }
    private static void updateProgress(int rendered, int total) {
        int progress = (int) ((rendered / (double) total) * 100);

        synchronized (progressLock) {
            if (progress != lastProgress && progress % 5 == 0) {  // Update every 5%
                System.out.println("Progress: " + progress + "%");
                lastProgress = progress;
            }
        }
    }
}