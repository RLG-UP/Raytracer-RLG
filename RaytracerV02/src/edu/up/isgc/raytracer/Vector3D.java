package edu.up.isgc.raytracer;

/**
 * Represents a 3D vector with x, y, z components and common vector operations.
 * Includes methods for vector math and comparison.
 */
public class Vector3D {
    public double x, y, z;  // Vector components
    public double value;    // Precomputed magnitude of the vector

    /**
     * Constructs a 3D vector with specified components.
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.value = Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Adds another vector to this vector.
     * @param v Vector to add
     * @return New vector result of addition
     */
    public Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Subtracts another vector from this vector.
     * @param v Vector to subtract
     * @return New vector result of subtraction
     */
    public Vector3D subtract(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    /**
     * Multiplies this vector by a scalar value.
     * @param scalar The scalar multiplier
     * @return New scaled vector
     */
    public Vector3D scale(double scalar) {
        return new Vector3D(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Computes the dot product with another vector.
     * @param v The other vector
     * @return Dot product result
     */
    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Normalizes this vector to unit length.
     * @return New normalized vector
     */
    public Vector3D normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        return new Vector3D(x / length, y / length, z / length);
    }

    /**
     * Compares this vector with another vector.
     * First compares magnitudes, then components if magnitudes are equal.
     * @param v Vector to compare with
     * @return 1 if this > v, -1 if this < v, 0 if equal
     */
    public int compare(Vector3D v) {
        if(this.value == v.value){
            if(this.x > v.x) return 1;
            else if(this.x < v.x) return -1;
            else{
                if(this.y > v.y) return 1;
                else if(this.y < v.y) return -1;
                else{
                    if(this.z > v.z) return 1;
                    else if(this.z < v.z) return -1;
                    else return 0;
                }
            }
        }
        else if(this.value > v.value) return 1;
        else return -1;
    }
}