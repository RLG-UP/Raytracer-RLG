package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.lighting.Material;
import edu.up.isgc.raytracer.optimization.BoundingBox;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.shapes.models.Texture;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.shapes.models.Polygon;

import java.awt.*;

import static java.lang.Math.clamp;


/**
 * Represents a triangular polygon in 3D space that can be rendered by a raytracer.
 * Supports vertex normals, texture coordinates, and material properties.
 * Implements the Möller-Trumbore algorithm for ray-triangle intersection.
 */
public class Triangle extends Object3D {
    private Vector3D A, B, C;
    private Vector3D nA, nB, nC;
    private Vector3D tA = Vector3D.getZero(), tB = Vector3D.getZero(), tC = Vector3D.getZero();
    private double u, v, w;
    private boolean hasNormals = false, hasTextures = false;
    private Polygon parent;

    /**
     * Constructs a basic triangle with uniform material properties.
     *
     * @param A First vertex position
     * @param B Second vertex position
     * @param C Third vertex position
     * @param color Base color of triangle
     * @param refraction Index of refraction
     * @param transparency Transparency level (0=opaque, 1=transparent)
     */
    public Triangle(Vector3D A, Vector3D B, Vector3D C, Color color, double refraction, double transparency) {
        super(color, refraction, transparency);
        this.setA(A);
        this.setB(B);
        this.setC(C);
    }
    /**
     * Constructs a triangle with vertex normals.
     *
     * @param A First vertex position
     * @param B Second vertex position
     * @param C Third vertex position
     * @param nA Normal at first vertex
     * @param nB Normal at second vertex
     * @param nC Normal at third vertex
     * @param color Base color
     * @param refraction Index of refraction
     * @param transparency Transparency level
     * @param parent Parent polygon
     */
    public Triangle(Vector3D A, Vector3D B, Vector3D C, Vector3D nA, Vector3D nB, Vector3D nC, Color color, double refraction, double transparency, Polygon parent) {
        super(color, refraction, transparency);
        this.setA(A);
        this.setB(B);
        this.setC(C);

        this.setnA(nA);
        this.setnB(nB);
        this.setnC(nC);
        this.setHasNormals(true);

        this.setParent(parent);
    }
    /**
     * Constructs a triangle with vertex normals and material properties.
     *
     * @param A First vertex position
     * @param B Second vertex position
     * @param C Third vertex position
     * @param nA Normal at first vertex
     * @param nB Normal at second vertex
     * @param nC Normal at third vertex
     * @param parent Parent polygon
     * @param material Material properties
     */
    public Triangle(Vector3D A, Vector3D B, Vector3D C, Vector3D nA, Vector3D nB, Vector3D nC, Polygon parent, Material material) {
        super(material);
        this.setA(A);
        this.setB(B);
        this.setC(C);

        this.setnA(nA);
        this.setnB(nB);
        this.setnC(nC);
        this.setHasNormals(true);

        this.setParent(parent);
        this.setMaterial(material);
        this.setHasMaterial(true);
        super.refraction = material.getRefraction();
        super.transparency = material.getTransparency();
    }
    /**
     * Constructs a triangle with vertex normals and texture coordinates.
     *
     * @param A First vertex position
     * @param B Second vertex position
     * @param C Third vertex position
     * @param nA Normal at first vertex
     * @param nB Normal at second vertex
     * @param nC Normal at third vertex
     * @param tA Texture coordinate at first vertex
     * @param tB Texture coordinate at second vertex
     * @param tC Texture coordinate at third vertex
     * @param color Base color
     * @param refraction Index of refraction
     * @param transparency Transparency level
     * @param parent Parent polygon
     */
    public Triangle(Vector3D A, Vector3D B, Vector3D C, Vector3D nA, Vector3D nB, Vector3D nC , Vector3D tA, Vector3D tB, Vector3D tC, Color color, double refraction, double transparency, Polygon parent) {
        super(color, refraction, transparency);
        this.setA(A);
        this.setB(B);
        this.setC(C);

        this.setnA(nA);
        this.setnB(nB);
        this.setnC(nC);
        this.setHasNormals(true);

        this.setTA(tA);
        this.setTB(tB);
        this.setTC(tC);
        this.setHasTextures(true);

        this.setParent(parent);
    }
    /**
     * Constructs a triangle with full attributes (normals, textures, material).
     *
     * @param A First vertex position
     * @param B Second vertex position
     * @param C Third vertex position
     * @param nA Normal at first vertex
     * @param nB Normal at second vertex
     * @param nC Normal at third vertex
     * @param tA Texture coordinate at first vertex
     * @param tB Texture coordinate at second vertex
     * @param tC Texture coordinate at third vertex
     * @param parent Parent polygon
     * @param material Material properties
     */
    public Triangle(Vector3D A, Vector3D B, Vector3D C, Vector3D nA, Vector3D nB, Vector3D nC , Vector3D tA, Vector3D tB, Vector3D tC, Polygon parent, Material material) {
        // In Triangle constructor:
        super(material);
        this.setA(A);
        this.setB(B);
        this.setC(C);

        this.setnA(nA);
        this.setnB(nB);
        this.setnC(nC);
        this.setHasNormals(true);

        this.setTA(tA);
        this.setTB(tB);
        this.setTC(tC);
        this.setHasTextures(true);
        this.setHasMaterial(true);

        this.setParent(parent);
    }


