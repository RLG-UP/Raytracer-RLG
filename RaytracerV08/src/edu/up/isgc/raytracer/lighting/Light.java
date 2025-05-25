package edu.up.isgc.raytracer.lighting;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.shapes.Object3D;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.world.Scene;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.clamp;

public abstract class Light {
    private static float ambientLight = 1.0f;
    private float intensity;
    private Color color;
    private Vector3D position;
    protected float attenuation;
    protected static ArrayList<Light> lights = new ArrayList<Light>();

    public Light(float intensity, Color color, Vector3D position) {
        this.setIntensity(intensity);
        this.setColor(color);
        this.setPosition(position);
        lights.add(this);
    }

    public static float[] normalizeColor(Color color) {
        return new float[] { color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f};
    }

    public static float[] returnColor(float[] normalizedColor) {
        return new float[] {normalizedColor[0] * 255.0f, normalizedColor[1] * 255.0f, normalizedColor[2] * 255.0f};
    }

    public static float getAmbientLight() { return ambientLight; }
    public static void setAmbientLight(float ambientLight) { Light.ambientLight = ambientLight; }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public abstract String type();

    public float getAttenuation(){ return this.attenuation; }
    public abstract void setAttenuation(float d);

    public static float schlick(float cosTheta, float refractiveIndex) {
        float r0 = (1 - refractiveIndex) / (1 + refractiveIndex);
        r0 = r0 * r0;
        return r0 + (1 - r0) * (float)Math.pow(1 - cosTheta, 5);
    }


    public static Color calculateColor(Vector3D N, Vector3D point, Object3D object, Intersection intersection) {
        Color finalColor = new Color(0, 0, 0);
        Color pointColor = intersection.color;
        float ks = 0.0f;
        float p = 1000f;
        float ka = 0.1f;  // Ambient constant
        float reflectivity = 0.1f;
        float refraction = 0.4f;
        float transparency = 0.0f;




        if(object.getHasMaterial()){
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
                lambertian = (float) Math.max(N.dot(l) * light.getAttenuation(), 0.0);
                Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
                blinn = (float) (ks * Math.pow(Math.max(N.dot(h), 0.0), p));
            }

            // Individual components
            Color ambient  = Light.shine(light.getColor(), pointColor, ambientIntensity, true);
            Color diffuse  = Light.shine(light.getColor(), pointColor, lambertian, true);
            Color specular = Light.shine(light.getColor(), pointColor, blinn, false); // specular is independent of object color

            // Combine them
            Color lightContribution = new Color(
                    clamp(ambient.getRed() + diffuse.getRed() + specular.getRed(), 0, 255),
                    clamp(ambient.getGreen() + diffuse.getGreen() + specular.getGreen(), 0, 255),
                    clamp(ambient.getBlue() + diffuse.getBlue() + specular.getBlue(), 0, 255)
            );

            // Accumulate contribution
            finalColor = new Color(
                    clamp(finalColor.getRed() + lightContribution.getRed(), 0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue() + lightContribution.getBlue(), 0, 255)
            );
        }

        //Color reflectContribution = Scene.castReflection(point, N, object, 5);
        //Color reflectContribution = Scene.castRefraction(point, N, object, 5);
        /*
        finalColor = new Color(
                clamp(Math.round( finalColor.getRed() + (reflectContribution.getRed()) ), 0, 255),
                clamp(Math.round( finalColor.getGreen() + (reflectContribution.getGreen()) ), 0, 255),
                clamp(Math.round( finalColor.getBlue() + (reflectContribution.getBlue()) ), 0, 255)
        );

                finalColor = new Color(
                clamp(Math.round( finalColor.getRed() * (1 - reflectivity) + (reflectivity * reflectContribution.getRed()) ), 0, 255),
                clamp(Math.round( finalColor.getGreen() * (1 - reflectivity) + (reflectivity * reflectContribution.getGreen()) ), 0, 255),
                clamp(Math.round( finalColor.getBlue() * (1 - reflectivity) + (reflectivity * reflectContribution.getBlue()) ), 0, 255)
        );
         */

        /*
        Vector3D viewDir = Vector3D.subtract(Camera.getCameraPosition(), point).normalize();
        float cosTheta = Math.max(0f, (float)viewDir.dot(N.normalize()));
        float fresnel = schlick(cosTheta, (float)object.refraction);


        float transparency = (float)object.transparency;

        finalColor = new Color(
                clamp(Math.round(finalColor.getRed() * (1 - transparency) +
                        transparency * (finalColor.getRed() * (1 - fresnel) + reflectContribution.getRed() * fresnel)), 0, 255),
                clamp(Math.round(finalColor.getGreen() * (1 - transparency) +
                        transparency * (finalColor.getGreen() * (1 - fresnel) + reflectContribution.getGreen() * fresnel)), 0, 255),
                clamp(Math.round(finalColor.getBlue() * (1 - transparency) +
                        transparency * (finalColor.getBlue() * (1 - fresnel) + reflectContribution.getBlue() * fresnel)), 0, 255)
        );

         */


        Color reflectionColor = Scene.castReflection(point, N, object, 10);
        Color refractionColor = Scene.castRefraction(point, N, object, 15);

