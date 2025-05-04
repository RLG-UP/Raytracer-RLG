package edu.up.isgc.raytracer.shapes.models;

import java.util.ArrayList;

public class Texture {
    private static ArrayList<Double[]> textures = new ArrayList<>();
    public Texture (Double a, Double b, Double c){
        Texture.addTexture(new Double[]{a,b,c});
    }

    public static void addTexture(Double[] vertex){ textures.add(vertex); }
    public static ArrayList<Double[]> getTexture(){ return Texture.textures; }
}
