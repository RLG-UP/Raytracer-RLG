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

        int width = 800;
        int height = 450;


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
        //String path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\Raytracer\\LocalFiles\\ObjFiles\\Ring.obj").getAbsolutePath();
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
        Scene scene = new Scene(new Color(220,20,60));
        //Scene scene = new Scene(Color.BLACK);
        String objPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Shards.obj").getAbsolutePath();
        String objPath01 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Shards2.obj").getAbsolutePath();
        String objPath02 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Man.obj").getAbsolutePath();
        String objPath02B = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Man3.obj").getAbsolutePath();
        String objPath03 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\Rage_Background2.obj").getAbsolutePath();
        String objPath04 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\BackMirror.obj").getAbsolutePath();
        Obj.RenderObj(scene, objPath, Material.MIRROR(Color.CYAN));
        Obj.RenderObj(scene, objPath01, Material.MIRROR(Color.CYAN));
        Obj.RenderObj(scene, objPath02B, Material.GLASS(new Color(1,1,1)));
        Obj.RenderObj(scene, objPath02, Material.RAGE_GLASS(new Color(1,1,1)));
        Obj.RenderObj(scene, objPath03, Material.MIRROR(Color.CYAN));
        Obj.RenderObj(scene, objPath04, Material.MIRROR(Color.CYAN));
        Camera camera = new Camera(new Vector3D(-0.005, -0.005, -5.5), nearPlane, farPlane, 30, width, height);

        //RAGE Lights
        Light light01 = new Point(1f, Color.WHITE, new Vector3D(0, 1, -10));
        Light light02 = new Point(2.5f, Color.WHITE, new Vector3D(0, -1.6593, -7));
        //Light light04 = new Spot(1f, Color.white, new Vector3D(0,0,-10), new Vector3D(4, 0, 0), 1f, 1f);
        //Light light05 = new Spot(1f, Color.white, new Vector3D(0,0,-10), new Vector3D(-4, 0, 0), 1f, 1f);


        // Set up camera at the origin
        //Camera camera = new Camera(new Vector3D(-0.008, -0.005, -1.5), nearPlane, farPlane, 60, width, height);
        //Camera camera = new Camera(new Vector3D(-0.005, -0.005, -4.75), nearPlane, farPlane, 30, width, height);

        //Light light0D = new Directional(1, Color.white, new Vector3D(0,0,10), new Vector3D(2, 0, 0));
        //Light light02 = new Directional(100, Color.blue, new Vector3D(0,-10,0), new Vector3D(0,0,0));
        //Light light03 = new Point(10f, Color.white, new Vector3D(0, -1, -2.5));
*/