        /*
        Vector3D viewDir = Vector3D.subtract(Camera.getCameraPosition(), point).normalize();
        float cosTheta = Math.max(0f, (float)viewDir.dot(N.normalize()));
        float fresnel = reflectivity * Light.schlick(cosTheta, refraction);

        //float fresnel = reflectivity * Light.schlick(cosTheta, (float)object.refraction);
        //float transparency = (float)object.transparency;

        int rI = clamp(Math.round(
                finalColor.getRed() * (1 - transparency) +
                        transparency * ((1 - fresnel) * refractionColor.getRed() + fresnel * reflectionColor.getRed())
        ), 0, 255);

        int gI = clamp(Math.round(
                finalColor.getGreen() * (1 - transparency) +
                        transparency * ((1 - fresnel) * refractionColor.getGreen() + fresnel * reflectionColor.getGreen())
        ), 0, 255);

        int bI = clamp(Math.round(
                finalColor.getBlue() * (1 - transparency) +
                        transparency * ((1 - fresnel) * refractionColor.getBlue() + fresnel * reflectionColor.getBlue())
        ), 0, 255);

        finalColor = new Color(rI, gI, bI);

         */
        Vector3D viewDir = Vector3D.subtract(Camera.getCameraPosition(), point).normalize();
        float cosTheta = Math.max(0f, (float)viewDir.dot(N.normalize()));
        float fresnel = reflectivity * Light.schlick(cosTheta, refraction);
        float reflectFactor = fresnel * reflectivity;
        float refractFactor = (1 - fresnel) * transparency;
        float baseFactor = 1 - reflectFactor - refractFactor;

// Prevent negative contribution due to floating-point inaccuracies
        baseFactor = Math.max(0, baseFactor);

        int rI = clamp(Math.round(
                baseFactor * finalColor.getRed() +
                        refractFactor * refractionColor.getRed() +
                        reflectFactor * reflectionColor.getRed()
        ), 0, 255);

        int gI = clamp(Math.round(
                baseFactor * finalColor.getGreen() +
                        refractFactor * refractionColor.getGreen() +
                        reflectFactor * reflectionColor.getGreen()
        ), 0, 255);

        int bI = clamp(Math.round(
                baseFactor * finalColor.getBlue() +
                        refractFactor * refractionColor.getBlue() +
                        reflectFactor * reflectionColor.getBlue()
        ), 0, 255);


        finalColor = new Color(rI, gI, bI);
        float gamma = 2.2f;

        float r = finalColor.getRed() / 255.0f;
        float g = finalColor.getGreen() / 255.0f;
        float b = finalColor.getBlue() / 255.0f;

        r = (float) Math.pow(r, 1.0 / gamma);
        g = (float) Math.pow(g, 1.0 / gamma);
        b = (float) Math.pow(b, 1.0 / gamma);

        finalColor = new Color(
                clamp((int)(r * 255), 0, 255),
                clamp((int)(g * 255), 0, 255),
                clamp((int)(b * 255), 0, 255)
        );

        return finalColor;
    }

    public abstract Vector3D getDirection(Vector3D point);
    public Vector3D getDirection() { return this.getDirection(Vector3D.getZero()); }

    /*
    public static Color shine(Color lightColor, Color objectColor, float lambertian){
        float[] nLC = Light.normalizeColor(lightColor);
        float[] nOC = Light.normalizeColor(objectColor);
        float[] nLOC = new float[]{nLC[0] * nOC[0], nLC[1] * nOC[1], nLC[2] * nOC[2]};

        return new Color(clamp(nLOC[0] * lambertian, 0,1.0f), clamp(nLOC[1] * lambertian,0,1.0f), clamp(nLOC[2] * lambertian,0,1.0f));
    }

     */

    /*
    // New version: separate diffuse/specular and object color logic
    public static Color shine(Color lightColor, Color objectColor, float intensity, boolean applyObjectColor) {
        float[] nLC = normalizeColor(lightColor);
        float[] nOC = normalizeColor(objectColor);

        float r = nLC[0] * (applyObjectColor ? nOC[0] : 1.0f) * intensity;
        float g = nLC[1] * (applyObjectColor ? nOC[1] : 1.0f) * intensity;
        float b = nLC[2] * (applyObjectColor ? nOC[2] : 1.0f) * intensity;

        return new Color(
                clamp((int)(r * 255), 0, 255),
                clamp((int)(g * 255), 0, 255),
                clamp((int)(b * 255), 0, 255)
        );
    }

     */

    public static Color shine(Color lightColor, Color objectColor, float intensity, boolean applyObjectColor) {
        float[] nLC = normalizeColor(lightColor);
        float[] nOC = normalizeColor(objectColor);

        // Blend object color into specular a little, even if applyObjectColor is false
        float blendFactor = applyObjectColor ? 1.0f : 0.3f; // 0.3f gives a colored tint to specular

        float r = (nLC[0] * (blendFactor * nOC[0] + (1 - blendFactor))) * intensity;
        float g = (nLC[1] * (blendFactor * nOC[1] + (1 - blendFactor))) * intensity;
        float b = (nLC[2] * (blendFactor * nOC[2] + (1 - blendFactor))) * intensity;

        return new Color(
                clamp((int)(r * 255), 0, 255),
                clamp((int)(g * 255), 0, 255),
                clamp((int)(b * 255), 0, 255)
        );
    }


    public static ArrayList<Light> getLights(){return Light.lights;}
}
