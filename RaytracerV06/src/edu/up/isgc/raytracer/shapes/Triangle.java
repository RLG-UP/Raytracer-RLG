package edu.up.isgc.raytracer.shapes;

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


    /*
    @Override
    public Intersection[] intersect(Ray ray) {
        final double EPSILON = 1e-8;

        Vector3D v0 = this.getA();
        Vector3D v1 = this.getB();
        Vector3D v2 = this.getC();

        Vector3D edge2 = Vector3D.subtract(v1, v0);
        Vector3D edge1 = Vector3D.subtract(v2, v0);

        Vector3D pvec = Vector3D.crossProduct(ray.direction, edge2);
        double det = edge1.dot(pvec);

        if (Math.abs(det) < EPSILON) {
            return Intersection.nullIntersection(); // Ray is parallel to triangle
        }

        double invDet = 1.0 / det;
        Vector3D tvec = Vector3D.subtract(ray.origin, v0);
        double u = tvec.dot(pvec) * invDet;

        if (u < 0.0 || u > 1.0) {
            return Intersection.nullIntersection();
        }

        Vector3D qvec = Vector3D.crossProduct(tvec, edge1);
        double v = ray.direction.dot(qvec) * invDet;

        if (v < 0.0 || (u + v) > 1.0) {
            return Intersection.nullIntersection();
        }

        double t = edge2.dot(qvec) * invDet;

        if (t < EPSILON) {
            return Intersection.nullIntersection(); // Intersection behind ray origin
        }

        double w = 1.0 - u - v;

        Vector3D intersectionPoint = ray.origin.add(ray.direction.scale(t));

        // If you're using interpolated normals or color, store u, v, w somewhere
        this.u = u;
        this.v = v;
        this.w = w;

        return new Intersection[] {
                new Intersection(intersectionPoint, t, super.getColor())
        };
    }

     */





    /*

    public Intersection[] intersect(Ray ray) {
        Vector3D v0 = this.getA();  // First vertex
        Vector3D v1 = this.getB();  // Second vertex
        Vector3D v2 = this.getC();  // Third vertex

        Vector3D edge1 = Vector3D.subtract(v1, v0);
        Vector3D edge2 = Vector3D.subtract(v2, v0);

        Vector3D h = Vector3D.crossProduct(ray.direction.scale(-1), edge2);
        double det = edge1.dot(h);

        final double EPSILON = 1e-8;
        if (Math.abs(det) < EPSILON) {
            return Intersection.nullIntersection();  // Ray is parallel to triangle
        }

        double invDet = 1.0 / det;
        Vector3D s = Vector3D.subtract(ray.origin, v0);
        u = invDet * s.dot(h);
        if (u < 0.0 || u > 1.0) {
            return Intersection.nullIntersection();
        }

        Vector3D q = Vector3D.crossProduct(s, edge1);
        v = invDet * ray.direction.scale(-1).dot(q);
        if (v < 0.0 || u + v > 1.0) {
            return Intersection.nullIntersection();
        }
        w = 1-u-v;

        double t = invDet * edge2.dot(q);
        if (t < EPSILON) {
            return Intersection.nullIntersection();  // Intersection behind the ray origin
        }

        Vector3D intersectionPoint = ray.origin.add(ray.direction.scale(t));
        return new Intersection[] {
                new Intersection(intersectionPoint, t, super.getColor())
        };
    }

     */

    /*
    @Override
    public Intersection[] intersect(Ray ray) {
        Vector3D v0 = this.getA();
        Vector3D v1 = this.getB();
        Vector3D v2 = this.getC();

        Vector3D edge1 = Vector3D.subtract(v1, v0);
        Vector3D edge2 = Vector3D.subtract(v2, v0);

        Vector3D h = Vector3D.crossProduct(ray.direction, edge2);
        double a = edge1.dot(h);
        final double EPSILON = 1e-8;

        if (Math.abs(a) < EPSILON) {
            return Intersection.nullIntersection();  // Ray is parallel to triangle
        }

        double f = 1.0 / a;
        Vector3D s = Vector3D.subtract(ray.origin, v0);
        u = f * s.dot(h);

        if (u < 0.0 || u > 1.0) {
            return Intersection.nullIntersection();
        }

        Vector3D q = Vector3D.crossProduct(s, edge1);
        v = f * ray.direction.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return Intersection.nullIntersection();
        }

        double t = f * edge2.dot(q);
        if (t < EPSILON) {
            return Intersection.nullIntersection();  // Behind the ray or too close
        }

        w = 1.0 - u - v;
        Vector3D intersectionPoint = ray.origin.add(ray.direction.scale(t));
        return new Intersection[] { new Intersection(intersectionPoint, t, super.getColor()) };
    }

     */




    @Override
    public Object3D returnZero(){
        return new Triangle(Vector3D.getZero(), Vector3D.getZero(), Vector3D.getZero(), null);
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
        Vector3D v = Vector3D.subtract(this.getB(), this.getA()).normalize().scale(-1);
        Vector3D w = Vector3D.subtract(this.getA(), this.getC()).normalize().scale(-1);
        return Vector3D.crossProduct(v, w).normalize();
    }

    public Vector3D calculateNormalPoint(float u, float v, float w){ return this.getnA().scale(this.w).add(this.getnB().scale(this.v)).add(this.getnC().scale(this.u)).normalize(); }


    @Override
    public Color addLight(Vector3D point) {
        Color finalColor = new Color(0, 0, 0);
        Vector3D N = this.getnA().scale(this.w).add(this.getnB().scale(this.v)).add(this.getnC().scale(this.u)).normalize();
        //Vector3D N = this.getnA().scale((double) 1 /3).add(this.getnB().scale((double) 1 /3)).add(this.getnC().scale((double) 1 /3)).normalize();

        float ks = 1f;
        float p = 100f;
        float ka = 0.1f;  // Ambient constant
        float reflectivity = 0.5f;
        float ambientIntensity = ka * Light.getAmbientLight();

        for (Light light : Light.getLights()) {
            Vector3D l = Vector3D.getZero();
            double lightDistance = Double.MAX_VALUE;

            if (light.type().equals("directional")) {
                l = light.getDirection().normalize();
            } else if (light.type().equals("point") || light.type().equals("spot")) {
                l = light.getDirection(point).normalize();
                lightDistance = Vector3D.subtract(light.getPosition(), point).value;
            }

            boolean inShadow = Scene.isInShadow(point, N, light, this);

            float lambertian = 0;
            float blinn = 0;

            if (!inShadow) {
                lambertian = (float) Math.max(N.dot(l) * light.getAttenuation(), 0.0);
                Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
                blinn = (float) (ks * Math.pow(Math.max(N.dot(h), 0.0), p));
            }

            // Individual components
            Color ambient  = Light.shine(light.getColor(), super.getColor(), ambientIntensity, true);
            Color diffuse  = Light.shine(light.getColor(), super.getColor(), lambertian, true);
            Color specular = Light.shine(light.getColor(), super.getColor(), blinn, false); // specular is independent of object color

            // Combine them
            Color lightContribution = new Color(
                    clamp(ambient.getRed() + diffuse.getRed() + specular.getRed(), 0, 255),
                    clamp(ambient.getGreen() + diffuse.getGreen() + specular.getGreen(), 0, 255),
                    clamp(ambient.getBlue() + diffuse.getBlue() + specular.getBlue(), 0, 255)
            );

            // Accumulate contribution
            finalColor = new Color(
                    clamp(finalColor.getRed() + lightContribution.getRed(), 0, 255),
                    clamp(finalColor.getGreen() + lightContribution.getGreen(), 0, 255),
                    clamp(finalColor.getBlue() + lightContribution.getBlue(), 0, 255)
            );
        }

        Color reflectContribution = Scene.castReflection(point, N, this, 5);
        finalColor = new Color(
                clamp(Math.round( finalColor.getRed() * (1 - reflectivity) + (reflectivity * reflectContribution.getRed()) ), 0, 255),
                clamp(Math.round( finalColor.getGreen() * (1 - reflectivity) + (reflectivity * reflectContribution.getGreen()) ), 0, 255),
                clamp(Math.round( finalColor.getBlue() * (1 - reflectivity) + (reflectivity * reflectContribution.getBlue()) ), 0, 255)
        );
        return finalColor;
    }





    /*
    public Color addLight(Vector3D point){
        float lambertian = 0;
        float blinn = 0;
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
                //lambertian = (float) clamp(N.dot(l), 0.0, 1.0);
            }
            else if(light.type().equals("point") || light.type().equals("spot")){
                l = light.getDirection(point).normalize();
            }

            Intersection shadowPoint = super.castShadow(point, l);
            float ka = (float) ( 0.1 + (0.1 * lambertian) );
            float ambient = (float) ka * Light.getAmbientLight();

            if(shadowPoint == null) {
                lambertian = (float) clamp(N.dot(l) * light.getAttenuation(), 0.0, 1.0);
                Vector3D h = Vector3D.subtract(Camera.getCameraPosition(), point).normalize().add(l).normalize();
                blinn = (float) (ks * Math.pow(clamp(N.dot(h), 0, 1), p));
            }else{
                System.out.println("Shadow Point");
            }

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

     */

    @Override
    public String type(){ return "triangle"; }
}