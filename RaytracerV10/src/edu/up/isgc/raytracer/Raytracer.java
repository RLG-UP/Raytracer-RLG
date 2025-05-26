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

        double nearPlane = -1000, farPlane = 1000;

        //TEST SCENE FOR MOYA ;D
        Scene scene = new Scene(Color.BLACK);
        String hatPath = new File("./OBJS/Hat.obj").getAbsolutePath();

        String wheeliePath = new File("./OBJS/CarlManfred.obj").getAbsolutePath();
        String wheelieMTLPath = new File("./OBJS/CarlManfred.mtl").getAbsolutePath();

        String hazmatPath = new File("./OBJS/SmallTeapot.obj").getAbsolutePath();

        //POLYGONS

        //MTL File reading
        Obj.RenderObj(scene, wheeliePath, wheelieMTLPath, null,
                new Vector3D(0,180,0),
                new Vector3D(0.5f,0.5f,0.5f),
                new Vector3D(0,-1,0));

        //TEXTURE READING
        BufferedImage texture = ImageIO.read(new File("./OBJS/Hat_Albedo.png"));
        Polygon hat = new Polygon(hatPath, new Color(220, 20, 60), 0.4, 0.3, texture);
        hat.scale(0.4f,0.4f,0.4f);
        hat.translate(0.04f,0.75f,-0.3f);
        hat.rotate(0,180,0);
        scene.addPolygon( hat );

        //MATERIAL ADDITION TO OBJ
        Obj.RenderObj(scene, hazmatPath,Material.GLASS(Color.RED), //REFLECTION
                new Vector3D(0,90,0),
                new Vector3D(0.7f,0.7f,0.7f),
                new Vector3D(-1.6,-0.2,-0.1));
        //
        Obj.RenderObj(scene, hazmatPath,Material.MIRROR(Color.BLUE), //REFRACTION
                new Vector3D(0,90,0),
                new Vector3D(0.7f,0.7f,0.7f),
                new Vector3D(-1.2,-0.6,-0.1));

        Obj.RenderObj(scene, hazmatPath,Material.METAL(Color.YELLOW), //PHONG
                new Vector3D(0,0,0),
                new Vector3D(0.7f,0.7f,0.7f),
                new Vector3D(-1.2,0.3,1));

        //Obj.RenderObj(scene, objPath, Material.GLASS(Color.white));

        //SPHERES
        scene.addObject(new Sphere(new Vector3D(1.5, 0, 0), 0.5, Material.METAL(Color.YELLOW))); //PHONG
        scene.addObject(new Sphere(new Vector3D(0, 0.5, 1), 0.5, Material.METAL(Color.GREEN))); //SHADOW
        scene.addObject(new Sphere(new Vector3D(0, 0.5, 5), 3, Material.METAL(Color.LIGHT_GRAY))); //SHADOW
        scene.addObject(new Sphere(new Vector3D(1, 0, 0), 0.5, Material.GLASS(Color.RED))); //REFRACTION
        scene.addObject(new Sphere(new Vector3D(1.25, 0.5, 0), 0.5, Material.MIRROR(Color.BLUE))); //REFLECTION


        //LIGHTS
        Light light0P = new Point(1f, Color.WHITE, new Vector3D(1.25, 0.2, -1)); //POINT

        //TEST THE OTHER LIGHTS HERE!!!!!!!
        //Light light0D = new Directional(1, Color.WHITE, new Vector3D(0,0,-10), new Vector3D(2, 0, 0)); //DIRECTIONAL

        /*
        Light light0S01 = new Spot(0.5f, Color.WHITE,   //Spot
                new Vector3D(0.5, 1, 1),
                new Vector3D(0, 0.2, -1),
                0.5f, 1f);
        */

        Camera camera = new Camera(new Vector3D(-0.005, -0.005, -5.5), nearPlane, farPlane, 30, width, height);
        Renderer.renderScene(width, height, camera, scene);
    }

}