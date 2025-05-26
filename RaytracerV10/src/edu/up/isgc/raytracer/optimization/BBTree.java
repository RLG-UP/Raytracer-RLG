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

public class BBTree {
    private BBNode root;

    public BBTree() {
        this.root = null;
    }

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



    private void insert(BBNode node, Object3D object) {
        BBNode currentNode = node;
        Stack<BBNode> path = new Stack<>();
        // If current node is a leaf, split it
        do{
            path.push(currentNode);
            if (currentNode.isLeaf()) {
                 // Create new children
                 currentNode.left = new BBNode(currentNode.object);
                 currentNode.right = new BBNode(object);

                 // Update this node to be internal
                 currentNode.object = null;
                 //currentNode.updateBoundingBox();
                 while(!path.isEmpty()){
                     path.pop().updateBoundingBox();
                 }
                 return;
             }
             // Decide which child to insert into based on which would result
             // in the smallest increase in surface area
             BoundingBox leftBB = BoundingBox.surroundingBox(currentNode.left.bbox, object.getBB());
             BoundingBox rightBB = BoundingBox.surroundingBox(currentNode.right.bbox, object.getBB());

             double leftCost = leftBB.getSurfaceArea() - currentNode.left.bbox.getSurfaceArea();
             double rightCost = rightBB.getSurfaceArea() - currentNode.right.bbox.getSurfaceArea();

             if (leftCost < rightCost) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
             // Update this node's bounding box
             //currentNode.updateBoundingBox();

        }while(true);
    }


    public Intersection[] traverse(Ray ray) {
        return traverse(root, ray);
    }

    private Intersection[] traverse(BBNode node, Ray ray) {
        if (node == null || !node.bbox.bbIntersects(ray)) {
            return null;
        }

        // Stack for iterative traversal
        Stack<BBNode> stack = new Stack<>();
        stack.push(node);

        Intersection[] finalResult = null;

        while (!stack.isEmpty()) {
            BBNode current = stack.pop();

            if (current.isLeaf()) {
                // Process leaf node
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
                // Push left first, so right gets processed first (stack is LIFO)
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



    private Intersection[] combineIntersections(Intersection[] hits1, Intersection[] hits2) {
        if (hits1 == null) return hits2;
        if (hits2 == null) return hits1;

        Intersection[] combined = new Intersection[hits1.length + hits2.length];
        System.arraycopy(hits1, 0, combined, 0, hits1.length);
        System.arraycopy(hits2, 0, combined, hits1.length, hits2.length);

        // Sort by distance to maintain order
        Arrays.sort(combined, Comparator.nullsLast(
                Comparator.comparingDouble(hit -> hit != null ? hit.distance : Double.MAX_VALUE)
        ));

        return combined;
    }

    private static class BBNode {
        public BoundingBox bbox;
        public Object3D object;  // Only for leaf nodes
        public BBNode left;
        public BBNode right;

        public BBNode(Object3D object) {
            this.object = object;
            this.bbox = object.getBB();
            this.left = null;
            this.right = null;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public void updateBoundingBox() {
            if (isLeaf()) {
                bbox = object.getBB();
            } else {
                bbox = BoundingBox.surroundingBox(left.bbox, right.bbox);
            }
        }
    }
}