package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.shapes.Object3D;

/**
 * Represents a node used in a binary tree structure, such as a BVH (Bounding Volume Hierarchy)
 * or Red-Black tree for spatial partitioning and optimization in ray tracing.
 */
public class Node {

    /**
     * The distance from the camera to the center of the bounding box of the object.
     * Used as a sorting metric for tree construction.
     */
    public double centroidValue;

    /**
     * Reference to the left child node.
     */
    public Node left;

    /**
     * Reference to the right child node.
     */
    public Node right;

    /**
     * Reference to the parent node.
     */
    public Node parent;

    /**
     * Flag indicating if the node is red (used in red-black tree balancing).
     */
    public boolean isRed;

    /**
     * Flag indicating if the node is black (used in red-black tree balancing).
     */
    public boolean isBlack;

    /**
     * Reference to the 3D object associated with this node.
     */
    public Object3D objectRef;

    /**
     * Constructs a new node with a given centroid value and associated 3D object.
     * By default, the node is red (as per red-black tree rules).
     *
     * @param centroidValue The distance from the camera to the object's bounding box center.
     * @param objectRef     The 3D object associated with this node.
     */
    public Node(double centroidValue, Object3D objectRef) {
        this.centroidValue = centroidValue;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.isRed = true;   // New nodes are red by default
        this.isBlack = !this.isRed;
        this.objectRef = objectRef;
    }

    /**
     * Checks whether this node is a leaf (i.e., has no children).
     *
     * @return {@code true} if the node has no children; {@code false} otherwise.
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /**
     * Marks this node as red and updates the black flag accordingly.
     */
    public void paintRed() {
        this.isRed = true;
        this.isBlack = false;
    }

    /**
     * Marks this node as black and updates the red flag accordingly.
     */
    public void paintBlack() {
        this.isRed = false;
        this.isBlack = true;
    }

    /**
     * Returns a string representation of the node, including its centroid value.
     *
     * @return A string describing the node.
     */
    @Override
    public String toString() {
        return String.format("Node(distance=%.3f)", centroidValue);
    }
}
