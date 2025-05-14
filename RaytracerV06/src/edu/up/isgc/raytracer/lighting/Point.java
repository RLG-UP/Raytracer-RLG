package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public class Point extends Light{
    private Vector3D direction;
    public Point(float intensity, Color color, Vector3D position) {
        super(intensity, color, position);
    }

    @Override
    public String type(){ return "point"; }

    @Override
    public Vector3D getDirection(Vector3D point){
        Vector3D L = Vector3D.subtract(super.getPosition(), point);
        this.setAttenuation((float) L.value);

        return L.normalize().scale(1);
    }

    @Override
    public void setAttenuation(float d){
       super.attenuation =  this.getIntensity()/(1+d);
    }
}