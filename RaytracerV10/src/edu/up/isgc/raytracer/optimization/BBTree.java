package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.shapes.Object3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * Represents a Bounding Box Tree (BBTree) structure for optimizing ray-object intersection tests.
 * Organizes 3D objects in a binary tree based on their bounding boxes to accelerate ray tracing.
 */
public class BBTree {
    private BBNode root;

    /**
     * Constructs an empty BBTree.
     */
    public BBTree() {
        this.root = null;
    }

    /**
     * Inserts a 3D object into the bounding box tree.
     *
     * @param object The Object3D to be inserted.
     */
    public void insert(Object3D object) {
        if (object == null) return;

        BoundingBox objectBB = object.getBB();
        if (objectBB == null) return;

        if (root == null) {
            root = new BBNode(object);
            return;
        }

        insert(root, object);
    }

    /**
     * Inserts an object into the BBTree starting from the given node.
     *
     * @param node   The current node in the tree.
     * @param object The Object3D to be inserted.
     */
    private void insert(BBNode node, Object3D object) {
        BBNode currentNode = node;
        Stack<BBNode> path = new Stack<>();

        do {
            path.push(currentNode);
            if (currentNode.isLeaf()) {
                currentNode.left = new BBNode(currentNode.object);
                currentNode.right = new BBNode(object);
                currentNode.object = null;

                while (!path.isEmpty()) {
                    path.pop().updateBoundingBox();
                }
                return;
            }

            BoundingBox leftBB = BoundingBox.surroundingBox(currentNode.left.bbox, object.getBB());
            BoundingBox rightBB = BoundingBox.surroundingBox(currentNode.right.bbox, object.getBB());

            double leftCost = leftBB.getSurfaceArea() - currentNode.left.bbox.getSurfaceArea();
            double rightCost = rightBB.getSurfaceArea() - currentNode.right.bbox.getSurfaceArea();

            currentNode = (leftCost < rightCost) ? currentNode.left : currentNode.right;

        } while (true);
    }

    /**
     * Performs a traversal of the BBTree using a ray to find intersections.
     *
     * @param ray The ray used to check for intersections.
     * @return An array of intersections found by the ray.
     */
    public Intersection[] traverse(Ray ray) {
        return traverse(root, ray);
    }

    /**
     * Recursively traverses the BBTree from the given node using the specified ray.
     *
     * @param node The starting node.
     * @param ray  The ray used to check for intersections.
     * @return An array of intersections or null if no intersections were found.
     */
    private Intersection[] traverse(BBNode node, Ray ray) {
        if (node == null || !node.bbox.bbIntersects(ray)) {
            return null;
        }

        Stack<BBNode> stack = new Stack<>();
        stack.push(node);

        Intersection[] finalResult = null;

        while (!stack.isEmpty()) {
            BBNode current = stack.pop();

            if (current.isLeaf()) {
                Intersection[] intersections = current.object.intersect(ray);
                if (intersections != null) {
                    for (Intersection intersection : intersections) {
                        if (intersection != null) {
                            intersection.object = current.object;
                        }
                    }
                    finalResult = combineIntersections(finalResult, intersections);
                }
            } else {
                if (current.left != null && current.left.bbox.bbIntersects(ray)) {
                    stack.push(current.left);
                }
                if (current.right != null && current.right.bbox.bbIntersects(ray)) {
                    stack.push(current.right);
                }
            }
        }

        return finalResult;
    }

    /**
     * Combines two arrays of intersections into one and sorts them by distance.
     *
     * @param hits1 The first array of intersections.
     * @param hits2 The second array of intersections.
     * @return A combined and sorted array of intersections.
     */
    private Intersection[] combineIntersections(Intersection[] hits1, Intersection[] hits2) {
        if (hits1 == null) return hits2;
        if (hits2 == null) return hits1;

        Intersection[] combined = new Intersection[hits1.length + hits2.length];
        System.arraycopy(hits1, 0, combined, 0, hits1.length);
        System.arraycopy(hits2, 0, combined, hits1.length, hits2.length);

        Arrays.sort(combined, Comparator.nullsLast(
                Comparator.comparingDouble(hit -> hit != null ? hit.distance : Double.MAX_VALUE)
        ));

        return combined;
    }

    /**
     * Represents a node in the Bounding Box Tree.
     * Leaf nodes contain objects, and internal nodes contain bounding boxes and children.
     */
    private static class BBNode {
        public BoundingBox bbox;
        public Object3D object;  ///< Present only in leaf nodes.
        public BBNode left;
        public BBNode right;

        /**
         * Constructs a BBNode with a given Object3D.
         *
         * @param object The object to be stored in this node.
         */
        public BBNode(Object3D object) {
            this.object = object;
            this.bbox = object.getBB();
            this.left = null;
            this.right = null;
        }

        /**
         * Checks whether the node is a leaf node.
         *
         * @return True if the node is a leaf, false otherwise.
         */
        public boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * Updates the bounding box of the current node based on its children.
         */
        public void updateBoundingBox() {
            if (isLeaf()) {
                bbox = object.getBB();
            } else {
                bbox = BoundingBox.surroundingBox(left.bbox, right.bbox);
            }
        }
    }
}
