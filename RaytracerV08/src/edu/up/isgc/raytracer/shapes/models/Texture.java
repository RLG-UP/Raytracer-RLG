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

        Vector3D e1 = Vector3D.subtract(v1,v2).scale(-1);
        Vector3D e2 = Vector3D.subtract(v1,v3).scale(-1);
        Vector3D eP = Vector3D.subtract(v1,point).scale(-1);

        /*
        Vector3D triNormal = Vector3D.crossProduct(e1,e2).normalize();
        Vector3D viewDir = Vector3D.subtract(point, Camera.getCameraPosition()).normalize();

         */

        double d11 = e1.dot(e1);
        double d12 = e1.dot(e2);
        double d22 = e2.dot(e2);
        double d1P = e1.dot(eP);
        double d2P = e2.dot(eP);

        double denominator = (d11*d22)-(d12*d12);
        if (Math.abs(denominator) < 1e-10) return Vector3D.getZero();

        double v = ( (d22*d1P)-(d12*d2P) )/denominator;
        double w = ( (d11*d2P)-(d12*d1P) )/denominator;
        double u = 1.0-v-w;


        /*

        v = clamp(v, 0, 1);
        w = clamp(w, 0, 1);
        u = clamp(u, 0, 1);
         */
        //System.out.println("u: " + u + " v: " + v + " w: " + w);

        /*

        double den = ( (v2.y - v3.y) * (v1.x - v3.x) ) + ( (v3.x - v2.x) * (v1.y - v3.y) );
        double v = (( (v2.y - v3.y) * (point.x - v3.x) ) + ( (v3.x - v2.x) * (point.y - v3.y) )) / den;
        double w = (( (v3.y - v1.y) * (point.x - v3.x) ) + ( (v1.x - v3.x) * (point.y - v3.y) )) / den;
        double u = 1 - v - w;

         */


        //System.out.println("u:" + u + " v:" + v + " w:" + w);
        //if(u < 0 || u > 1 || v < 0 || v > 1 || w < 0 || w > 1) return Vector3D.getZero();
        /*
        Vector3D uN = n1.scale(u);
        Vector3D vN = n2.scale(v);
        Vector3D wN = n3.scale(w);

         */

        //return uN.add(vN).add(wN).normalize();
        //return n1.scale(u).add(n2.scale(v)).add(n3.scale(w)).normalize();
        //return n1.add(n2).add(n3).scale(1.0/3.0).normalize();

        double texU = u * n1.x + v * n2.x + w * n3.x;
        double texV = u * n1.y + v * n2.y + w * n3.y;

        return new Vector3D(texU, texV, 0);
    }
}
