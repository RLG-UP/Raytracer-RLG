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
    private Vector3D nA, nB, nC;
    private double u, v, w;

    public Triangle(Vector3D A, Vector3D B, Vector3D C, Color color){
        super(color);
        this.setA(A);
        this.setB(B);
        this.setC(C);
    }

    public Triangle(Vector3D A, Vector3D B, Vector3D C, Vector3D nA, Vector3D nB, Vector3D nC, Color color){
        super(color);
        this.setA(A);
        this.setB(B);
        this.setC(C);
        this.setnA(nA);
        this.setnB(nB);
        this.setnC(nC);
    }

    @Override
    public Intersection[] intersect(Ray ray) {
        Vector3D v2v0 = Vector3D.subtract(this.getC(), this.getA());
        Vector3D v1v0 = Vector3D.subtract(this.getB(), this.getA());
        Vector3D P = Vector3D.crossProduct(ray.direction, v1v0);
        double det = v2v0.dot(P);
        double invDet = 1.0 / det;
        Vector3D T = Vector3D.subtract(ray.origin, this.getA());
        u = invDet * T.dot(P);

        if(u < 0 || u > 1) return Intersection.nullIntersection();
        Vector3D Q = Vector3D.crossProduct(T, v2v0);
        v = invDet * ray.direction.dot(Q);
        if( v< 0 || (u+v) > (1 + Camera.getEpsilon())) return Intersection.nullIntersection();
        w = 1-u-v;

        double t = invDet * Q.dot(v1v0);

        Vector3D point = ray.origin.add(ray.direction.scale(t));
        return new Intersection[]{new Intersection(point, t, this.addLight(point))};
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

    public Vector3D normal(){
        Vector3D v = Vector3D.subtract(this.getB(), this.getA()).normalize();
        Vector3D w = Vector3D.subtract(this.getA(), this.getC()).normalize();
        return Vector3D.crossProduct(v, w).normalize();
    }

    public Color addLight(Vector3D point){
        float lambertian = 0;
        Color finalColor = new Color(0,0,0);
        float lightAttenuation = 0;

        Vector3D N = this.getnA().scale(this.w).add(this.getnB().scale(this.v)).add(this.getnC().scale(this.u)).normalize();
        float ks = 1f;
        float p = 100;

        for(Light light : Light.getLights()){
            Vector3D l = Vector3D.getZero();
            if (light.type().equals("directional")) {
                l = light.getDirection();
                //lambertian = (float)clamp(Light.ericson(point, this.getA(), this.getC(), this.getB(),  this.getnA(), this.getnC(), this.getnB()).dot(light.getDirection()), 0.0, 1.0);
                lambertian = (float) clamp(N.dot(l), 0.0, 1.0);
            }
            else if(light.type().equals("point") || light.type().equals("spot")){
                l = light.getDirection(point).normalize();
                lambertian = (float) clamp(N.dot(l) * light.getAttenuation(), 0.0, 1.0);
            }

            Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
            float blinn = (float) ( ks * Math.pow(clamp(N.dot(h), 0,1), p) );
            float ka = (float) ( 0.1 + (0.1 * lambertian) );
            float ambient = (float) ka * Light.getAmbientLight();
            lambertian = (float) clamp(ambient + lambertian + blinn, 0.0, 1.0);

            Color lightContribution = Light.shine(light.getColor(), super.getColor(), lambertian);

            finalColor = new Color(
                    clamp(finalColor.getRed()   + lightContribution.getRed(),   0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue()  + lightContribution.getBlue(),  0, 255)
            );
        }
        return finalColor;

    }

    @Override
    public String type(){ return "triangle"; }
}