/*
        //War Is Childs Play Scene

        Scene scene = new Scene(new Color(158,158,158));
        String groundPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Ground.obj").getAbsolutePath();
        String greenSoldiersPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Green_Soldiers.obj").getAbsolutePath();
        String redSoldiersPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Red_Soldiers.obj").getAbsolutePath();
        String truckAndTurretPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\TruckAndTurret.obj").getAbsolutePath();
        String boxesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Boxes.obj").getAbsolutePath();
        String tanksPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Tanks.obj").getAbsolutePath();
        String armouredTrucksPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\ArmoredTrucks.obj").getAbsolutePath();

        BufferedImage armouredTrucksTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\apcf.obj\\apcf_1.png"));
        Obj.RenderObj(scene, groundPath, Material.GROUND(new Color(140,106,86)));
        //Obj.RenderObj(scene, groundPath, groundMTL);

        //GOOD GUYS

        String whiteBunniesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\WhiteBunnies.obj").getAbsolutePath();
        String bunniesEarsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\BunniesEars.obj").getAbsolutePath();
        String bunniesEyesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\BunniesEyes.obj").getAbsolutePath();
        String bunniesNosesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\BunniesNoses.obj").getAbsolutePath();
        BufferedImage whiteBunniesTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\TwoBunnies\\whiteTexture2.jpg"));

        String eyesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Eyes.obj").getAbsolutePath();
        String eyes02Path = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Eyes02.obj").getAbsolutePath();

        String orangeToyGun_BodyPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\OrangeToyGun_Body.obj").getAbsolutePath();
        String blackToyGun_DetailsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\BlackToyGun_Details.obj").getAbsolutePath();
        String coinsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Coins.obj").getAbsolutePath();

        String teddyBearPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\TeddyBear.obj").getAbsolutePath();
        BufferedImage teddyBearTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\Bear_obj\\texbear.png"));

        String coolAssSoldierPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\CoolAssSoldier.obj").getAbsolutePath();

        String greenChamaleonPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\GreenChamaleon.obj").getAbsolutePath();

        String rocketPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Rocket.obj").getAbsolutePath();
        BufferedImage rocketTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Toy Rocket 4K\\Toy Rocket_Toy Ship_BaseColor.png"));

        String wallPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Aftermath_Scene\\Wall.obj").getAbsolutePath();
        Obj.RenderObj(scene, wallPath, Material.GROUND(new Color(60,60,60)));

        Polygon whiteBunnies = new Polygon(whiteBunniesPath, new Color(95, 100, 100), 0.0, 0.0, whiteBunniesTEX);
        Polygon teddyBear = new Polygon(teddyBearPath, Color.BLACK, 0.0, 0.0, teddyBearTEX);
        Polygon rocket = new Polygon(rocketPath, Color.BLACK, 1, 0.0, rocketTEX);
        scene.addPolygon( whiteBunnies );
        scene.addPolygon( teddyBear );
        scene.addPolygon( rocket );

        Obj.RenderObj(scene, orangeToyGun_BodyPath, Material.PLASTIC(new Color(150, 10, 10)));
        Obj.RenderObj(scene, blackToyGun_DetailsPath, Material.PLASTIC(new Color(5,5,5)));
        Obj.RenderObj(scene, coolAssSoldierPath, Material.PLASTIC(new Color(10,60,120)));
        Obj.RenderObj(scene, coinsPath, Material.PLASTIC(new Color(150, 50, 10)));
        Obj.RenderObj(scene, greenChamaleonPath, Material.GROUND(new Color(3,10,1)));
        Obj.RenderObj(scene, bunniesEarsPath, Material.GROUND(Color.PINK));
        Obj.RenderObj(scene, bunniesEyesPath, Material.PLASTIC(Color.BLACK));
        Obj.RenderObj(scene, bunniesNosesPath, Material.GROUND(Color.PINK));
        Obj.RenderObj(scene, eyesPath, Material.PLASTIC(Color.BLACK));
        Obj.RenderObj(scene, eyes02Path, Material.PLASTIC(Color.GREEN));



        //BAD GUYS

        Obj.RenderObj(scene, greenSoldiersPath, Material.PLASTIC(new Color(3,48,15)));
        Obj.RenderObj(scene, redSoldiersPath, Material.PLASTIC(new Color(63, 15, 11)));
        Obj.RenderObj(scene, boxesPath, Material.PLASTIC(new Color(42,48,30)));
        Polygon armouredTrucks = new Polygon(armouredTrucksPath, Color.BLACK, 0.0, 0.0, armouredTrucksTEX);
        Polygon tanks = new Polygon(tanksPath, Color.BLACK, 0.0, 0.0, armouredTrucksTEX);
        Polygon truckAndTurret = new Polygon(truckAndTurretPath, Color.BLACK, 0.0, 0.0, armouredTrucksTEX);
        scene.addPolygon( armouredTrucks );
        scene.addPolygon( tanks );
        scene.addPolygon( truckAndTurret );


        Light light03 = new Point(1f, Color.WHITE, new Vector3D(-1, 0.2, -2.5));
        Light light04 = new Point(5.3f, Color.WHITE, new Vector3D(1.75, 1.2, -2.9));
        Light light05 = new Point(0.5f, new Color(192,167,156), new Vector3D(0.5, 0, -5));
        Light light06 = new Point(0.5f, new Color(192,167,156), new Vector3D(-1, 0, -4));


        Light light0S01 = new Spot(0.5f, Color.WHITE,new Vector3D(1, -3, -2.5), new Vector3D(-1.2, 0.35, -1.9), 0.5f, 1f);
        Light light0S02 = new Spot(10f, Color.WHITE,new Vector3D(-1.3, -2, -4), new Vector3D(0.9, 0.3, -2.5), 0.5f, 1f);
        Light light0S03 = new Spot(1.5f, new Color(192,167,156), new Vector3D(-1.2, -2, -1.0), new Vector3D(1, 2, -3), 0.5f, 1f);
        Light light0S04 = new Spot(1f, new Color(192,167,156),new Vector3D(1, -1.7, 1), new Vector3D(-1.3, 0.1, -4.3), 0.5f, 1f);

        Camera camera = new Camera(new Vector3D(-0.2, -0.005, -5.6), nearPlane, farPlane, 30, width, height);



/*
        //Light light0S_BACK = new Spot(0.2f, new Color(100,140,190),new Vector3D(50, -30, 100), new Vector3D(-2.2, 0.65, -1.3), 0.5f, 1f);
        //Light light05 = new Point(10f, Color.WHITE, new Vector3D(-3, 1, -5));
        //Light light06 = new Point(5f, new Color(192,167,156), new Vector3D(-3, 0, -7));
        //Light light05 = new Spot(5f, new Color(192,167,156), new Vector3D(0, 5, 0), new Vector3D(0, 0, 0), 1f, 1f);
        //Light light0D = new Directional(1, Color.white, new Vector3D(-2,-3,0), new Vector3D(0, 0, 0));
        //Light light0S= new Spot(1f, new Color(246,220,189),new Vector3D(20,10,10), new Vector3D(0, 0, 0), 1f, 1f);
        Light light01 = new Spot(1f, new Color(246,220,189), new Vector3D(-6, -1.5, -7), new Vector3D(0, 0, 0), 1f, 1f);
        Light light02 = new Spot(1f, new Color(246,220,189), new Vector3D(6, -1.5, -7), new Vector3D(0, 0, 0), 1f, 1f);
        Light light03 = new Spot(1f, new Color(246,220,189), new Vector3D(-6, 5, 3), new Vector3D(0, 0, 0), 1f, 1f);

 */


        //Pandemonium Scene
        Scene scene = new Scene(Color.BLACK);

        //NINJA
        String ninjaHelmetPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\NinjaHelmet.obj").getAbsolutePath();
        String ninjaBodyPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\NinjaBody.obj").getAbsolutePath();
        String ninjaArmsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\NinjaArms.obj").getAbsolutePath();
        String ninjaSkullPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\NinjaSkull.obj").getAbsolutePath();
        String ninjaLightPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\NinjaLight.obj").getAbsolutePath();
        String objPath04 = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Scream_Scene\\RenderOBJS\\BackMirror.obj").getAbsolutePath();
        BufferedImage armouredTrucksTEX = ImageIO.read(new File("C:\\Users\\rodlo\\Downloads\\Aftermath\\Added\\apcf.obj\\apcf_1.png"));


        //new Color(10,15,15)
        Obj.RenderObj(scene, ninjaHelmetPath, Material.NINJA_METAL(new Color(5,7,7)));
        Obj.RenderObj(scene, ninjaBodyPath, Material.NINJA_METAL(new Color(5,7,7)));
        Obj.RenderObj(scene, ninjaArmsPath, Material.RAGE_GLASS(new Color(5,7,7)));
        Obj.RenderObj(scene, ninjaSkullPath, Material.RAGE_GLASS(new Color(255,10,30)));
        Obj.RenderObj(scene, ninjaLightPath, Material.NINJA_METAL(new Color(5,7,7)));
        Obj.RenderObj(scene, objPath04, Material.MIRROR(Color.WHITE));

        //SWORD

        String swordBladePath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\Sword_Blade.obj").getAbsolutePath();
        String swordBladeBPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\Sword_BladeB.obj").getAbsolutePath();
        String swordBladeDetailsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\Sword_Details.obj").getAbsolutePath();
        String swordBladeDetailsBPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\Sword_DetailsB.obj").getAbsolutePath();
        String swordHandlePath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\Sword_Handle.obj").getAbsolutePath();
        String swordSupportPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\Sword_Support.obj").getAbsolutePath();
        String swordSupportBPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\Sword_SupportB.obj").getAbsolutePath();
        Obj.RenderObj(scene, swordBladePath, Material.NINJA_GLASS(new Color(255,10,30)));
        Obj.RenderObj(scene, swordBladeBPath, Material.PANDEMONIUM_MIRROR(Color.WHITE));

        Obj.RenderObj(scene, swordBladeDetailsPath, Material.PANDEMONIUM_METAL(new Color(255,10,30)));
        Obj.RenderObj(scene, swordBladeDetailsBPath, Material.PANDEMONIUM_METAL(new Color(255,10,30)));
        Obj.RenderObj(scene, swordHandlePath, Material.PANDEMONIUM_METAL(new Color(5,7,7)));
        Obj.RenderObj(scene, swordSupportPath, Material.PANDEMONIUM_METAL(new Color(5,7,7)));
        Obj.RenderObj(scene, swordSupportBPath, Material.PANDEMONIUM_METAL(new Color(255,10,30)));



        //EVIL MECHA
