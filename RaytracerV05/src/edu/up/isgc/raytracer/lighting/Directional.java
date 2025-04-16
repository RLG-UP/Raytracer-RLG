package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public class Directional extends Light{
    private int intensity;
    private Color color;
    private Vector3D direction, position;

    public Directional(int intensity, Color color, Vector3D direction, Vector3D position) {
        super(intensity, color, position);
        this.setDirection(direction.normalize());
    }

    @Override
    public Vector3D getDirection(Vector3D point) {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    @Override
    public String type(){ return "directional"; }
}