    /**
     * Calculates ray-triangle intersection using Möller-Trumbore algorithm.
     * Supports textured triangles by sampling from material texture maps.
     *
     * @param ray The ray to test for intersection
     * @return Array containing intersection data or null if no intersection
     */
    @Override
    public Intersection[] intersect(Ray ray) {
        Intersection intersection = new Intersection(null, -1, null);
        Vector3D D = ray.direction.normalize().scale(-1);
        Vector3D[] vert = new Vector3D[]{this.getA(), this.getB(), this.getC()};
        Vector3D v2v0 = Vector3D.subtract(vert[2], vert[0]);
        Vector3D v1v0 = Vector3D.subtract(vert[1], vert[0]);
        Vector3D vectorP = Vector3D.crossProduct(D, v1v0);
        double det = v2v0.dot(vectorP);
        double invDet = 1.0 / det;
        Vector3D vectorT = Vector3D.subtract(ray.origin, vert[0]);
        double u = invDet * vectorT.dot(vectorP);

        if (!(u < 0 || u > 1)) {
            Vector3D vectorQ = Vector3D.crossProduct(vectorT, v2v0);
            double v = invDet * D.dot(vectorQ);
            if (!(v < 0 || (u + v) > (1.0 + Camera.getEpsilon()))) {
                double w = 1-u-v;
                double t = invDet * vectorQ.dot(v1v0);
                intersection.point = ray.origin.add(D.scale(t));
                intersection.distance = t;
                if(this.getHasTextures()) {
                    Vector3D uvTexture= Texture.ericson(intersection.point, this.getA(), this.getB(), this.getC(), this.getTA(), this.getTB(), this.getTC());
                    if(this.getHasNormals()) {
                            //System.out.println("%%%%%%%%%%%%%%%%");
                            int texX = (int) (uvTexture.x * (super.getMaterial().getTextureMap().getWidth() - 1));
                            int texY = (int) ((1 - uvTexture.y) * (super.getMaterial().getTextureMap().getHeight() - 1)); // flip v if needed

                            texX = Math.max(0, Math.min(texX, super.getMaterial().getTextureMap().getWidth() - 1));
                            texY = Math.max(0, Math.min(texY, super.getMaterial().getTextureMap().getHeight() - 1));

                            intersection.color = new Color(super.getMaterial().getTextureMap().getRGB(texX, texY));
                    }
                    else{
                        intersection.color = super.getColor();
                    }
                }else if(this.getHasMaterial()){
                    intersection.color = super.getMaterial().getColor();
                    //intersection.color = Color.white;
                }else{
                    intersection.color = super.getColor();
                }
                intersection.setNormal(this.calculateNormalPoint((float) u, (float) v, (float) w));
                return new Intersection[] {intersection};
            }
        }
        return Intersection.nullIntersection();

    }
    /**
     * Calculates the face normal of the triangle.
     *
     * @return Normalized face normal vector
     */