/*
        String ballMechaCablesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\BallMecha_Cables.obj").getAbsolutePath();
        String ballMechaCasesPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\BallMecha_Cases.obj").getAbsolutePath();
        String ballMechaCentralPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\BallMecha_Central.obj").getAbsolutePath();
        String ballMechaCoilsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\BallMecha_Coils.obj").getAbsolutePath();
        String ballMechaDisksPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\BallMecha_Disks.obj").getAbsolutePath();
        String ballMechaMetalPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\BallMecha_Metal.obj").getAbsolutePath();
        String ballMechaRodsPath = new File("W:\\-UP_PC-\\4th_SEMESTER\\MMCG_FOURTH_SEMESTER_RLG\\RayTracer_Objs\\Pandemonium_Scene\\RENDER_Objs\\BallMecha_Rods.obj").getAbsolutePath();

        Obj.RenderObj(scene, ballMechaCablesPath, Material.PLASTIC(Color.LIGHT_GRAY));
        Obj.RenderObj(scene, ballMechaCasesPath, Material.ALBEDO(Color.LIGHT_GRAY));
        Obj.RenderObj(scene, ballMechaCentralPath, Material.PLASTIC(Color.LIGHT_GRAY));
        Obj.RenderObj(scene, ballMechaCoilsPath, Material.ALBEDO(Color.LIGHT_GRAY));
        Obj.RenderObj(scene, ballMechaDisksPath, Material.ALBEDO(Color.LIGHT_GRAY));
        Obj.RenderObj(scene, ballMechaMetalPath, Material.PANDEMONIUM_METAL(new Color(0,2,2)));
        Obj.RenderObj(scene, ballMechaRodsPath, Material.ALBEDO(Color.LIGHT_GRAY));
 */

        Light light01 = new Point(1f, Color.WHITE, new Vector3D(0, 0.5, 0));
        Light light02 = new Point(0.5f, Color.WHITE, new Vector3D(0, 1, 0.2));
        Light light03 = new Point(30f, new Color(255,10,30), new Vector3D(0.3, -0.6, -1.17));
        //Light light04 = new Point(30f, new Color(255,10,30), new Vector3D(0.3, -0.6, -1.2));
        Light light05 = new Point(75f, new Color(255,10,30), new Vector3D(0.2, -0.8, -1.08));
        Light light06 = new Point(10f, new Color(255,10,30), new Vector3D(0.3, -0, -1.17));
        Light light0S= new Spot(3f, Color.WHITE ,new Vector3D(-10,-20,0), new Vector3D(1.5, 1.5, -3), 1f, 1f);
        Camera camera = new Camera(new Vector3D(-0.005, -0.005, -4.75), nearPlane, farPlane, 30, width, height);
        Renderer.renderScene(width, height, camera, scene);

    }

}