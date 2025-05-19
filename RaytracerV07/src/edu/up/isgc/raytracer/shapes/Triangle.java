package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.BoundingBox;
import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.world.Scene;

import java.awt.*;

import static java.lang.Math.clamp;

public class Triangle extends Object3D {
    private Vector3D A, B, C;
    private Vector3D nA, nB, nC;
    private double u, v, w;

    public Triangle(Vector3D A, Vector3D B, Vector3D C, Color color, double refraction, double transparency) {
        super(color, refraction, transparency);
        this.setA(A);
        this.setB(B);
        this.setC(C);
    }

    public Triangle(Vector3D A, Vector3D B, Vector3D C, Vector3D nA, Vector3D nB, Vector3D nC, Color color, double refraction, double transparency) {
        super(color, refraction, transparency);
        this.setA(A);
        this.setB(B);
        this.setC(C);
        this.setnA(nA);
        this.setnB(nB);
        this.setnC(nC);
    }


    //INTERSECT METHOD BEFORE REFACTOR

    /*
    @Override
    public Intersection[] intersect(Ray ray) {
        Vector3D v2v0 = Vector3D.subtract(this.getC(), this.getA());
        Vector3D v1v0 = Vector3D.subtract(this.getB(), this.getA());
        Vector3D D = ray.direction.normalize();
        Vector3D P = Vector3D.crossProduct(D, v1v0);
        double det = v2v0.dot(P);
        double invDet = 1.0 / det;
        Vector3D T = Vector3D.subtract(ray.origin, this.getA());
        u = invDet * T.dot(P);

        if(u < 0 || u > 1) return Intersection.nullIntersection();

        Vector3D Q = Vector3D.crossProduct(T, v2v0);
        v = invDet * D.dot(Q);
        if( v< 0 || (u+v) > (1 + Camera.getEpsilon())) return Intersection.nullIntersection();
        w = 1-u-v;

        double t = invDet * Q.dot(v1v0);

        Vector3D point = ray.origin.add(D.scale(t));
        //return new Intersection[]{new Intersection(point, t, this.addLight(point))};
        return new Intersection[]{new Intersection(point, t, super.getColor())};
    }

     */



    /*
    @Override
    public Intersection[] intersect(Ray ray) {
        final double EPSILON = 1e-8;

        Vector3D v0 = this.getA();
        Vector3D v1 = this.getB();
        Vector3D v2 = this.getC();

        Vector3D edge1 = Vector3D.subtract(v1, v0);
        Vector3D edge2 = Vector3D.subtract(v2, v0);

        Vector3D pvec = Vector3D.crossProduct(ray.direction, edge2);
        double det = edge1.dot(pvec);

        // Use absolute value check to avoid missing intersections due to small determinant
        if (Math.abs(det) < EPSILON) return Intersection.nullIntersection();

        double invDet = 1.0 / det;
        Vector3D tvec = Vector3D.subtract(ray.origin, v0);
        double u = tvec.dot(pvec) * invDet;

        if (u < 0.0 || u > 1.0) return Intersection.nullIntersection();

        Vector3D qvec = Vector3D.crossProduct(tvec, edge1);
        double v = ray.direction.dot(qvec) * invDet;

        if (v < 0.0 || (u + v) > 1.0) return Intersection.nullIntersection();

        double t = edge2.dot(qvec) * invDet;

        if (t < EPSILON) return Intersection.nullIntersection();

        // Save barycentric coordinates
        this.u = u;
        this.v = v;
        this.w = 1.0 - u - v;

        Vector3D intersectionPoint = ray.origin.add(ray.direction.scale(t));

        // Lighting-aware rendering: if you have lighting logic, apply it here
        return new Intersection[] {
                new Intersection(intersectionPoint, t, super.getColor()) // <- or super.getColor()
        };
    }

     */

    //THIS IS THE METHOD WITH THE COLOR PROBLEM BUT ALMOST THERE

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
                this.u = u;
                this.v = v;
                this.w = 1-u-v;
                double t = invDet * vectorQ.dot(v1v0);
                intersection.point = ray.origin.add(D.scale(t));
                intersection.distance = t;
                intersection.color = super.getColor();
                intersection.setNormal(this.calculateNormalPoint((float) this.u, (float) this.v, (float) this.w));
                return new Intersection[] {intersection};
            }
        }
        return Intersection.nullIntersection();

    }


    public Vector3D normal(){
        Vector3D v = Vector3D.subtract(this.getB(), this.getA()).normalize().scale(-1);
        Vector3D w = Vector3D.subtract(this.getA(), this.getC()).normalize().scale(-1);
        return Vector3D.crossProduct(v, w).normalize();
    }

    public Vector3D calculateNormalPoint(float u, float v, float w){ return this.getnA().scale(this.w).add(this.getnB().scale(this.v)).add(this.getnC().scale(this.u)).normalize(); }


    @Override
    public Color addLight(Vector3D point) {
        Vector3D N = this.getnA().scale(this.w).add(this.getnB().scale(this.v)).add(this.getnC().scale(this.u)).normalize();
        return Light.calculateColor(N, point, this);
    }

    @Override
    public String type(){ return "triangle"; }

    @Override
    public Object3D returnZero(){
        return new Triangle(Vector3D.getZero(), Vector3D.getZero(), Vector3D.getZero(), null, 0, 0);
    }

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

}