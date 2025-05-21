package edu.up.isgc.raytracer.files;

import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.shapes.models.Face;
import edu.up.isgc.raytracer.shapes.models.Polygon;
import edu.up.isgc.raytracer.world.Scene;

public class Obj {
    public static void RenderObj(Scene scene, String objPath, String mtlPath){
        MTLReader.readMTL(mtlPath);
        scene.addPolygon(new Polygon(objPath));
        Face.clearMaterialMap();
    }

    public static void RenderObj(Scene scene, String objPath, String mtlPath, Vector3D rotate, Vector3D scale, Vector3D translation){
        MTLReader.readMTL(mtlPath);
        Polygon polygon = new Polygon(objPath);

        if(rotate != null){ polygon.rotate((float)rotate.x, (float)rotate.y, (float)rotate.z); }
        if(scale != null){ polygon.scale((float)scale.x, (float)scale.y, (float)scale.z); }
        if(translation != null){ polygon.translate((float)translation.x, (float)translation.y, (float)translation.z); }

        scene.addPolygon(polygon);
        Face.clearMaterialMap();
    }
}
