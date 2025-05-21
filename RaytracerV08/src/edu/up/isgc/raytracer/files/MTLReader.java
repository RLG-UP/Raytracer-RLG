package edu.up.isgc.raytracer.files;

import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.shapes.models.Face;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MTLReader {
    public static void readMTL(String filePath) {
        File file = new File(filePath);

        Pattern patternNewMTL = Pattern.compile("^newmtl\\s+");
        Pattern patternNs = Pattern.compile("^Ns\\s+");
        Pattern patternKa = Pattern.compile("^Ka\\s+");
        Pattern patternKd = Pattern.compile("^Kd\\s+");
        Pattern patternKs = Pattern.compile("^Ks\\s+");
        Pattern patternKe = Pattern.compile("^Ke\\s+");
        Pattern patternNi = Pattern.compile("^Ni\\s+");
        Pattern patternD = Pattern.compile("^d\\s+|^Tr\\s+");
        Pattern patternMapKd = Pattern.compile("^map_Kd\\s+");

        Map<String, Material> mtlDictionary = new HashMap<>();

        String currentName = null;
        Float ns = null, ka = null, ks = null, ni = null, d = null;
        BufferedImage texture = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) continue;

                if (patternNewMTL.matcher(line).find()) {
                    // If this is not the first material, create and store the previous one
                    if (currentName != null) {
                        Material material = new Material(
                                ks != null ? ks : 0f,
                                ns != null ? ns : 0f,
                                ka != null ? ka : 0f,
                                0f,
                                ni != null ? 1f - ni : 0f,
                                d != null ? 1f - d : 0f,
                                texture,
                                currentName
                        );
                        System.out.println("Saving material: " + currentName);
                        mtlDictionary.put(currentName, material);
                    }

                    // Reset values for the new material
                    currentName = line.split("\\s+")[1];
                    ns = ka = ks = ni = d = null;
                    texture = null;

                } else if (patternNs.matcher(line).find()) {
                    ns = Float.parseFloat(line.split("\\s+")[1]);

                } else if (patternKa.matcher(line).find()) {
                    ka = Float.parseFloat(line.split("\\s+")[1]); // taking just red channel

                } else if (patternKs.matcher(line).find()) {
                    ks = Float.parseFloat(line.split("\\s+")[1]); // taking just red channel

                } else if (patternNi.matcher(line).find()) {
                    ni = Float.parseFloat(line.split("\\s+")[1]);

                } else if (patternD.matcher(line).find()) {
                    d = Float.parseFloat(line.split("\\s+")[1]);

                } else if (patternMapKd.matcher(line).find()) {
                    String texturePath = line.substring(line.indexOf(" ") + 1).trim();
                    try {
                        texture = ImageIO.read(new File(texturePath));
                        //System.out.println("Loaded texture: \"" + texturePath + "\"");
                    } catch (IOException e) {
                        System.err.println("Failed to load texture: " + texturePath);
                    }
                }
            }

            // Create the last material
            if (currentName != null) {
                Material material = new Material(
                        ks != null ? ks : 0f,
                        ns != null ? ns : 0f,
                        ka != null ? ka : 0f,
                        0f,
                        ni != null ? 1f - ni : 0f,
                        d != null ? 1f - d : 0f,
                        texture,
                        currentName
                );
                System.out.println("Saving LAST material: " + currentName);
                mtlDictionary.put(currentName, material);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set your dictionary somewhere global for lookups later
        Material.setMtlDictionary(mtlDictionary);
    }

}
