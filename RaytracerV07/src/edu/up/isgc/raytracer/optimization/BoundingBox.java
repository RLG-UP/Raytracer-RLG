package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

public class BoundingBox {
    private Vector3D min;
    private Vector3D max;

    public BoundingBox(Vector3D min, Vector3D max) {
        this.setMin(min);
        this.setMax(max);
    }

    public boolean bbIntersects(Ray ray){
        float tmin = (float)((this.getMin().x - ray.origin.x) / (ray.direction.x));
        float tmax = (float)((this.getMax().x - ray.origin.x) / (ray.direction.x));
        if(tmin > tmax){ float temp = tmin; tmin = tmax; tmax = temp; }

        float tymin = (float)((this.getMin().y - ray.origin.y) / (ray.direction.y));
        float tymax = (float)((this.getMax().y - ray.origin.y) / (ray.direction.y));
        if(tymin > tymax){ float temp = tymin; tymin = tymax; tymax = temp; }

        if((tmin > tymax) || (tymin > tmax)) return false;
        tmin = Math.min(tmin, tymin);
        tmax = Math.max(tmax, tymax);

        float tzmin = (float)((this.getMin().z - ray.origin.z) / (ray.direction.z));
        float tzmax = (float)((this.getMax().z - ray.origin.z) / (ray.direction.z));
        if(tzmin > tzmax){float temp = tzmin; tzmin = tzmax; tzmax = temp; }

        if((tmin > tzmax) || (tzmin > tmax)) return false;

        return true;
    }

    public static BoundingBox surroundingBox(BoundingBox box1, BoundingBox box2) {
        Vector3D small = new Vector3D(
                Math.min(box1.min.x, box2.min.x),
                Math.min(box1.min.y, box2.min.y),
                Math.min(box1.min.z, box2.min.z)
        );
        Vector3D big = new Vector3D(
                Math.max(box1.max.x, box2.max.x),
                Math.max(box1.max.y, box2.max.y),
                Math.max(box1.max.z, box2.max.z)
        );
        return new BoundingBox(small, big);
    }

    public Vector3D getMin() {
        return min;
    }

    public void setMin(Vector3D min) {
        this.min = min;
    }

    public Vector3D getMax() {
        return max;
    }

    public void setMax(Vector3D max) {
        this.max = max;
    }
}
