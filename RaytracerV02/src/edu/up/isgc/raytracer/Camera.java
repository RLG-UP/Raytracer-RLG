package edu.up.isgc.raytracer;

/**
 * Represents a camera in the 3D scene that generates rays for rendering.
 * The camera's position determines the origin point for all rays.
 */
public class Camera {
    private Vector3D position;  // The position of the camera in 3D space
    private Double[] clipPlanes = new Double[2];

    /**
     * Constructs a camera with the specified position.
     * @param position The 3D coordinates of the camera's location
     */
    public Camera(Vector3D position, Double nearPlane, Double farPlane) {
        this.setPosition(position);
        this.setNearPlane(nearPlane);
        this.setFarPlane(farPlane);
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

    public Vector3D getPosition() { return position; }

    public void setPosition(Vector3D position) { this.position = position; }

    public Double getNearPlane() { return this.getClipPlanes()[0]; }

    public void setNearPlane(Double nearPlane) { this.setClipPlanes(new Double[]{nearPlane, this.getClipPlanes()[1]}); }

    public Double getFarPlane() { return this.getClipPlanes()[1]; }

    public void setFarPlane(Double farPlane) { this.setClipPlanes(new Double[]{this.getClipPlanes()[0], farPlane}); }


    public Double[] getClipPlanes() { return clipPlanes; }

    public void setClipPlanes(Double[] clipPlanes) {
        if(clipPlanes[0] <= clipPlanes[1]) this.clipPlanes = clipPlanes;
        else{
            clipPlanes[0] = clipPlanes[1];
            clipPlanes[1] = clipPlanes[0];
        }
    }
}