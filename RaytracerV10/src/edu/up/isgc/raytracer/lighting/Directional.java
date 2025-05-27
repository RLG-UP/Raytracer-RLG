package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

/**
 * Represents a directional light source in the ray tracing scene.
 * Directional lights have a direction but no specific position in space,
 * and they emit parallel light rays similar to sunlight.
 */
public class Directional extends Light {
    private Color color;
    private Vector3D direction, position;

    /**
     * Constructs a Directional light with specified intensity, color, direction, and position.
     *
     * @param intensity the intensity of the light
     * @param color the color of the light
     * @param direction the direction in which the light travels
     * @param position the position of the light (not typically used for directional lights)
     */
    public Directional(int intensity, Color color, Vector3D direction, Vector3D position) {
        super(intensity, color, position);
        this.setDirection(direction.normalize());
    }

    /**
     * Gets the normalized direction of the light from the light source to the given point.
     * For directional lights, the direction is constant and does not depend on the point.
     *
     * @param point the point in the scene (ignored for directional lights)
     * @return the normalized direction vector of the light
     */
    @Override
    public Vector3D getDirection(Vector3D point) {
        this.setAttenuation(1);
        return this.direction.normalize().scale(1);
    }

    /**
     * Calculates the attenuation factor based on the distance to the point.
     * Directional lights have constant attenuation regardless of distance.
     *
     * @param point the point in the scene
     * @return the light intensity (constant)
     */
    @Override
    public float calculateAttenuation(Vector3D point) {
        return super.getIntensity();
    }

    /**
     * Sets the attenuation value for the directional light.
     * For directional lights, attenuation is always equal to intensity.
     *
     * @param d the distance (ignored)
     */
    @Override
    public void setAttenuation(float d) {
        super.attenuation = super.getIntensity();
    }

    /**
     * Sets the direction of the light.
     *
     * @param direction the new direction vector
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    /**
     * Returns the type of the light.
     *
     * @return the string "directional"
     */
    @Override
    public String type() {
        return "directional";
    }
}
