package edu.up.isgc.raytracer;

import edu.up.isgc.raytracer.files.Obj;
import edu.up.isgc.raytracer.files.Renderer;
import edu.up.isgc.raytracer.lighting.*;
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

        /*
        int width = 1600;
        int height = 900;

         */



        int width = 4096;
        int height = 2160;


        double nearPlane = -1000, farPlane = 1000;
        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\Hat.obj").getAbsolutePath();
        //String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\Hat.mtl").getAbsolutePath();

        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\carl-manfred-detroit-become-human\\CarlManfred.obj").getAbsolutePath();
        //String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\carl-manfred-detroit-become-human\\CarlManfred.mtl").getAbsolutePath();

        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pilot_Scene\\Hazmat\\Obj\\tripo_pbr_model_a7cac90a-deb1-4ce8-9cef-093d5db3efea.obj").getAbsolutePath();
        //String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pilot_Scene\\Hazmat\\Obj\\tripo_pbr_model_a7cac90a-deb1-4ce8-9cef-093d5db3efea.mtl").getAbsolutePath();

        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Sentence_Splashes.obj").getAbsolutePath();

        //Calibration
        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Calibration\\Calibrateobj.obj").getAbsolutePath();

        //String path2 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\Ring.obj").getAbsolutePath();
        String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\Ring.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\SmallTeapot.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\Hat.obj").getAbsolutePath();
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\shark1.obj").getAbsolutePath();

        // Create scene with objects
        //Scene scene = new Scene(Color.white);

        //scene.addObject(new Sphere(new Vector3D(0, 0, 0), 2, Color.green,  0.5, 0.9));
        //scene.addObject(new Sphere(new Vector3D(0, -2, 0), 3, Material.GLASS(Color.WHITE)));
        //scene.addObject(new Sphere(new Vector3D(1, 2, 0), 3, Material.GLASS(Color.RED)));
        //scene.addObject(new Sphere(new Vector3D(0, 2, 0), 1, Color.lightGray,  0.4, 0.7));

        //BufferedImage texture = ImageIO.read(new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\textures\\Hat_Albedo.png"));

        //Polygon polygon = new Polygon(path, new Color(220, 20, 60), 0.4, 0.3, texture);
        //Polygon polygon = new Polygon(path, Color.red, 0.0, 0, texture);


        //Obj.RenderObj(scene, objPath, mtlPath);
        //Obj.RenderObj(scene, objPath, Material.GLASS(Color.white));
        //Obj.RenderObj(scene, objPath, Material.GLASS(Color.red), new Vector3D(0,90,0), new Vector3D(10,10,10), new Vector3D(0.75,0,0));
        //Obj.RenderObj(scene, objPath, Material.GLASS(Color.lightGray), new Vector3D(0,90,0), new Vector3D(10,10,10), new Vector3D(-0.75,0,0));
        //Obj.RenderObj(scene, path, Material.GLASS(Color.RED),new Vector3D(-90,0,0), new Vector3D(2,2,2), new Vector3D(0,0,0));
        //Obj.RenderObj(scene, path, Material.GLASS(Color.blue),new Vector3D(0,0,0), new Vector3D(2,2,2), new Vector3D(0,0,0));

        //Polygon polygon2 = new Polygon(path2, Color.lightGray, 0.4, 0.7);
        /*
        polygon.rotate(0, -180, 0);
        polygon.translate(0, -0.5f, 0f);
        polygon.scale(2f, 2f, 2f);
        polygon.rotate(10, 0, 0);
         */

        //scene.addPolygon( polygon );
        //scene.addPolygon( polygon2 );

        //RAGE SCENE
        /*
        Scene scene = new Scene(new Color(220, 20, 60));
        String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Shards.obj").getAbsolutePath();
        String objPath01 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Shards2.obj").getAbsolutePath();
        String objPath02 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Man.obj").getAbsolutePath();
        String objPath03 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Background.obj").getAbsolutePath();
        Obj.RenderObj(scene, objPath, Material.GLASS(Color.BLACK));
        Obj.RenderObj(scene, objPath01, Material.GLASS(Color.BLACK));
        Obj.RenderObj(scene, objPath02, Material.GLASS(Color.BLACK));
        Obj.RenderObj(scene, objPath03, Material.GLASS(Color.WHITE));
        Camera camera = new Camera(new Vector3D(-0.005, -0.005, -5.5), nearPlane, farPlane, 30, width, height);

        //RAGE Lights
        Light light01 = new Point(5f, Color.white, new Vector3D(0, 1.1, -7));
        Light light02 = new Point(5f, Color.white, new Vector3D(0, -1.6593, -7));
        Light light04 = new Spot(1f, Color.white, new Vector3D(0,0,-10), new Vector3D(4, 0, 0), 1f, 1f);
        Light light05 = new Spot(1f, Color.white, new Vector3D(0,0,-10), new Vector3D(-4, 0, 0), 1f, 1f);
         */

        // Set up camera at the origin
        //Camera camera = new Camera(new Vector3D(-0.008, -0.005, -1.5), nearPlane, farPlane, 60, width, height);
        //Camera camera = new Camera(new Vector3D(-0.005, -0.005, -4.75), nearPlane, farPlane, 30, width, height);

        //Light light0D = new Directional(1, Color.white, new Vector3D(0,0,10), new Vector3D(2, 0, 0));
        //Light light02 = new Directional(100, Color.blue, new Vector3D(0,-10,0), new Vector3D(0,0,0));
        //Light light03 = new Point(10f, Color.white, new Vector3D(0, -1, -2.5));

        Scene scene = new Scene(new Color(220, 20, 60));
        Camera camera = new Camera(new Vector3D(-0.005, -0.005, -5.5), nearPlane, farPlane, 30, width, height);

        Renderer.renderScene(width, height, camera, scene);

    }

}