package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

import static java.lang.Math.acos;
import static java.lang.Math.clamp;

public class Spot extends Light{
    private Vector3D direction;
    private float innerAngle, outerAngle;
    public Spot(float intensity, Color color, Vector3D direction , Vector3D position, float innerAngle, float outerAngle) {
        super(intensity, color, position);
        this.setDirection(direction);
        this.setInnerAngle(innerAngle);
        this.setOuterAngle(outerAngle);
    }

    @Override
    public String type(){ return "spot"; }

    @Override
    public Vector3D getDirection(Vector3D point){
        Vector3D L = Vector3D.subtract(this.getPosition(), point);

        float angle = (float) acos(this.direction.dot(L.normalize()));
        float spotFactor = clamp((this.getOuterAngle() - angle)/(this.getOuterAngle()-this.getInnerAngle()), 0, 1);
        this.setAttenuation((float) L.value * spotFactor);

        return L.normalize();
    }

    @Override
    public void setAttenuation(float d){
        super.attenuation = this.getIntensity()/(1+d);
    }

    public void setDirection(Vector3D direction){
        this.direction = direction.normalize();
    }

    public float getInnerAngle() {
        return innerAngle;
    }

    public void setInnerAngle(float innerAngle) {
        this.innerAngle = innerAngle;
    }

    public float getOuterAngle() {
        return outerAngle;
    }

    public void setOuterAngle(float outerAngle) {
        this.outerAngle = outerAngle;
    }
}