    public Vector3D normal(){
        Vector3D v = Vector3D.subtract(this.getB(), this.getA()).normalize().scale(-1);
        Vector3D w = Vector3D.subtract(this.getA(), this.getC()).normalize().scale(-1);
        return Vector3D.crossProduct(v, w).normalize();
    }

    /**
     * Interpolates vertex normals using barycentric coordinates.
     *
     * @param u First barycentric coordinate
     * @param v Second barycentric coordinate
     * @param w Third barycentric coordinate (1-u-v)
     * @return Interpolated normal vector
     */
    public Vector3D calculateNormalPoint(float u, float v, float w){ return this.getnA().scale(w).add(this.getnB().scale(v)).add(this.getnC().scale(u)).normalize(); }


    /**
     * Calculates lighting at an intersection point.
     *
     * @param intersection Contains position, normal and material data
     * @return Color with lighting applied
     */
    @Override
    public Color addLight(Intersection intersection) {
        Vector3D N = intersection.getNormal();
        return Light.calculateColor(N, intersection.point, this, intersection);
    }
    /**
     * Returns the type identifier of this object.
     *
     * @return "triangle"
     */
    @Override
    public String type(){ return "triangle"; }
    /**
     * Returns a default zero-state triangle instance.
     *
     * @return Triangle at origin with zero area
     */
    @Override
    public Object3D returnZero(){
        return new Triangle(Vector3D.getZero(), Vector3D.getZero(), Vector3D.getZero(), null, 0, 0);
    }

    /**
     * Computes an axis-aligned bounding box for this triangle.
     *
     * @return Bounding box containing the triangle
     */
    @Override
    public BoundingBox getBB() {
        double minX = Math.min(this.getA().x, Math.min(this.getB().x, this.getC().x));
        double minY = Math.min(this.getA().y, Math.min(this.getB().y, this.getC().y));
        double minZ = Math.min(this.getA().z, Math.min(this.getB().z, this.getC().z));

        double maxX = Math.max(this.getA().x, Math.max(this.getB().x, this.getC().x));
        double maxY = Math.max(this.getA().y, Math.max(this.getB().y, this.getC().y));
        double maxZ = Math.max(this.getA().z, Math.max(this.getB().z, this.getC().z));

        return new BoundingBox(
                new Vector3D(minX, minY, minZ),
                new Vector3D(maxX, maxY, maxZ)
        );
    }
    // Standard getters and setters with basic documentation
    public Vector3D getA() { return this.A; }

    public void setA(Vector3D a) { this.A = a; }
    public Vector3D getB() { return this.B; }

    public void setB(Vector3D b) { this.B = b; }
    public Vector3D getC() { return this.C; }

    public void setC(Vector3D c) { this.C = c; }
    public Vector3D getnA() { return nA; }

    public void setnA(Vector3D nA) { this.nA = nA; }
    public Vector3D getnB() { return nB; }

    public void setnB(Vector3D nB) { this.nB = nB; }
    public Vector3D getnC() { return nC; }

    public void setnC(Vector3D nC) { this.nC = nC; }

    public Vector3D getTA() {
        return tA;
    }

    public void setTA(Vector3D tA) {
        this.tA = tA;
    }

    public Vector3D getTB() {
        return tB;
    }

    public void setTB(Vector3D tB) {
        this.tB = tB;
    }

    public Vector3D getTC() {
        return tC;
    }

    public void setTC(Vector3D tC) {
        this.tC = tC;
    }

    public boolean getHasNormals() {
        return hasNormals;
    }

    public void setHasNormals(boolean hasNormals) {
        this.hasNormals = hasNormals;
    }

    public boolean getHasTextures() {
        return hasTextures;
    }

    public void setHasTextures(boolean hasTextures) {
        this.hasTextures = hasTextures;
    }

    public Polygon getParent() {
        return parent;
    }

    public void setParent(Polygon parent) {
        this.parent = parent;
    }

}