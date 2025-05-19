package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.shapes.Object3D;

public class Node {
    public double distance;    // distance from camera to object's bounding box center
    public Node left;
    public Node right;
    public Node parent;
    public boolean isRed;
    public boolean isBlack;
    public Object objectRef; // You can store your 3D object reference here if needed

    public Node(double distance, Object3D objectRef) {
        this.distance = distance;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.isRed = true;   // New nodes are red by default
        this.isBlack = !this.isRed;
        this.objectRef = objectRef;
    }

    public void paintRed() {
        this.isRed = true;
        this.isBlack = false;
    }

    public void paintBlack() {
        this.isRed = false;
        this.isBlack = true;
    }

    @Override
    public String toString() {
        return String.format("Node(distance=%.3f)", distance);
    }
}
