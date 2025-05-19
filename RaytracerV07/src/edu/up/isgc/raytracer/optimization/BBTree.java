package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.shapes.Object3D;

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
        // If current node is a leaf, split it
        if (node.isLeaf()) {
            // Create new children
            node.left = new BBNode(node.object);
            node.right = new BBNode(object);

            // Update this node to be internal
            node.object = null;
            node.updateBoundingBox();
            return;
        }

        // Decide which child to insert into based on which would result
        // in the smallest increase in surface area
        BoundingBox leftBB = BoundingBox.surroundingBox(node.left.bbox, object.getBB());
        BoundingBox rightBB = BoundingBox.surroundingBox(node.right.bbox, object.getBB());

        double leftCost = leftBB.getSurfaceArea() - node.left.bbox.getSurfaceArea();
        double rightCost = rightBB.getSurfaceArea() - node.right.bbox.getSurfaceArea();

        if (leftCost < rightCost) {
            insert(node.left, object);
        } else {
            insert(node.right, object);
        }

        // Update this node's bounding box
        node.updateBoundingBox();
    }

    public Intersection traverse(Ray ray) {
        return traverse(root, ray);
    }

    /*
    private Intersection traverse(BBNode node, Ray ray) {
        if (node == null || !node.bbox.bbIntersects(ray)) {
            return null;
        }

        // If this is a leaf node, check intersection with its object
        if (node.isLeaf()) {
            Intersection[] intersections = node.object.intersect(ray);
            if (intersections == null) return null;

            // Find closest valid intersection
            Intersection closest = null;
            for (Intersection intersection : intersections) {
                if (intersection != null &&
                        (closest == null || intersection.distance < closest.distance)) {
                    // Create new intersection matching your class structure
                    closest = new Intersection(
                            intersection.point,
                            intersection.distance,
                            node.object.getColor(),  // Get color from the object
                            intersection.getNormal(),
                            node.object              // Set the object reference
                    );
                }
            }
            return closest;
        }

        // Otherwise check both children
        Intersection leftHit = traverse(node.left, ray);
        Intersection rightHit = traverse(node.right, ray);

        // Return the closer intersection
        if (leftHit == null) return rightHit;
        if (rightHit == null) return leftHit;
        return leftHit.distance < rightHit.distance ? leftHit : rightHit;
    }

     */

    private Intersection traverse(BBNode node, Ray ray) {
        if (node == null || !node.bbox.bbIntersects(ray)) {
            return null;
        }

        if (node.isLeaf()) {
            Intersection[] intersections = node.object.intersect(ray);
            if (intersections == null) return null;

            Intersection closest = null;
            for (Intersection intersection : intersections) {
                if (intersection != null &&
                        (closest == null || intersection.distance < closest.distance)) {
                    // Use the original intersection but ensure correct object reference
                    intersection.object = node.object;  // Critical line
                    closest = intersection;
                }
            }
            return closest;
        }

        Intersection leftHit = traverse(node.left, ray);
        Intersection rightHit = traverse(node.right, ray);

        if (leftHit == null) return rightHit;
        if (rightHit == null) return leftHit;
        return leftHit.distance < rightHit.distance ? leftHit : rightHit;
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