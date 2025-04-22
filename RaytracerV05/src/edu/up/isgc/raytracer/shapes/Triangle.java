package edu.up.isgc.raytracer.shapes;

import edu.up.isgc.raytracer.lighting.Light;
import edu.up.isgc.raytracer.world.Camera;
import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;

import java.awt.*;

import static java.lang.Math.clamp;

public class Triangle extends Object3D {
    private Vector3D A, B, C;

    public Triangle(Vector3D A, Vector3D B, Vector3D C, Color color){
        super(color);
        this.setA(A);
        this.setB(B);
        this.setC(C);
    }

    @Override
    public Intersection[] intersect(Ray ray, Light light) {
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

        return new Intersection[]{new Intersection(ray.origin.add(ray.direction.scale(t)), t, this.addLight(light))};
    }

    public Vector3D getA() { return this.A; }
    public void setA(Vector3D a) { this.A = a; }

    public Vector3D getB() { return this.B; }
    public void setB(Vector3D b) { this.B = b; }

    public Vector3D getC() { return this.C; }
    public void setC(Vector3D c) { this.C = c; }

    public Vector3D normal(){
        Vector3D v = Vector3D.subtract(this.getB(), this.getA()).normalize();
        Vector3D w = Vector3D.subtract(this.getA(), this.getC()).normalize();
        return Vector3D.crossProduct(v, w).normalize();
    }

    public Color addLight(Light light){
        float lambertian = 0;
        if (light.type().equals("directional")) {
            lambertian = (float)clamp(this.normal().dot(light.getDirection()), 0.0, 1.0);
        }
        else if(light.type().equals("point") || light.type().equals("spot")){
            lambertian = (float) clamp(this.normal().dot(light.getDirection(this.normal())), 0.0, 1.0);
        }

        return Light.shine(light.getColor(), super.getColor(), lambertian * light.getAttenuation());
    }

    @Override
    public String type(){ return "triangle"; }
}