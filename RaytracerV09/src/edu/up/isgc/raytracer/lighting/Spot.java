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
    public Vector3D getDirection(Vector3D point) {
        return this.direction.normalize().scale(1);
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

    @Override
    public float calculateAttenuation(Vector3D point) {
        Vector3D L = Vector3D.subtract(point, super.getPosition());
        Vector3D Lnormalized = L.normalize();

        // Calculate spot factor
        float cosAngle = (float) clamp(this.direction.dot(Lnormalized), -1, 1);
        float angle = (float) Math.acos(cosAngle);
        float spotFactor;
        if (angle <= this.getInnerAngle()) {
            spotFactor = 1.0f;
        } else if (angle >= this.getOuterAngle()) {
            spotFactor = 0.0f;
        } else {
            spotFactor = (this.getOuterAngle() - angle) /
                    (this.getOuterAngle() - this.getInnerAngle());
        }
        return (float)((spotFactor * this.getInnerAngle())/(L.value));
    }
}