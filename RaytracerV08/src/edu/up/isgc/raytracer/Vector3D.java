package edu.up.isgc.raytracer;

/**
 * Represents a 3D vector with x, y, z components and common vector operations.
 * Includes methods for vector math and comparison.
 */
public class Vector3D {
    public double x, y, z;  // Vector components
    public double value;    // Precomputed magnitude of the vector
    private static Vector3D zero = new Vector3D(0, 0, 0);
    public static final double doubleOne = 1.0000000000000000000000;
    public static final double doubleZero = 0.0000000000000000000000;
    public static final float floatOne = 1.0000000000000000000000f;
    public static final float floatZero = 0.0000000000000000000000f;

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
    //public Vector3D subtract(Vector3D v) {return new Vector3D(x - v.x, y - v.y, z - v.z);}
    public static Vector3D subtract(Vector3D w, Vector3D v) {
        return new Vector3D(w.x - v.x, w.y - v.y, w.z - v.z);
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
        if(length == 0) return zero;
        return new Vector3D(x / length, y / length, z / length);
    }

    public static Vector3D crossProduct(Vector3D a, Vector3D b){
        double x,y,z;
        x = (a.y * b.z - a.z * b.y);
        y = (a.z * b.x - a.x * b.z);
        z = (a.x * b.y - a.y * b.x);
        return new Vector3D(x, y, z);
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

    public void translate(float tX, float tY, float tZ) {
        float[][] translationMatrix = Vector3D.getIdentityMatrix();
        translationMatrix[0][3] = tX;
        translationMatrix[1][3] = tY;
        translationMatrix[2][3] = tZ;

        translationMatrix = matrixMultiply(translationMatrix, this.turnToPilarMatrix());

        this.x = translationMatrix[0][0];
        this.y = translationMatrix[1][0];
        this.z = translationMatrix[2][0];
    }

    public void scale(float sX, float sY, float sZ) {
        float[][] scaleMatrix = new Vector3D(sX, sY, sZ).turnToDiagonalMatrix();

        scaleMatrix= matrixMultiply(scaleMatrix, this.turnToPilarMatrix());

        this.x = scaleMatrix[0][0];
        this.y = scaleMatrix[1][0];
        this.z = scaleMatrix[2][0];
    }

    public void rotate(float rX, float rY, float rZ, boolean inRadians) {
        float[][] rotateMatrix = this.turnToPilarMatrix();

        if(!inRadians){
            rX = (float) Math.toRadians(rX);
            rY = (float) Math.toRadians(rY);
            rZ = (float) Math.toRadians(rZ);
        }

        if(rX != 0) {
            float[][] rotateXMatrix = Vector3D.getIdentityMatrix();
            rotateXMatrix[1][1] = (float) Math.cos(rX);
            rotateXMatrix[1][2] = (float) ((-1.0f) * Math.sin(rX));
            rotateXMatrix[2][1] = (float) Math.sin(rX);
            rotateXMatrix[2][2] = (float) Math.cos(rX);
            rotateMatrix = matrixMultiply(rotateXMatrix, rotateMatrix);
        }

        if(rY != 0) {
            float[][] rotateYMatrix = Vector3D.getIdentityMatrix();
            rotateYMatrix[0][0] = (float) Math.cos(rY);
            rotateYMatrix[0][2] = (float) Math.sin(rY);
            rotateYMatrix[2][0] = (float) ((-1.0f) * Math.sin(rY));
            rotateYMatrix[2][2] = (float) Math.cos(rY);
            rotateMatrix = matrixMultiply(rotateYMatrix, rotateMatrix);
        }

        if(rZ != 0) {
            float[][] rotateZMatrix = Vector3D.getIdentityMatrix();
            rotateZMatrix[0][0] = (float) Math.cos(rZ);
            rotateZMatrix[0][1] = (float) ((-1.0f) * Math.sin(rZ));
            rotateZMatrix[1][0] = (float) Math.sin(rZ);
            rotateZMatrix[1][1] = (float) Math.cos(rZ);
            rotateMatrix = matrixMultiply(rotateZMatrix, rotateMatrix);
        }

        this.x = rotateMatrix[0][0];
        this.y = rotateMatrix[1][0];
        this.z = rotateMatrix[2][0];
    }

    public void rotate(float rX, float rY, float rZ) { this.rotate(rX, rY, rZ, false); }

    /*
    public static float[][] matrixMultiply(float[][] a, float[][] b) {
        float[][] c = new float[a[0].length][b.length];
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < b.length; j++){
                c[i][j] += a[i][j] * b[j][i];
            }
        }
        return c;
    }

     */

    public static float[][] matrixMultiply(float[][] a, float[][] b) {
        int aRows = a.length;
        int aCols = a[0].length;
        int bCols = b[0].length;

        float[][] result = new float[aRows][bCols];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                for (int k = 0; k < aCols; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }


    public float[][] turnToDiagonalMatrix(){
        return new float[][] {
                {(float) this.x, Vector3D.floatZero, Vector3D.floatZero, Vector3D.floatZero},
                {Vector3D.floatZero, (float) this.y, Vector3D.floatZero, Vector3D.floatZero},
                {Vector3D.floatZero, Vector3D.floatZero, (float) this.z, Vector3D.floatZero},
                {Vector3D.floatZero, Vector3D.floatZero, Vector3D.floatZero, Vector3D.floatOne}};
    }

    public float[][] turnToPilarMatrix(){
        return new float[][] {
                {(float) this.x},
                {(float) this.y},
                {(float) this.z},
                {Vector3D.floatOne}
        };
    }

    public static float[][] getIdentityMatrix(){
        return new float[][] {
                {Vector3D.floatOne, Vector3D.floatZero, Vector3D.floatZero, Vector3D.floatZero},
                {Vector3D.floatZero, Vector3D.floatOne, Vector3D.floatZero, Vector3D.floatZero},
                {Vector3D.floatZero, Vector3D.floatZero, Vector3D.floatOne, Vector3D.floatZero},
                {Vector3D.floatZero, Vector3D.floatZero, Vector3D.floatZero, Vector3D.floatOne}
        };
    }

    public static Vector3D multiply(Vector3D v, Vector3D w) { return new Vector3D(v.x * w.x, v.y * w.y, v.z * w.z); }

    public static Vector3D arrayToVector(Double[] array){
        return new Vector3D(array[0], array[1], array[2]);
    }

    public static Vector3D getZero() { return Vector3D.zero; }

    public String toString() { return "(" + x + "," + y + "," + z + ")"; }

    public static Vector3D rotateAroundAxis(Vector3D v, Vector3D axis, double angle) {
        Vector3D k = axis.normalize();
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);

        Vector3D term1 = v.scale(cosTheta);
        Vector3D term2 = Vector3D.crossProduct(k,v).scale(sinTheta);
        Vector3D term3 = k.scale(k.dot(v) * (1 - cosTheta));

        return term1.add(term2).add(term3);
    }

    public static Vector3D rotateAroundAxis(Vector3D v, Vector3D axis, double cosTheta, double sinTheta) {
        Vector3D k = axis.normalize();

        Vector3D term1 = v.scale(cosTheta);
        Vector3D term2 = Vector3D.crossProduct(k,v).scale(sinTheta);
        Vector3D term3 = k.scale(k.dot(v) * (1 - cosTheta));

        return term1.add(term2).add(term3);
    }


    public double get(int i) {
        if (i == 0) return x;
        else if (i == 1) return y;
        else if (i == 2) return z;
        return 0;
    }
}