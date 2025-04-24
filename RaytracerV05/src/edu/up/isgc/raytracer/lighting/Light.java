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

    public static ArrayList<Light> getLights(){return Light.lights;}
}
