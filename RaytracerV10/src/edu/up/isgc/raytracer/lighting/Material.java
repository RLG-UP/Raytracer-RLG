package edu.up.isgc.raytracer.lighting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a material used in ray tracing with various optical properties
 * such as specular reflection, shininess, transparency, and more.
 * Also supports texture mapping and predefined material factory methods.
 *
 * @author
 */
public class Material {
    private Color color = Color.magenta;
    private float specular = 0f;     // ks - Ns
    private float shininess = 0;     // p
    private float ambient = 0;       // ka - Ka
    private float reflectivity = 0;
    private float refraction = 0;    // Ni
    private float transparency = 0;  // d - Tr
    private BufferedImage textureMap = null;
    private String name = null;
    private static Map<String, Material> mtlDictionary = new HashMap<>();

    /**
     * Constructs a Material with texture and name.
     *
     * @param specular      Specular reflection coefficient.
     * @param shininess     Shininess exponent.
     * @param ambient       Ambient reflection coefficient.
     * @param reflectivity  Reflectivity coefficient.
     * @param refraction    Refraction index.
     * @param transparency  Transparency value.
     * @param textureMap    Texture map image.
     * @param name          Name of the material.
     */
    public Material(float specular, float shininess, float ambient, float reflectivity, float refraction, float transparency, BufferedImage textureMap, String name) {
        this.setSpecular(specular);
        this.setShininess(shininess);
        this.setAmbient(ambient);
        this.setReflectivity(reflectivity);
        this.setRefraction(refraction);
        this.setTransparency(transparency);
        this.setTextureMap(textureMap);
        this.setName(name);
        try {
            System.out.println(this.getTextureMap().getWidth() + " " + this.getTextureMap().getHeight());
        } catch (RuntimeException e) {
            System.err.println("The material " + name + " could not be created.");
        }
    }

    /**
     * Constructs a basic Material without texture or color.
     */
    public Material(float specular, float shininess, float ambient, float reflectivity, float refraction, float transparency) {
        this(specular, shininess, ambient, reflectivity, refraction, transparency, (BufferedImage) null, null);
    }

    /**
     * Constructs a Material with specified color.
     */
    public Material(float specular, float shininess, float ambient, float reflectivity, float refraction, float transparency, Color color) {
        this(specular, shininess, ambient, reflectivity, refraction, transparency);
        this.setColor(color);
    }

    /**
     * Constructs a Material with specified color and name.
     */
    public Material(float specular, float shininess, float ambient, float reflectivity, float refraction, float transparency, Color color, String name) {
        this(specular, shininess, ambient, reflectivity, refraction, transparency, color);
        this.setName(name);
    }

    /** @return Specular reflection coefficient. */
    public float getSpecular() {
        return specular;
    }

    /** @param specular Specular reflection coefficient. */
    public void setSpecular(float specular) {
        this.specular = specular;
    }

    /** @return Shininess exponent. */
    public float getShininess() {
        return shininess;
    }

    /** @param shininess Shininess exponent. */
    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    /** @return Ambient reflection coefficient. */
    public float getAmbient() {
        return ambient;
    }

    /** @param ambient Ambient reflection coefficient. */
    public void setAmbient(float ambient) {
        this.ambient = ambient;
    }

    /** @return Reflectivity coefficient. */
    public float getReflectivity() {
        return reflectivity;
    }

    /** @param reflectivity Reflectivity coefficient. */
    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    /** @return Refraction index. */
    public float getRefraction() {
        return refraction;
    }

    /** @param refraction Refraction index. */
    public void setRefraction(float refraction) {
        this.refraction = refraction;
    }

    /** @return Transparency value. */
    public float getTransparency() {
        return transparency;
    }

    /** @param transparency Transparency value. */
    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    /** @return Texture map image. */
    public BufferedImage getTextureMap() {
        return textureMap;
    }

    /** @param textureMap Texture map image. */
    public void setTextureMap(BufferedImage textureMap) {
        this.textureMap = textureMap;
    }

    /** @return Material name. */
    public String getName() {
        return name;
    }

    /** @param name Material name. */
    public void setName(String name) {
        this.name = name;
    }

    /** @return Dictionary of predefined materials. */
    public static Map<String, Material> getMtlDictionary() {
        return mtlDictionary;
    }

    /** @param mtlDictionary Dictionary of predefined materials. */
    public static void setMtlDictionary(Map<String, Material> mtlDictionary) {
        Material.mtlDictionary = mtlDictionary;
    }

    /**
     * Searches for a material by name.
     *
     * @param name The name of the material.
     * @return The Material object, or null if not found.
     */
    public static Material findByName(String name){
        return Material.getMtlDictionary().get(name);
    }

    /** @return Color of the material. */
    public Color getColor() {
        return color;
    }

    /** @param color Color of the material. */
    public void setColor(Color color) {
        this.color = color;
    }

    // === Predefined Material Factory Methods ===

    /** @param color Base color. @return Predefined glass material. */
    public static Material GLASS(Color color){
        return new Material(1.0f, 8000f, 0.1f, 0.0f, 0.9f, 0.7f, color);
    }

    /** @param color Base color. @return Glass material with high reflectivity. */
    public static Material RAGE_GLASS(Color color){
        return new Material(1.0f, 8000f, 0.1f, 0.8f, 0.37f, 0.5f, color);
    }

    /** @param color Base color. @return Glass material with ninja-like appearance. */
    public static Material NINJA_GLASS(Color color){
        return new Material(1.0f, 5f, 0.1f, 0.4f, 0.4f, 0.7f, color);
    }

    /** @return White glass material. */
    public static Material GLASS(){
        return Material.GLASS(Color.white);
    }

    /** @param color Base color. @return Standard metal material. */
    public static Material METAL(Color color){
        return new Material(1f, 300f, 0.1f, 0.0f, 0.0f, 0.0f, color);
    }

    /** @param color Base color. @return Highly reflective and shiny metal. */
    public static Material PANDEMONIUM_METAL(Color color){
        return new Material(1f, 30000f, 0.1f, 0.7f, 0.0f, 0.0f, color);
    }

    /** @param color Base color. @return Stylized ninja metal. */
    public static Material NINJA_METAL(Color color){
        return new Material(1f, 800f, 0.1f, 0.7f, 0.0f, 0.0f, color);
    }

    /** @param color Base color. @return Mirror-like reflective surface. */
    public static Material MIRROR(Color color){
        return new Material(1f, 80000f, 0.1f, 0.9f, 0.0f, 0.0f, color);
    }

    /** @param color Base color. @return A variant of mirror with some refraction. */
    public static Material PANDEMONIUM_MIRROR(Color color){
        return new Material(1f, 10f, 0.1f, 0.9f, 0.5f, 0.0f, color);
    }

    /** @param color Base color. @return Albedo-style material. */
    public static Material ALBEDO(Color color){
        return new Material(1.0f, 1000f, 0.1f, 0.0f, 0.0f, 0.0f, color);
    }

    /** @param color Base color. @return Plastic-like material. */
    public static Material PLASTIC(Color color){
        return new Material(1f, 100f, 0.1f, 0.0f, 0.0f, 0.0f, color);
    }

    /** @param color Base color. @return Ground-like diffuse material. */
    public static Material GROUND(Color color){
        return new Material(0.0f, 10000f, 0.1f, 0.0f, 0.0f, 0.0f, color);
    }
}
