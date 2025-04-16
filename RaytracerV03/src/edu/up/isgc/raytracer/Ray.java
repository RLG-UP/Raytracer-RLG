package edu.up.isgc.raytracer;

/**
 * Represents a ray in 3D space with an origin point and direction vector.
 * The direction vector is always normalized upon creation.
 */
public class Ray {
    private Vector3D origin;    // Starting point of the ray
    private Vector3D direction; // Normalized direction vector of the ray

    /**
     * Constructs a ray with the specified origin and direction.
     * @param origin The starting point of the ray
     * @param direction The direction vector (will be normalized)
     */
    public Ray(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Vector3D getOrigin() { return origin; }
    public void setOrigin(Vector3D origin) { this.origin = origin; }

    public Vector3D getDirection() { return direction; }
    public void setDirection(Vector3D direction) { this.direction = direction.normalize(); }
}