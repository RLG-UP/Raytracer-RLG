package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

public class Triangle extends Object3D {
    private Vector3D A, B, C;

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
        Vector3D P = Vector3D.crossProduct(ray.direction, v1v0);
        double det = v2v0.dot(P);
        double invDet = 1.0 / det;
        Vector3D T = Vector3D.subtract(ray.origin, this.getA());
        double u = invDet * T.dot(P);

        if(u < 0 || u > 1) return Intersection.nullIntersection();
        Vector3D Q = Vector3D.crossProduct(T, v2v0);
        double v = invDet * ray.direction.dot(Q);
        if( v< 0 || (u+v) > (1 + Camera.getEpsilon())) return Intersection.nullIntersection();

        double t = invDet * Q.dot(v1v0);

        return new Intersection[]{new Intersection(ray.origin.add(ray.direction.scale(t)), t, this.color)};
    }

    public Vector3D getA() { return this.A; }
    public void setA(Vector3D a) { this.A = a; }

    public Vector3D getB() { return this.B; }
    public void setB(Vector3D b) { this.B = b; }

    public Vector3D getC() { return this.C; }
    public void setC(Vector3D c) { this.C = c; }
}
