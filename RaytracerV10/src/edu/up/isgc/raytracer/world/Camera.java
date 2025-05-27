package edu.up.isgc.raytracer.world;

import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

/**
 * Represents a camera in the 3D scene that generates rays for rendering.
 * The camera's position determines the origin point for all rays.
 */
public class Camera {
    public static Vector3D position;  // The position of the camera in 3D space
    private static double epsilon = 0.00001;
    private static double shadowEpsilon = 0.001;
    public double[] clipPlanes = new double[2];
    private double fov;
    private int width, height;

    /**
     * Constructs a camera with the specified position.
     * @param position The 3D coordinates of the camera's location
     */
    public Camera(Vector3D position,  double nearPlane, double farPlane, double fov, int width, int height) {
        this.position = position;
        this.clipPlanes[0] = nearPlane;
        this.clipPlanes[1] = farPlane;
        this.setFov(fov);
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
    // Standard getters and setters with basic documentation
    public static double getEpsilon(){ return Camera.epsilon; }
    public static void setEpsilon(double epsilon){ Camera.epsilon = epsilon; }
    public static Vector3D getCameraPosition() { return Camera.position; }

    public Vector3D getPosition() { return this.position; }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public static double getShadowEpsilon() {
        return shadowEpsilon;
    }

    public static void setShadowEpsilon(double shadowEpsilon) {
        Camera.shadowEpsilon = shadowEpsilon;
    }
}