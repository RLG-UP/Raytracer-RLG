package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public class Point extends Light{
    private Vector3D direction;
    public Point(int intensity, Color color, Vector3D position) {
        super(intensity, color, position);
    }

    @Override
    public String type(){ return "point"; }

    @Override
    public Vector3D getDirection(Vector3D point){
        return Vector3D.subtract(super.getPosition(), point).normalize();
    }

    public float getAttenuation(float d){
        return super.getIntensity()/(d*d);
    }
}