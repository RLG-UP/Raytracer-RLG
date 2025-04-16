package edu.up.isgc.raytracer;

import java.awt.*;
import java.util.Vector;

public class Triangle extends Object3D {
    private Vector3D A, B, C;
    private Color color;

    public Triangle(Vector3D A, Vector3D B, Vector3D C, Color color){
        super(color);
        this.setA(A);
        this.setB(B);
        this.setC(C);
    }

    @Override
    public Intersection[] intersect(Ray ray) {
        Vector3D v2v0 = Vector3D.subtract(this.getC(), this.getA());
        Vector3D v1v0 = Vector3D.subtract(this.getB(), this.getA());
        Vector3D P = Vector3D.crossProduct(ray.getDirection(), v1v0);
        double det = v2v0.dot(P);
        double invDet = 1.0 / det;
        Vector3D T = Vector3D.subtract(ray.getOrigin(), this.getA());
        double u = invDet * T.dot(P);

        if(u < 0 || u > 1) return Intersection.nullIntersection();

        Vector3D Q = Vector3D.crossProduct(T, v2v0);
        double v = invDet * ray.getDirection().dot(Q);
        if( v< 0 || (u+v) > (1 + Camera.getEpsilon())) return Intersection.nullIntersection();

        double t = invDet * Q.dot(v1v0);
        return new Intersection[]{new Intersection(ray.getOrigin().add(ray.getDirection().scale(t)), t, this.getColor())};
    }

    public Vector3D getA() { return this.A; }
    public void setA(Vector3D a) { this.A = a; }

    public Vector3D getB() { return this.B; }
    public void setB(Vector3D b) { this.B = b; }

    public Vector3D getC() { return this.C; }
    public void setC(Vector3D c) { this.C = c; }
}
