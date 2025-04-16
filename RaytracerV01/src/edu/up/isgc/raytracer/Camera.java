package edu.up.isgc.raytracer;

/**
 * Represents a camera in the 3D scene that generates rays for rendering.
 * The camera's position determines the origin point for all rays.
 */
public class Camera {
    public Vector3D position;  // The position of the camera in 3D space

    /**
     * Constructs a camera with the specified position.
     * @param position The 3D coordinates of the camera's location
     */
    public Camera(Vector3D position) {
        this.position = position;
    }

    /**
     * Generates a ray from the camera's position through the specified viewport coordinates.
     * @param u The horizontal viewport coordinate (normalized)
     * @param v The vertical viewport coordinate (normalized)
     * @return A normalized ray originating from the camera's position
     */
    public Ray generateRay(double u, double v) {
        Vector3D rayDir = new Vector3D(u, v, -1).normalize();
        return new Ray(position, rayDir);
    }
}