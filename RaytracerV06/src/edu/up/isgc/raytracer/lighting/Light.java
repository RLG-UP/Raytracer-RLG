package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.world.Camera;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.clamp;

public abstract class Light {
    private static float ambientLight = 1.0f;
    private float intensity;
    private Color color;
    private Vector3D position;
    protected float attenuation;
    protected static ArrayList<Light> lights = new ArrayList<Light>();

    public Light(float intensity, Color color, Vector3D position) {
        this.setIntensity(intensity);
        this.setColor(color);
        this.setPosition(position);
        lights.add(this);
    }

    public static float[] normalizeColor(Color color) {
        return new float[] { color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f};
    }

    public static float[] returnColor(float[] normalizedColor) {
        return new float[] {normalizedColor[0] * 255.0f, normalizedColor[1] * 255.0f, normalizedColor[2] * 255.0f};
    }

    public static float getAmbientLight() { return ambientLight; }
    public static void setAmbientLight(float ambientLight) { Light.ambientLight = ambientLight; }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public abstract String type();

    public float getAttenuation(){ return this.attenuation; }
    public abstract void setAttenuation(float d);

    public abstract Vector3D getDirection(Vector3D point);
    public Vector3D getDirection() { return this.getDirection(Vector3D.getZero()); }

    /*
    public static Color shine(Color lightColor, Color objectColor, float lambertian){
        float[] nLC = Light.normalizeColor(lightColor);
        float[] nOC = Light.normalizeColor(objectColor);
        float[] nLOC = new float[]{nLC[0] * nOC[0], nLC[1] * nOC[1], nLC[2] * nOC[2]};

        return new Color(clamp(nLOC[0] * lambertian, 0,1.0f), clamp(nLOC[1] * lambertian,0,1.0f), clamp(nLOC[2] * lambertian,0,1.0f));
    }

     */

    // New version: separate diffuse/specular and object color logic
    public static Color shine(Color lightColor, Color objectColor, float intensity, boolean applyObjectColor) {
        float[] nLC = normalizeColor(lightColor);
        float[] nOC = normalizeColor(objectColor);

        float r = nLC[0] * (applyObjectColor ? nOC[0] : 1.0f) * intensity;
        float g = nLC[1] * (applyObjectColor ? nOC[1] : 1.0f) * intensity;
        float b = nLC[2] * (applyObjectColor ? nOC[2] : 1.0f) * intensity;

        return new Color(
                clamp((int)(r * 255), 0, 255),
                clamp((int)(g * 255), 0, 255),
                clamp((int)(b * 255), 0, 255)
        );
    }




    public static Vector3D ericson(Vector3D point, Vector3D v1, Vector3D v2, Vector3D v3, Vector3D n1, Vector3D n2, Vector3D n3) {

        Vector3D e1 = Vector3D.subtract(v2,v1);
        Vector3D e2 = Vector3D.subtract(v3,v1);
        Vector3D eP = Vector3D.subtract(point,v1);

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


        System.out.println("u:" + u + " v:" + v + " w:" + w);
        //if(u < 0 || u > 1 || v < 0 || v > 1 || w < 0 || w > 1) return Vector3D.getZero();

        Vector3D uN = n1.scale(u);
        Vector3D vN = n2.scale(v);
        Vector3D wN = n3.scale(w);

        return uN.add(vN).add(wN).normalize();
        //return n1.scale(u).add(n2.scale(v)).add(n3.scale(w)).normalize();
        //return n1.add(n2).add(n3).scale(1.0/3.0).normalize();
    }

    public static ArrayList<Light> getLights(){return Light.lights;}
}
