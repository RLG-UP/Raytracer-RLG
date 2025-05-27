package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.shapes.Object3D;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.world.Scene;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.clamp;

/**
 * Abstract base class representing a light source in the ray tracer.
 * It contains common attributes and methods shared across different types of lights.
 */
public abstract class Light {
    private static float ambientLight = 1.0f;
    private float intensity;
    private Color color;
    private Vector3D position;
    protected float attenuation;
    protected static ArrayList<Light> lights = new ArrayList<>();

    /**
     * Constructs a new Light instance with the given intensity, color, and position.
     * @param intensity The light intensity.
     * @param color The color of the light.
     * @param position The position of the light in 3D space.
     */
    public Light(float intensity, Color color, Vector3D position) {
        this.setIntensity(intensity);
        this.setColor(color);
        this.setPosition(position);
        lights.add(this);
    }

    /**
     * Normalizes a color to the range [0,1].
     * @param color The original color.
     * @return An array of floats representing the normalized RGB values.
     */
    public static float[] normalizeColor(Color color) {
        return new float[] {
                color.getRed() / 255.0f,
                color.getGreen() / 255.0f,
                color.getBlue() / 255.0f
        };
    }

    /**
     * Converts a normalized color back to the [0,255] range.
     * @param normalizedColor The normalized RGB array.
     * @return An array of floats representing RGB values in [0,255].
     */
    public static float[] returnColor(float[] normalizedColor) {
        return new float[] {
                normalizedColor[0] * 255.0f,
                normalizedColor[1] * 255.0f,
                normalizedColor[2] * 255.0f
        };
    }

    /** @return The global ambient light intensity. */
    public static float getAmbientLight() {
        return ambientLight;
    }

    /**
     * Sets the global ambient light intensity.
     * @param ambientLight New ambient light intensity.
     */
    public static void setAmbientLight(float ambientLight) {
        Light.ambientLight = ambientLight;
    }

    /** @return The light intensity. */
    public float getIntensity() {
        return intensity;
    }

    /**
     * Sets the light intensity.
     * @param intensity New intensity value.
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    /** @return The light color. */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the light color.
     * @param color New color value.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /** @return The light's position. */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets the light's position.
     * @param position New position vector.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * @return The type of light (e.g., "point", "directional", etc.).
     */
    public abstract String type();

    /** @return The attenuation value for the light. */
    public float getAttenuation() {
        return this.attenuation;
    }

    /**
     * Sets the attenuation based on distance or other factors.
     * @param d A parameter used for attenuation calculation.
     */
    public abstract void setAttenuation(float d);

    /**
     * Calculates the Schlick approximation for Fresnel reflection.
     * @param cosTheta Cosine of the angle between the normal and view direction.
     * @param refractiveIndex The index of refraction of the material.
     * @return The reflection coefficient.
     */
    public static float schlick(float cosTheta, float refractiveIndex) {
        float r0 = (1 - refractiveIndex) / (1 + refractiveIndex);
        r0 *= r0;
        return r0 + (1 - r0) * (float) Math.pow(1 - cosTheta, 5);
    }

