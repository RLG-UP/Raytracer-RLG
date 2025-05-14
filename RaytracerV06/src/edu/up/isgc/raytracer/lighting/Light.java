package edu.up.isgc.raytracer.lighting;

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

    public static Color calculateColor(Vector3D N, Vector3D point, Object3D object) {
        Color finalColor = new Color(0, 0, 0);
        float ks = 1f;
        float p = 10f;
        float ka = 0.1f;  // Ambient constant
        float reflectivity = 0.5f;
        float ambientIntensity = ka * Light.getAmbientLight();

        for (Light light : Light.getLights()) {
            Vector3D l = Vector3D.getZero();
            double lightDistance = Double.MAX_VALUE;

            if (light.type().equals("directional")) {
                l = light.getDirection().normalize().scale(-1);
            } else if (light.type().equals("point") || light.type().equals("spot")) {
                l = light.getDirection(point).normalize().scale(-1);
                lightDistance = Vector3D.subtract(light.getPosition(), point).value;
            }

            boolean inShadow = Scene.isInShadow(point, N, light, object);

            float lambertian = 0;
            float blinn = 0;

            if (!inShadow) {
            }
            lambertian = (float) Math.max(N.dot(l) * light.getAttenuation(), 0.0);
            Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
            blinn = (float) (ks * Math.pow(Math.max(N.dot(h), 0.0), p));

            // Individual components
            Color ambient  = Light.shine(light.getColor(), object.getColor(), ambientIntensity, true);
            Color diffuse  = Light.shine(light.getColor(), object.getColor(), lambertian, true);
            Color specular = Light.shine(light.getColor(), object.getColor(), blinn, false); // specular is independent of object color

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

        Color reflectContribution = Scene.castReflection(point, N, object, 5);
        finalColor = new Color(
                clamp(Math.round( finalColor.getRed() * (1 - reflectivity) + (reflectivity * reflectContribution.getRed()) ), 0, 255),
                clamp(Math.round( finalColor.getGreen() * (1 - reflectivity) + (reflectivity * reflectContribution.getGreen()) ), 0, 255),
                clamp(Math.round( finalColor.getBlue() * (1 - reflectivity) + (reflectivity * reflectContribution.getBlue()) ), 0, 255)
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





    public static Vector3D ericson(Vector3D point, Vector3D v1, Vector3D v2, Vector3D v3, Vector3D n1, Vector3D n2, Vector3D n3) {

        Vector3D e1 = Vector3D.subtract(v1,v2);
        Vector3D e2 = Vector3D.subtract(v1,v3);
        Vector3D eP = Vector3D.subtract(v1,point);

        /*
        Vector3D triNormal = Vector3D.crossProduct(e1,e2).normalize();
        Vector3D viewDir = Vector3D.subtract(point, Camera.getCameraPosition()).normalize();

         */

        double d11 = e1.dot(e1);
        double d12 = e1.dot(e2);
        double d22 = e2.dot(e2);
        double d1P = e1.dot(eP);
        double d2P = e2.dot(eP);

        double denominator = (d11*d22)-(d12*d12);
        if (Math.abs(denominator) < 1e-10) return Vector3D.getZero();

        double v = ( (d22*d1P)-(d12*d2P) )/denominator;
        double w = ( (d11*d2P)-(d12*d1P) )/denominator;
        double u = 1.0-v-w;


        /*

        v = clamp(v, 0, 1);
        w = clamp(w, 0, 1);
        u = clamp(u, 0, 1);
         */
        //System.out.println("u: " + u + " v: " + v + " w: " + w);

        /*

        double den = ( (v2.y - v3.y) * (v1.x - v3.x) ) + ( (v3.x - v2.x) * (v1.y - v3.y) );
        double v = (( (v2.y - v3.y) * (point.x - v3.x) ) + ( (v3.x - v2.x) * (point.y - v3.y) )) / den;
        double w = (( (v3.y - v1.y) * (point.x - v3.x) ) + ( (v1.x - v3.x) * (point.y - v3.y) )) / den;
        double u = 1 - v - w;

         */


        System.out.println("u:" + u + " v:" + v + " w:" + w);
        //if(u < 0 || u > 1 || v < 0 || v > 1 || w < 0 || w > 1) return Vector3D.getZero();

        Vector3D uN = n1.scale(u);
        Vector3D vN = n2.scale(v);
        Vector3D wN = n3.scale(w);

        return uN.add(vN).add(wN).normalize();
        //return n1.scale(u).add(n2.scale(v)).add(n3.scale(w)).normalize();
        //return n1.add(n2).add(n3).scale(1.0/3.0).normalize();
    }

    public static ArrayList<Light> getLights(){return Light.lights;}
}
