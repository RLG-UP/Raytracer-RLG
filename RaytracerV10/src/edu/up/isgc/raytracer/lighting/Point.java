package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Vector3D;
import java.awt.*;

/**
 * Represents a point light source in the scene.
 * A point light emits light uniformly in all directions from a specific position in space.
 * It extends the {@link Light} class and overrides its direction and attenuation behavior.
 */
public class Point extends Light {

    /**
     * Constructs a Point light with the given intensity, color, and position.
     *
     * @param intensity The intensity of the light.
     * @param color The color of the light.
     * @param position The position of the light in 3D space.
     */
    public Point(float intensity, Color color, Vector3D position) {
        super(intensity, color, position);
    }

    /**
     * Returns the type of light.
     *
     * @return The string "point" to indicate this is a point light.
     */
    @Override
    public String type() {
        return "point";
    }

    /**
     * Calculates and returns the direction vector from the light source to a given point.
     * Also updates the attenuation based on the distance.
     *
     * @param point The point in space where the light is being evaluated.
     * @return The normalized direction vector from the light source to the point.
     */
    @Override
    public Vector3D getDirection(Vector3D point) {
        Vector3D L = Vector3D.subtract(point, super.getPosition());
        this.setAttenuation((float) (1.0f + 0.1f * L.value + 0.01f * L.value * L.value));
        return L.normalize().scale(1);
    }

    /**
     * Sets the attenuation factor of the light based on the distance.
     *
     * @param d The distance between the light and the surface point.
     */
    @Override
    public void setAttenuation(float d) {
        super.attenuation = this.getIntensity() / (1 + d);
    }

    /**
     * Calculates the attenuation based on the distance value in a vector.
     *
     * @param point A vector representing the direction and magnitude to the point.
     * @return The computed attenuation.
     */
    @Override
    public float calculateAttenuation(Vector3D point) {
        return (float) (this.getIntensity() / (1 + point.value));
    }
}