    /**
     * Computes the resulting color at a point considering light interaction, including
     * ambient, diffuse, specular, reflection, and refraction contributions.
     * @param N The surface normal at the point.
     * @param point The intersection point.
     * @param object The object being shaded.
     * @param intersection The intersection data.
     * @return The final computed color.
     */
    public static Color calculateColor(Vector3D N, Vector3D point, Object3D object, Intersection intersection) {
        Color finalColor = new Color(0, 0, 0);
        Color pointColor = intersection.color;
        float ks = 0.0f;
        float p = 1000f;
        float ka = 0.1f;
        float reflectivity = 0.1f;
        float refraction = 0.4f;
        float transparency = 0.0f;

        if (object.getHasMaterial()) {
            Material material = object.getMaterial();
            ks = material.getSpecular();
            p = material.getShininess();
            ka = material.getAmbient();
            reflectivity = material.getReflectivity();
            refraction = material.getRefraction();
            transparency = material.getTransparency();
        }

        float ambientIntensity = ka * Light.getAmbientLight();

        for (Light light : Light.getLights()) {
            Vector3D l = Vector3D.getZero();
            double lightDistance = Double.MAX_VALUE;
            boolean inShadow = false;

            if (light.type().equals("directional")) {
                l = light.getDirection().normalize().scale(1);
            } else if (light.type().equals("point") || light.type().equals("spot")) {
                l = light.getDirection(point).normalize().scale(-1);
                lightDistance = Vector3D.subtract(light.getPosition(), point).value;
                inShadow = Scene.isInShadow(point, N, light, object);
            }

            float lambertian = 0;
            float blinn = 0;

            if (!inShadow) {
                lambertian = (float) Math.max(N.dot(l) * light.calculateAttenuation(point), 0.0);
                Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
                blinn = (float) (ks * Math.pow(Math.max(N.dot(h), 0.0), p));
            }

            Color ambient = Light.shine(light.getColor(), pointColor, ambientIntensity, true);
            Color diffuse = Light.shine(light.getColor(), pointColor, lambertian, true);
            Color specular = Light.shine(light.getColor(), pointColor, blinn, false);

            Color lightContribution = new Color(
                    clamp(ambient.getRed() + diffuse.getRed() + specular.getRed(), 0, 255),
                    clamp(ambient.getGreen() + diffuse.getGreen() + specular.getGreen(), 0, 255),
                    clamp(ambient.getBlue() + diffuse.getBlue() + specular.getBlue(), 0, 255)
            );

            finalColor = new Color(
                    clamp(finalColor.getRed() + lightContribution.getRed(), 0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue() + lightContribution.getBlue(), 0, 255)
            );
        }

        Color reflectionColor = Scene.castReflection(point, N, object, 5);
        Color refractionColor = Scene.castRefraction(point, N, object, 10);

        Vector3D viewDir = Vector3D.subtract(Camera.getCameraPosition(), point).normalize();
        float cosTheta = Math.max(0f, (float) viewDir.dot(N.normalize()));
        float fresnel = reflectivity * Light.schlick(cosTheta, refraction);
        float reflectFactor = fresnel * reflectivity;
        float refractFactor = (1 - fresnel) * transparency;
        float baseFactor = 1 - reflectFactor - refractFactor;

        baseFactor = Math.max(0, baseFactor);

        int rI = clamp(Math.round(
                baseFactor * finalColor.getRed() +
                        refractFactor * refractionColor.getRed() +
                        reflectFactor * reflectionColor.getRed()), 0, 255);

        int gI = clamp(Math.round(
                baseFactor * finalColor.getGreen() +
                        refractFactor * refractionColor.getGreen() +
                        reflectFactor * reflectionColor.getGreen()), 0, 255);

        int bI = clamp(Math.round(
                baseFactor * finalColor.getBlue() +
                        refractFactor * refractionColor.getBlue() +
                        reflectFactor * reflectionColor.getBlue()), 0, 255);

        finalColor = new Color(rI, gI, bI);
        float gamma = 2.2f;

        float r = finalColor.getRed() / 255.0f;
        float g = finalColor.getGreen() / 255.0f;
        float b = finalColor.getBlue() / 255.0f;

        r = (float) Math.pow(r, 1.0 / gamma);
        g = (float) Math.pow(g, 1.0 / gamma);
        b = (float) Math.pow(b, 1.0 / gamma);

        return new Color(
                clamp((int)(r * 255), 0, 255),
                clamp((int)(g * 255), 0, 255),
                clamp((int)(b * 255), 0, 255)
        );
    }

    /**
     * Abstract method to get the light's direction relative to a given point.
     * @param point The point for direction calculation.
     * @return The direction vector.
     */
    public abstract Vector3D getDirection(Vector3D point);

    /**
     * Gets the light's direction assuming origin as the reference point.
     * @return The direction vector.
     */
    public Vector3D getDirection() {
        return this.getDirection(Vector3D.getZero());
    }

    /**
     * Calculates the final color contribution of a light to an object surface.
     * @param lightColor The light color.
     * @param objectColor The object color.
     * @param intensity The intensity factor.
     * @param applyObjectColor Whether to apply the object's color in the result.
     * @return The resulting color.
     */
    public static Color shine(Color lightColor, Color objectColor, float intensity, boolean applyObjectColor) {
        float[] nLC = normalizeColor(lightColor);
        float[] nOC = normalizeColor(objectColor);

        float blendFactor = applyObjectColor ? 1.0f : 0.3f;

        float r = (nLC[0] * (blendFactor * nOC[0] + (1 - blendFactor))) * intensity;
        float g = (nLC[1] * (blendFactor * nOC[1] + (1 - blendFactor))) * intensity;
        float b = (nLC[2] * (blendFactor * nOC[2] + (1 - blendFactor))) * intensity;

        return new Color(
                clamp((int)(r * 255), 0, 255),
                clamp((int)(g * 255), 0, 255),
                clamp((int)(b * 255), 0, 255)
        );
    }

    /**
     * Returns the list of all light instances in the scene.
     * @return The list of lights.
     */
    public static ArrayList<Light> getLights() {
        return Light.lights;
    }

    /**
     * Abstract method for calculating the attenuation of light at a given point.
     * @param point The point where attenuation is calculated.
     * @return The attenuation factor.
     */
    public abstract float calculateAttenuation(Vector3D point);
}
