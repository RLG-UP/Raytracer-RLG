package edu.up.isgc.raytracer.shapes.models;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;
import java.util.ArrayList;

public class Texture {
    private static ArrayList<Double[]> textures = new ArrayList<>();
    public Texture (Double a, Double b, Double c){
        Texture.addTexture(new Double[]{a,b,c});
    }

    public static void clear() {
        textures.clear();
    }
    public static void addTexture(Double[] vertex){ textures.add(vertex); }
    public static ArrayList<Double[]> getTexture(){ return Texture.textures; }

    public static Vector3D ericson(Vector3D point, Vector3D v1, Vector3D v2, Vector3D v3, Vector3D n1, Vector3D n2, Vector3D n3) {

        Vector3D e1 = Vector3D.subtract(v1, v2).scale(-1);
        Vector3D e2 = Vector3D.subtract(v1, v3).scale(-1);
        Vector3D eP = Vector3D.subtract(v1, point).scale(-1);

        double d11 = e1.dot(e1);
        double d12 = e1.dot(e2);
        double d22 = e2.dot(e2);
        double d1P = e1.dot(eP);
        double d2P = e2.dot(eP);

        double denominator = (d11 * d22) - (d12 * d12);
        if (Math.abs(denominator) < 1e-10) return Vector3D.getZero();

        double v = ((d22 * d1P) - (d12 * d2P)) / denominator;
        double w = ((d11 * d2P) - (d12 * d1P)) / denominator;
        double u = 1.0 - v - w;
        
        double texU = u * n1.x + v * n2.x + w * n3.x;
        double texV = u * n1.y + v * n2.y + w * n3.y;

        return new Vector3D(texU, texV, 0);
    }
}
