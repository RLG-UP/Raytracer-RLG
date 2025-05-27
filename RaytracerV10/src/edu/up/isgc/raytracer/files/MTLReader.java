package edu.up.isgc.raytracer.files;

import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.shapes.models.Face;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class for reading and parsing MTL (Material Template Library) files.
 * Handles material properties and texture loading for 3D models.
 */
public class MTLReader {

    /**
     * Reads and parses an MTL file, creating Material objects for each material definition.
     *
     * @param filePath Path to the MTL file to read
     */
    public static void readMTL(String filePath) {
        File file = new File(filePath);

        // Patterns for matching different MTL file properties
        Pattern patternNewMTL = Pattern.compile("^newmtl\\s+");
        Pattern patternNs = Pattern.compile("^Ns\\s+");        // Specular exponent
        Pattern patternKa = Pattern.compile("^Ka\\s+");        // Ambient color
        Pattern patternKd = Pattern.compile("^Kd\\s+");       // Diffuse color
        Pattern patternKs = Pattern.compile("^Ks\\s+");        // Specular color
        Pattern patternKe = Pattern.compile("^Ke\\s+");        // Emissive color
        Pattern patternNi = Pattern.compile("^Ni\\s+");        // Optical density (refraction index)
        Pattern patternD = Pattern.compile("^d\\s+|^Tr\\s+"); // Dissolve (transparency)
        Pattern patternMapKd = Pattern.compile("^map_Kd\\s+"); // Diffuse texture map

        // Dictionary to store material definitions
        Map<String, Material> mtlDictionary = new HashMap<>();

        // Current material properties being processed
        String currentName = null;
        Float ns = null, ka = null, ks = null, ni = null, d = null;
        BufferedImage texture = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Handle new material definition
                if (patternNewMTL.matcher(line).find()) {
                    // If this is not the first material, create and store the previous one
                    if (currentName != null) {
                        Material material = new Material(
                                ks != null ? ks : 0f,
                                ns != null ? ns : 0f,
                                ka != null ? ka : 0f,
                                0f,
                                ni != null ? ni : 0f,
                                d != null ? d : 0f,
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

                }
                // Handle specular exponent (Ns)
                else if (patternNs.matcher(line).find()) {
                    ns = Float.parseFloat(line.split("\\s+")[1]);

                }
                // Handle ambient color (Ka) - using just red channel
                else if (patternKa.matcher(line).find()) {
                    ka = Float.parseFloat(line.split("\\s+")[1]);

                }
                // Handle specular color (Ks) - using just red channel
                else if (patternKs.matcher(line).find()) {
                    ks = Float.parseFloat(line.split("\\s+")[1]);

                }
                // Handle optical density (Ni)
                else if (patternNi.matcher(line).find()) {
                    ni = Float.parseFloat(line.split("\\s+")[1]);

                }
                // Handle dissolve/transparency (d or Tr)
                else if (patternD.matcher(line).find()) {
                    d = Float.parseFloat(line.split("\\s+")[1]);

                }
                // Handle diffuse texture map (map_Kd)
                else if (patternMapKd.matcher(line).find()) {
                    String texturePath = line.substring(line.indexOf(" ") + 1).trim();
                    try {
                        Path path = Paths.get(texturePath).toAbsolutePath();
                        File textureFile = path.toFile();
                        texture = ImageIO.read(textureFile);
                    } catch (IOException e) {
                        System.err.println("Failed to load texture: " + texturePath);
                    }
                }
            }

            // Create and store the last material in the file
            if (currentName != null) {
                Material material = new Material(
                        ks != null ? ks : 0f,
                        ns != null ? ns : 0f,
                        ka != null ? ka : 0f,
                        0f,
                        ni != null ? ni : 0f,
                        d != null ? d : 0f,
                        texture,
                        currentName
                );

                System.out.println("Saving LAST material: " + currentName);
                mtlDictionary.put(currentName, material);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the material dictionary for later use
        Material.setMtlDictionary(mtlDictionary);
    }
}