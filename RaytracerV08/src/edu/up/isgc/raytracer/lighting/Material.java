package edu.up.isgc.raytracer.lighting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Material {
    private Color color = Color.magenta;
    private float specular = 0;     //ks - Ns
    private float shininess = 0;    //p
    private float ambient = 0;      // ka - Ka
    private float reflectivity = 0;
    private float refraction = 0;   // Ni
    private float transparency = 0; // d  - Tr
    private BufferedImage textureMap = null;
    private String name = null;
    private static Map<String, Material> mtlDictionary = new HashMap<>();

    public Material(float specular, float shininess, float ambient, float reflectivity, float refraction, float transparency, BufferedImage textureMap, String name) {
        this.setSpecular(specular);
        this.setShininess(shininess);
        this.setAmbient(ambient);
        this.setReflectivity(reflectivity);
        this.setRefraction(refraction);
        this.setTransparency(transparency);
        this.setTextureMap(textureMap);
        this.setName(name);
        try{
            System.out.println(this.getTextureMap().getWidth() + " " + this.getTextureMap().getHeight());
        } catch (RuntimeException e) {
            System.err.println("The material " + name + " could not be created.");
        }
    }

    public Material(float specular, float shininess, float ambient, float reflectivity, float refraction, float transparency) {
        this.setSpecular(specular);
        this.setShininess(shininess);
        this.setAmbient(ambient);
        this.setReflectivity(reflectivity);
        this.setRefraction(refraction);
        this.setTransparency(transparency);
    }

    public Material(float specular, float shininess, float ambient, float reflectivity, float refraction, float transparency, Color color) {
        this.setSpecular(specular);
        this.setShininess(shininess);
        this.setAmbient(ambient);
        this.setReflectivity(reflectivity);
        this.setRefraction(refraction);
        this.setTransparency(transparency);
        this.setColor(color);
    }


    public float getSpecular() {
        return specular;
    }

    public void setSpecular(float specular) {
        this.specular = specular;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public float getAmbient() {
        return ambient;
    }

    public void setAmbient(float ambient) {
        this.ambient = ambient;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public float getRefraction() {
        return refraction;
    }

    public void setRefraction(float refraction) {
        this.refraction = refraction;
    }

    public float getTransparency() {
        return transparency;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    public BufferedImage getTextureMap() {
        return textureMap;
    }

    public void setTextureMap(BufferedImage textureMap) {
        this.textureMap = textureMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Map<String, Material> getMtlDictionary() {
        return mtlDictionary;
    }

    public static void setMtlDictionary(Map<String, Material> mtlDictionary) {
        Material.mtlDictionary = mtlDictionary;
    }

    public static Material findByName(String name){
        //System.out.println("Searching for material with name " + name);
        Material material = Material.getMtlDictionary().get(name);
        //if (material == null) System.out.println("MATERIAL NOT FOUND");
        //if (material != null) System.out.println("MATERIAL FOUND: " + material);
        return material;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static Material GLASS(Color color){
        return new Material(0.8f, 1000f, 0.1f,0.8f,0.4f,0.96f, color);
    }

    public static Material METAL(Color color){
        return new Material(1f, 1000f, 0.1f,0.4f,0.7f,0.1f, color);
    }
    public static Material GLASS(){
        return Material.GLASS(Color.white);
    }
}
