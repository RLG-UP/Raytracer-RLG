package edu.up.isgc.raytracer;

import edu.up.isgc.raytracer.files.Obj;
import edu.up.isgc.raytracer.files.Renderer;
import edu.up.isgc.raytracer.lighting.Directional;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.lighting.Point;
import edu.up.isgc.raytracer.lighting.Spot;
import edu.up.isgc.raytracer.shapes.Sphere;
import edu.up.isgc.raytracer.shapes.Triangle;
import edu.up.isgc.raytracer.shapes.models.Polygon;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.world.Scene;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Main ray tracing engine that renders 3D scenes to 2D images.
 * Handles scene setup, camera configuration, and image generation.
 */
public class Raytracer {
    public static void main(String[] args) throws IOException {
        // Image settings
        int width = 1600;
        int height = 900;
        double nearPlane = -1000, farPlane = 1000;
        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\Hat.obj").getAbsolutePath();
        //String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\Hat.mtl").getAbsolutePath();

        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\carl-manfred-detroit-become-human\\CarlManfred.obj").getAbsolutePath();
        //String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\carl-manfred-detroit-become-human\\CarlManfred.mtl").getAbsolutePath();

        String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\carl-manfred-detroit-become-human\\CarlManfred.obj").getAbsolutePath();
        String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\carl-manfred-detroit-become-human\\CarlManfred.mtl").getAbsolutePath();

        //String path2 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\Ring.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\Ring.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\SmallTeapot.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\Hat.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\shark1.obj").getAbsolutePath();

        // Create scene with objects
        Scene scene = new Scene(Color.black);

        //scene.addObject(new Sphere(new Vector3D(0.7, 0, 0), 0.7, Color.lightGray,  0.4, 0.7));
        //scene.addObject(new Sphere(new Vector3D(0, 2, 0), 1, Color.lightGray,  0.4, 0.7));
        //scene.addObject(new Sphere(new Vector3D(0, 0, 0), 50, Color.CYAN,  2.77, 0));

        BufferedImage texture = ImageIO.read(new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\textures\\Hat_Albedo.png"));

        //Polygon polygon = new Polygon(path, new Color(220, 20, 60), 0.4, 0.3, texture);
        //Polygon polygon = new Polygon(path, Color.red, 0.0, 0, texture);


        Obj.RenderObj(scene, objPath, mtlPath);

        //Polygon polygon2 = new Polygon(path2, Color.lightGray, 0.4, 0.7);
        /*
        polygon.rotate(0, -180, 0);
        polygon.translate(0, -0.5f, 0f);
        polygon.scale(2f, 2f, 2f);
        polygon.rotate(10, 0, 0);
         */

        //scene.addPolygon( polygon );
        //scene.addPolygon( polygon2 );

        // Set up camera at the origin
        Camera camera = new Camera(new Vector3D(0, 0, -6), nearPlane, farPlane);
        Light light01 = new Point(10f, Color.white, new Vector3D(2.9896, 1.6178, 3.9184));
        Light light02 = new Point(10f, Color.white, new Vector3D(2.9896, 1.6178, 3.9184));
        Light light03 = new Point(10f, Color.white, new Vector3D(2.9896, 1.6178, 3.9184));

        //Light light01 = new Directional(1, Color.white, new Vector3D(0,10,0), new Vector3D(0, 0, 0));
        //Light light02 = new Directional(100, Color.blue, new Vector3D(0,-10,0), new Vector3D(0,0,0));
        //Light light03 = new Point(10f, Color.white, new Vector3D(0, -1, -2.5));
        //Light light04 = new Spot(1f, Color.white, new Vector3D(0,-4,0), new Vector3D(0,0,0), 1f, 1f);


        Renderer.renderScene(width, height, camera, scene);

    }

}