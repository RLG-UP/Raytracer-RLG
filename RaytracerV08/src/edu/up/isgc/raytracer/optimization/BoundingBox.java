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


    public boolean bbIntersects(Ray ray) {
        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 3; i++) {
            double origin = ray.origin.get(i);
            double direction = ray.direction.get(i);
            double min = this.getMin().get(i);
            double max = this.getMax().get(i);

            if (Math.abs(direction) < 1e-6) {
                // Ray is parallel to this axis
                if (origin < min || origin > max) {
                    return false;
                }
            } else {
                double t1 = (min - origin) / direction;
                double t2 = (max - origin) / direction;

                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tmin = Math.max(tmin, t1);
                tmax = Math.min(tmax, t2);

                if (tmin > tmax) {
                    return false;
                }
            }
        }

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

    public double getSurfaceArea() {
        double dx = max.x - min.x;
        double dy = max.y - min.y;
        double dz = max.z - min.z;
        return 2 * (dx * dy + dx * dz + dy * dz);
    }
}
