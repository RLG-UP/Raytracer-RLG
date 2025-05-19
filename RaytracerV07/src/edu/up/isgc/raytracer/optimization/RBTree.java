package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.shapes.Object3D;

public class RBTree {
    public Node root;

    public RBTree() {
        this.root = null;
    }

    // Left rotation
    public void rotateLeft(Node node) {
        if (node == null || node.right == null) return;

        Node y = node.right;
        node.right = y.left;
        if (y.left != null) {
            y.left.parent = node;
        }
        y.parent = node.parent;

        if (node.parent == null) {
            root = y;
        } else if (node == node.parent.left) {
            node.parent.left = y;
        } else {
            node.parent.right = y;
        }

        y.left = node;
        node.parent = y;
    }

    // Right rotation
    public void rotateRight(Node node) {
        if (node == null || node.left == null) return;

        Node y = node.left;
        node.left = y.right;
        if (y.right != null) {
            y.right.parent = node;
        }
        y.parent = node.parent;

        if (node.parent == null) {
            root = y;
        } else if (node == node.parent.right) {
            node.parent.right = y;
        } else {
            node.parent.left = y;
        }

        y.right = node;
        node.parent = y;
    }

    // Fix insertion to maintain red-black properties
    public void fixInsertion(Node node) {
        while (node != root && node.parent.isRed) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle != null && uncle.isRed) {
                    node.parent.paintBlack();
                    uncle.paintBlack();
                    node.parent.parent.paintRed();
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.paintBlack();
                    node.parent.parent.paintRed();
                    rotateRight(node.parent.parent);
                }
            } else {
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

    // Insert a new node by distance value
    public Node insert(double distance, Object3D object) {
        if (root == null) {
            root = new Node(distance, object);
            root.paintBlack();
            return root;
        } else {
            return insertRecursive(root, distance, object);
        }
    }

    private Node insertRecursive(Node current, double distance, Object3D object) {
        if (distance == current.distance) {
            return current; // already exists, no duplicates
        } else if (distance < current.distance) {
            if (current.left == null) {
                Node newNode = new Node(distance, object);
                newNode.parent = current;
                current.left = newNode;
                fixInsertion(newNode);
                return newNode;
            } else {
                return insertRecursive(current.left, distance, object);
            }
        } else {
            if (current.right == null) {
                Node newNode = new Node(distance, object);
                newNode.parent = current;
                current.right = newNode;
                fixInsertion(newNode);
                return newNode;
            } else {
                return insertRecursive(current.right, distance, object);
            }
        }
    }
}
