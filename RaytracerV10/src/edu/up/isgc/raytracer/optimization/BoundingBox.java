package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

/**
 * Represents an axis-aligned bounding box used for spatial optimization in ray tracing.
 * Encapsulates a minimum and maximum corner in 3D space.
 */
public class BoundingBox {
    private Vector3D min;
    private Vector3D max;

    /**
     * Constructs a BoundingBox with the specified minimum and maximum corners.
     *
     * @param min The minimum corner (lower bound) of the bounding box.
     * @param max The maximum corner (upper bound) of the bounding box.
     */
    public BoundingBox(Vector3D min, Vector3D max) {
        this.setMin(min);
        this.setMax(max);
    }

    /**
     * Checks whether the given ray intersects with this bounding box using the slab method.
     *
     * @param ray The ray to test for intersection.
     * @return {@code true} if the ray intersects the bounding box; {@code false} otherwise.
     */
    public boolean bbIntersects(Ray ray) {
        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 3; i++) {
            double origin = ray.origin.get(i);
            double direction = ray.direction.get(i);
            double min = this.getMin().get(i);
            double max = this.getMax().get(i);

            if (Math.abs(direction) < 1e-6) {
                // Ray is parallel to the slab
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

    /**
     * Computes the smallest bounding box that surrounds both input bounding boxes.
     *
     * @param box1 The first bounding box.
     * @param box2 The second bounding box.
     * @return A new bounding box that encompasses both {@code box1} and {@code box2}.
     */
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

    /**
     * Gets the minimum corner of this bounding box.
     *
     * @return The minimum {@link Vector3D} of the box.
     */
    public Vector3D getMin() {
        return min;
    }

    /**
     * Sets the minimum corner of this bounding box.
     *
     * @param min The {@link Vector3D} to set as the minimum corner.
     */
    public void setMin(Vector3D min) {
        this.min = min;
    }

    /**
     * Gets the maximum corner of this bounding box.
     *
     * @return The maximum {@link Vector3D} of the box.
     */
    public Vector3D getMax() {
        return max;
    }

    /**
     * Sets the maximum corner of this bounding box.
     *
     * @param max The {@link Vector3D} to set as the maximum corner.
     */
    public void setMax(Vector3D max) {
        this.max = max;
    }

    /**
     * Calculates and returns the surface area of the bounding box.
     *
     * @return The surface area of the box.
     */
    public double getSurfaceArea() {
        double dx = max.x - min.x;
        double dy = max.y - min.y;
        double dz = max.z - min.z;
        return 2 * (dx * dy + dx * dz + dy * dz);
    }
}
