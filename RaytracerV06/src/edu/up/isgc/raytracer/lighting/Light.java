package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;
import java.util.ArrayList;

public abstract class Light {
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

    public static Color shine(Color lightColor, Color objectColor, float lambertian){
        float[] nLC = Light.normalizeColor(lightColor);
        float[] nOC = Light.normalizeColor(objectColor);
        float[] nLOC = new float[]{nLC[0] * nOC[0], nLC[1] * nOC[1], nLC[2] * nOC[2]};

        return new Color(nLOC[0] * lambertian, nLOC[1] * lambertian, nLOC[2] * lambertian);
    }

    public static Vector3D ericson(Vector3D point, Vector3D v1, Vector3D v2, Vector3D v3) {
        Vector3D e1 = Vector3D.subtract(v2,v1);
        Vector3D e2 = Vector3D.subtract(v3,v1);
        Vector3D eP = Vector3D.subtract(point,v1);

        Double d11 = e1.dot(e1);
        Double d12 = e1.dot(e2);
        Double d22 = e2.dot(e2);
        Double d1P = e1.dot(eP);
        Double d2P = e2.dot(eP);

        Double denominator = (d11*d22)-(d12*d12);
        Double v = ( (d22*d1P)-(d12*d2P) )/denominator;
        Double w = ( (d11*d2P)-(d12*d1P) )/denominator;
        Double u = 1-v-w;

        Vector3D vV = v1.scale(v);
        Vector3D wV = v2.scale(w);
        Vector3D uV = v3.scale(u);

        return vV.add(wV).add(uV);
    }

    public static ArrayList<Light> getLights(){return Light.lights;}
}
