package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.shapes.Object3D;

import java.util.Collections;
import java.util.List;

public class RBTree {
    public Node root;

    public RBTree() {
        this.root = null;
    }

    // Left rotation
    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;

        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    // Right rotation
    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;

        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
    }

    // Fix insertion to maintain red-black properties
    private void fixInsertion(Node node) {
        while (node != root && node.parent.isRed) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;

                // Case 1: Uncle is red
                if (uncle != null && uncle.isRed) {
                    node.parent.paintBlack();
                    uncle.paintBlack();
                    node.parent.parent.paintRed();
                    node = node.parent.parent;
                } else {
                    // Case 2: Node is right child
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Case 3: Node is left child
                    node.parent.paintBlack();
                    node.parent.parent.paintRed();
                    rotateRight(node.parent.parent);
                }
            } else {
                // Mirror cases
                Node uncle = node.parent.parent.left;

                if (uncle != null && uncle.isRed) {
                    node.parent.paintBlack();
                    uncle.paintBlack();
                    node.parent.parent.paintRed();
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.paintBlack();
                    node.parent.parent.paintRed();
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.paintBlack();
    }

    // Public insert method
    public void insert(Object3D obj) {
        BoundingBox bbox = obj.getBB();
        Vector3D centroid = bbox.getMin().add(bbox.getMax()).scale(0.5);
        Node newNode = new Node(centroid.x, obj);

        if (root == null) {
            root = newNode;
            root.paintBlack();
            return;
        }

        Node current = root;
        Node parent = null;
        int depth = 0;

        // Find insertion point
        while (current != null) {
            parent = current;
            double valueToCompare = getAxisValue(centroid, depth % 3);

            if (valueToCompare < current.centroidValue) {
                current = current.left;
            } else {
                current = current.right;
            }
            depth++;
        }

        // Insert new node
        double valueToCompare = getAxisValue(centroid, (depth-1) % 3);
        if (valueToCompare < parent.centroidValue) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;

        // Fix RB tree properties
        fixInsertion(newNode);
    }

    private double getAxisValue(Vector3D centroid, int axis) {
        return switch (axis) {
            case 0 -> centroid.x;
            case 1 -> centroid.y;
            case 2 -> centroid.z;
            default -> centroid.x;
        };
    }

    public Intersection traverse(Node node, Ray ray) {
        if (node == null) return null;

        // First check if ray intersects this node's bounding box
        if (!node.objectRef.getBB().bbIntersects(ray)) {
            return null;
        }

        Intersection closest = null;

        // Check this node's object
        Intersection[] nodeIntersections = node.objectRef.intersect(ray);
        if (nodeIntersections != null) {
            for (Intersection intersection : nodeIntersections) {
                if (intersection != null &&
                        (closest == null || intersection.distance < closest.distance)) {
                    closest = intersection;
                }
            }
        }

        // Recursively check children
        Intersection leftHit = traverse(node.left, ray);
        if (leftHit != null && (closest == null || leftHit.distance < closest.distance)) {
            closest = leftHit;
        }

        Intersection rightHit = traverse(node.right, ray);
        if (rightHit != null && (closest == null || rightHit.distance < closest.distance)) {
            closest = rightHit;
        }

        return closest;
    }

    public void printTree(Node node, int level) {
        if (node == null) return;

        String indent = "  ".repeat(level);
        String color = node.isRed ? "RED" : "BLACK";
        System.out.println(indent + node.objectRef + " (depth=" + level +
                ", color=" + color + ", bbox=" + node.objectRef.getBB());

        printTree(node.left, level + 1);
        printTree(node.right, level + 1);
    }
}