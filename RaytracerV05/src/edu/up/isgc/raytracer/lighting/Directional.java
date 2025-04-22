package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public class Directional extends Light{
    private Color color;
    private Vector3D direction, position;

    public Directional(int intensity, Color color, Vector3D direction, Vector3D position) {
        super(intensity, color, position);
        this.setDirection(direction.normalize());
    }

    @Override
    public Vector3D getDirection(Vector3D point) {
        this.setAttenuation(1);
        return direction.normalize();
    }

    @Override
    public void setAttenuation(float d){ super.attenuation = super.getIntensity(); }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    @Override
    public String type(){ return "directional"; }
}
