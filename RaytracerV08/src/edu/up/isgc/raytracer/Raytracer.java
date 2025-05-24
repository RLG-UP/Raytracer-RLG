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

        int width = 1600;
        int height = 900;


/*

        int width = 4096;
        int height = 2160;

 */



        double nearPlane = -1000, farPlane = 1000;
        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\Hat.obj").getAbsolutePath();
        //String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\optimized-3d-capturedphotogrammetry-hat\\Hat.mtl").getAbsolutePath();

        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\carl-manfred-detroit-become-human\\CarlManfred.obj").getAbsolutePath();
        //String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\carl-manfred-detroit-become-human\\CarlManfred.mtl").getAbsolutePath();

        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pilot_Scene\\Hazmat\\Obj\\tripo_pbr_model_a7cac90a-deb1-4ce8-9cef-093d5db3efea.obj").getAbsolutePath();
        //String mtlPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pilot_Scene\\Hazmat\\Obj\\tripo_pbr_model_a7cac90a-deb1-4ce8-9cef-093d5db3efea.mtl").getAbsolutePath();

        //String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Sentence_Splashes.obj").getAbsolutePath();

        //Calibration
        String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Calibration\\Calibrateobj.obj").getAbsolutePath();

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

        Scene scene = new Scene(new Color(58,58,58));
        //Scene scene = new Scene(Color.white);
        String groundPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Ground.obj").getAbsolutePath();
        String greenSoldiersPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Green_Soldiers.obj").getAbsolutePath();
        String redSoldiersPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Red_Soldiers.obj").getAbsolutePath();
        String truckAndTurretPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\TruckAndTurret.obj").getAbsolutePath();
        String boxesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Boxes.obj").getAbsolutePath();
        String tanksPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Tanks.obj").getAbsolutePath();
        String armouredTrucksPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\ArmoredTrucks.obj").getAbsolutePath();

        BufferedImage armouredTrucksTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\apcf.obj\\apcf_1.png"));
        //Obj.RenderObj(scene, groundPath, Material.GROUND(new Color(140,106,86)));
        //Obj.RenderObj(scene, groundPath, groundMTL);

        //GOOD GUYS
        String brownBunniesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\BrownBunnies.obj").getAbsolutePath();
        BufferedImage brownBunniesTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\TwoBunnies\\brownTexture.jpg"));

        String whiteBunniesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\WhiteBunnies.obj").getAbsolutePath();
        BufferedImage whiteBunniesTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\TwoBunnies\\whiteTexture.jpg"));

        String orangeToyGun_BodyPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\OrangeToyGun_Body.obj").getAbsolutePath();
        String blackToyGun_DetailsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\BlackToyGun_Details.obj").getAbsolutePath();
        String coinsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Coins.obj").getAbsolutePath();

        String teddyBearPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\TeddyBear.obj").getAbsolutePath();
        BufferedImage teddyBearTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\Bear_obj\\texbear.png"));

        String coolAssSoldierPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\CoolAssSoldier.obj").getAbsolutePath();

        String greenChamaleonPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\GreenChamaleon.obj").getAbsolutePath();
        BufferedImage greenChamaleonTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\Suse_3dscan_obj\\tex\\suse_scan_aligned_v01.png"));

        String rocketPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Rocket.obj").getAbsolutePath();
        BufferedImage rocketTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Toy Rocket 4K\\Toy Rocket_Toy Ship_BaseColor.png"));

        //Polygon greenChamaleon = new Polygon(greenChamaleonPath, Color.BLACK, 0.0, 0.0, greenChamaleonTEX);
        //scene.addPolygon( greenChamaleon );


        Polygon brownBunnies = new Polygon(brownBunniesPath, new Color(95, 100, 100), 0.0, 0.0, brownBunniesTEX);
        Polygon whiteBunnies = new Polygon(whiteBunniesPath, new Color(95, 100, 100), 0.0, 0.0, whiteBunniesTEX);
        Polygon teddyBear = new Polygon(teddyBearPath, Color.BLACK, 0.0, 0.0, teddyBearTEX);
        Polygon rocket = new Polygon(rocketPath, Color.BLACK, 1, 0.0, rocketTEX);
        scene.addPolygon( brownBunnies );
        scene.addPolygon( whiteBunnies );
        scene.addPolygon( teddyBear );
        scene.addPolygon( rocket );

        Obj.RenderObj(scene, orangeToyGun_BodyPath, Material.PLASTIC(new Color(150, 10, 10)));
        Obj.RenderObj(scene, blackToyGun_DetailsPath, Material.PLASTIC(new Color(5,5,5)));
        Obj.RenderObj(scene, coolAssSoldierPath, Material.PLASTIC(new Color(10,60,120)));
        Obj.RenderObj(scene, coinsPath, Material.PLASTIC(new Color(150,100,0)));
        Obj.RenderObj(scene, greenChamaleonPath, Material.GROUND(new Color(3,10,1)));



        //BAD GUYS
        //Obj.RenderObj(scene, objPath, Material.GROUND(new Color(130,97,92)));
        //Obj.RenderObj(scene, truckAndTurretPath, Material.PLASTIC(new Color(63, 15, 11)));
        //Obj.RenderObj(scene, tanksPath, Material.PLASTIC(new Color(90,96,69)));


        Obj.RenderObj(scene, greenSoldiersPath, Material.PLASTIC(new Color(3,48,15)));
        Obj.RenderObj(scene, redSoldiersPath, Material.PLASTIC(new Color(63, 15, 11)));
        Obj.RenderObj(scene, boxesPath, Material.PLASTIC(new Color(42,48,30)));
        Polygon armouredTrucks = new Polygon(armouredTrucksPath, Color.BLACK, 0.0, 0.0, armouredTrucksTEX);
        Polygon tanks = new Polygon(tanksPath, Color.BLACK, 0.0, 0.0, armouredTrucksTEX);
        Polygon truckAndTurret = new Polygon(truckAndTurretPath, Color.BLACK, 0.0, 0.0, armouredTrucksTEX);
        scene.addPolygon( armouredTrucks );
        scene.addPolygon( tanks );
        scene.addPolygon( truckAndTurret );




        //String objPath03 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Background.obj").getAbsolutePath();
        //Obj.RenderObj(scene, path, Material.GLASS(Color.WHITE));
        Polygon polygon = new Polygon(path, Material.MIRROR(Color.WHITE));
        polygon.rotate((float)90, (float)0, (float)0);
        scene.addPolygon(polygon);
        Obj.RenderObj(scene, path, Material.MIRROR(Color.RED));

        //Obj.RenderObj(scene, armouredTrucksPath, armouredTrucksMTL, null);


        //Obj.RenderObj(scene, truckAndTurretPath, truckAndTurretMTL, null);
        //Lights

        //Light light01 = new Point(25f, Color.WHITE, new Vector3D(-6, 2, -3));
        //Light light02 = new Point(5f, new Color(192,167,156), new Vector3D(3, 0.5, -7));
        Light light03 = new Point(2f, Color.WHITE, new Vector3D(-1, 0.2, -2.5));
        Light light04 = new Point(2f, Color.WHITE, new Vector3D(1.75, 1.2, -2));
        Light light05 = new Point(0.1f, new Color(192,167,156), new Vector3D(0.5, 0, -5));
        Light light06 = new Point(1f, new Color(192,167,156), new Vector3D(-1, 0, -4));
        //Light light05 = new Point(10f, Color.WHITE, new Vector3D(-3, 1, -5));
        //Light light06 = new Point(5f, new Color(192,167,156), new Vector3D(-3, 0, -7));

        //Light light05 = new Spot(5f, new Color(192,167,156), new Vector3D(0, 5, 0), new Vector3D(0, 0, 0), 1f, 1f);


        //Light light0D = new Directional(1, Color.white, new Vector3D(10,0,10), new Vector3D(0, 0, 0));
        //Light light0S= new Spot(1f, new Color(246,220,189),new Vector3D(20,10,10), new Vector3D(0, 0, 0), 1f, 1f);
/*
        Light light01 = new Spot(1f, new Color(246,220,189), new Vector3D(-6, -1.5, -7), new Vector3D(0, 0, 0), 1f, 1f);
        Light light02 = new Spot(1f, new Color(246,220,189), new Vector3D(6, -1.5, -7), new Vector3D(0, 0, 0), 1f, 1f);
        Light light03 = new Spot(1f, new Color(246,220,189), new Vector3D(-6, 5, 3), new Vector3D(0, 0, 0), 1f, 1f);


 */


        //Camera camera = new Camera(new Vector3D(-0.005, -0.005, -5.5), nearPlane, farPlane, 30, width, height);
        Camera camera = new Camera(new Vector3D(-0.2, -0.005, -5.6), nearPlane, farPlane, 30, width, height);

        Renderer.renderScene(width, height, camera, scene);

    }

}