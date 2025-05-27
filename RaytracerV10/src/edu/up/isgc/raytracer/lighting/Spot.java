package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

import static java.lang.Math.acos;
import static java.lang.Math.clamp;

/**
 * Represents a spotlight in a 3D ray tracing environment.
 * A spotlight emits light in a cone shape from a position, with direction and angle limits.
 * The light intensity attenuates based on angle and distance from the spotlight's center direction.
 */
public class Spot extends Light {
    private Vector3D direction;
    private float innerAngle, outerAngle;

    /**
     * Constructs a Spot light with the specified properties.
     *
     * @param intensity   The intensity of the light.
     * @param color       The color of the light.
     * @param direction   The direction the spotlight is pointing.
     * @param position    The position of the spotlight in 3D space.
     * @param innerAngle  The inner angle (in radians) of the spotlight's full-intensity cone.
     * @param outerAngle  The outer angle (in radians) beyond which there is no light.
     */
    public Spot(float intensity, Color color, Vector3D direction, Vector3D position, float innerAngle, float outerAngle) {
        super(intensity, color, position);
        this.setDirection(direction);
        this.setInnerAngle(innerAngle);
        this.setOuterAngle(outerAngle);
    }

    /**
     * Returns the type of light.
     *
     * @return The string "spot" to identify the light as a spotlight.
     */
    @Override
    public String type() {
        return "spot";
    }

    /**
     * Returns the normalized direction of the spotlight.
     *
     * @param point The point in space (not used in this implementation).
     * @return A normalized vector indicating the spotlight's direction.
     */
    @Override
    public Vector3D getDirection(Vector3D point) {
        return this.direction.normalize().scale(1);
    }

    /**
     * Sets the attenuation factor based on the provided distance.
     *
     * @param d The distance between the light and the surface point.
     */
    @Override
    public void setAttenuation(float d) {
        super.attenuation = this.getIntensity() / (1 + d);
    }

    /**
     * Sets the direction of the spotlight.
     *
     * @param direction A vector representing the new direction.
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction.normalize();
    }

    /**
     * Gets the inner angle of the spotlight's cone (full intensity).
     *
     * @return The inner angle in radians.
     */
    public float getInnerAngle() {
        return innerAngle;
    }

    /**
     * Sets the inner angle of the spotlight's cone (full intensity).
     *
     * @param innerAngle The inner angle in radians.
     */
    public void setInnerAngle(float innerAngle) {
        this.innerAngle = innerAngle;
    }

    /**
     * Gets the outer angle of the spotlight's cone (no intensity).
     *
     * @return The outer angle in radians.
     */
    public float getOuterAngle() {
        return outerAngle;
    }

    /**
     * Sets the outer angle of the spotlight's cone (no intensity).
     *
     * @param outerAngle The outer angle in radians.
     */
    public void setOuterAngle(float outerAngle) {
        this.outerAngle = outerAngle;
    }

    /**
     * Calculates the attenuation of the spotlight based on the angle between
     * the light's direction and the vector to the point, as well as the distance.
     *
     * @param point The point in space where the light is being evaluated.
     * @return The attenuation value, accounting for spotlight angle and distance.
     */
    @Override
    public float calculateAttenuation(Vector3D point) {
        Vector3D L = Vector3D.subtract(point, super.getPosition());
        Vector3D Lnormalized = L.normalize();

        // Calculate cosine of the angle between light direction and vector to the point
        float cosAngle = (float) clamp(this.direction.dot(Lnormalized), -1, 1);
        float angle = (float) acos(cosAngle);

        float spotFactor;
        if (angle <= this.getInnerAngle()) {
            spotFactor = 1.0f;
        } else if (angle >= this.getOuterAngle()) {
            spotFactor = 0.0f;
        } else {
            // Linearly interpolate between inner and outer angles
            spotFactor = (this.getOuterAngle() - angle) /
                    (this.getOuterAngle() - this.getInnerAngle());
        }

        return (float) ((spotFactor * this.getInnerAngle()) / L.value);
    }
}
