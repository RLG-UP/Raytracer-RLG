package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public abstract class Light {
    private int intensity;
    private Color color;
    private Vector3D position;

    public Light(int intensity, Color color, Vector3D position) {
        this.setIntensity(intensity);
        this.setColor(color);
        this.setPosition(position);
    }

    public static float[] normalizeColor(Color color) {
        return new float[] { color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f};
    }

    public static float[] returnColor(float[] normalizedColor) {
        return new float[] {normalizedColor[0] * 255.0f, normalizedColor[1] * 255.0f, normalizedColor[2] * 255.0f};
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
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

    public abstract Vector3D getDirection(Vector3D point);
    public Vector3D getDirection() { return this.getDirection(Vector3D.getZero()); }
}
