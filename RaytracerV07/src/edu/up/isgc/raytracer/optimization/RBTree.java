package edu.up.isgc.raytracer.optimization;

import edu.up.isgc.raytracer.Intersection;
import edu.up.isgc.raytracer.Ray;
import edu.up.isgc.raytracer.Vector3D;
import edu.up.isgc.raytracer.shapes.Object3D;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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

    /*
    // Insert a new node by distance value
    public Node insert(Object3D object) {
        if (root == null) {
            BoundingBox bbox = object.getBB();
            Vector3D centroid = bbox.getMin().add(bbox.getMax()).scale((double) 1 /2);

            root = new Node(centroid.x, object);
            root.paintBlack();
            return root;
        } else {
            return insertRecursive(root, object, 0);
        }
    }

     */

    /*
    private Node insertRecursive(Node current, double distance, Object3D object) {
        if (distance == current.centroidValue) {
            return current; // already exists, no duplicates
        } else if (distance < current.centroidValue) {
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

     */
    /*
    private Node insertRecursive(Node current, Object3D object, int depth) {
        // Compute centroid of the object's bounding box
        BoundingBox bbox = object.getBB();
        Vector3D centroid = bbox.getMin().add(bbox.getMax()).scale((double) 1 /2);

        // Pick axis based on depth (0 = x, 1 = y, 2 = z)
        double valueToCompare = switch (depth % 3) {
            case 0 -> centroid.x;
            case 1 -> centroid.y;
            case 2 -> centroid.z;
            default -> centroid.x;
        };

        if (current == null) {
            Node newNode = new Node(valueToCompare, object); // centroidValue will be stored in constructor
            //fixInsertion(newNode);
            return newNode;
        }

        if (valueToCompare < current.centroidValue) {
            if (current.left == null) {
                Node newNode = new Node(valueToCompare, object);
                newNode.parent = current;
                current.left = newNode;
                //fixInsertion(newNode);
                return newNode;
            } else {
                return insertRecursive(current.left, object, depth + 1);
            }
        } else {
            if (current.right == null) {
                Node newNode = new Node(valueToCompare, object);
                newNode.parent = current;
                current.right = newNode;
                //fixInsertion(newNode);
                return newNode;
            } else {
                return insertRecursive(current.right, object, depth + 1);
            }
        }
    }

     */

    private void insert(Node node, Object3D obj, int depth) {
        BoundingBox bbox = obj.getBB();
        Vector3D centroid = bbox.getMin().add(bbox.getMax()).scale((double) 1 /2);

        // Pick axis based on depth (0 = x, 1 = y, 2 = z)
        double valueToCompare = switch (depth % 3) {
            case 0 -> centroid.x;
            case 1 -> centroid.y;
            case 2 -> centroid.z;
            default -> centroid.x;
        };

        if(valueToCompare < node.centroidValue) {
            if(node.left == null){
                node.left = new Node(valueToCompare, obj);
                return;
            }
            insert(node.left, obj, depth + 1);
        }
        else if(valueToCompare > node.centroidValue) {
            if(node.right == null){
                node.right = new Node(valueToCompare, obj);
                return;
            }
            insert(node.right, obj, depth + 1);
        }
    }

    public void insert(Object3D obj, int depth){
        if(this.root == null){
            BoundingBox bbox = obj.getBB();
            Vector3D centroid = bbox.getMin().add(bbox.getMax()).scale((double) 1 /2);

            // Pick axis based on depth (0 = x, 1 = y, 2 = z)
            double valueToCompare = switch (depth % 3) {
                case 0 -> centroid.x;
                case 1 -> centroid.y;
                case 2 -> centroid.z;
                default -> centroid.x;
            };
            this.root = new Node(valueToCompare, obj);
        }
        insert(this.root, obj, depth);
    }

    /*
    public Intersection[] traverse(Node node, Ray ray) {
        if (node == null) return null;

        if (node.objectRef.getBB().bbIntersects(ray)) {
            if (node.left == null && node.right == null) {
                return node.objectRef.intersect(ray);
            } else {
                traverse(node.left, ray);
                traverse(node.right, ray);
            }
        }
        return null;
    }

     */

    /*
    public void traverse(Node node, Ray ray, List<Intersection> intersections) {
        if (node == null) return;

        if (node.objectRef.getBB().bbIntersects(ray)) {
            Intersection[] nodeIntersections = node.objectRef.intersect(ray);
            if (nodeIntersections != null) {
                Collections.addAll(intersections, nodeIntersections);
            }

            // Continue traversal
            traverse(node.left, ray, intersections);
            traverse(node.right, ray, intersections);
        }
    }

     */

    //traverse before refactor
    public void traverse(Node node, Ray ray, List<Intersection> intersections) {
        if (node == null) return;

        //System.out.println("Testing node: " + node.objectRef);  // Debug print

        if (node.objectRef.getBB().bbIntersects(ray)) {
            //System.out.println("  Bounding box hit!");  // Debug print

            Intersection[] nodeIntersections = node.objectRef.intersect(ray);
            if (nodeIntersections != null && nodeIntersections[0] != null && nodeIntersections[0].object != null) {
                System.out.println("  Found " + nodeIntersections.length + " intersections");  // Debug print
                Collections.addAll(intersections, nodeIntersections);
            }

            traverse(node.left, ray, intersections);
            traverse(node.right, ray, intersections);
        } else {
            //System.out.println("  Bounding box MISS");  // Debug print
        }
    }


    /*
    public void traverse(Node node, Ray ray, List<Intersection> intersections) {
        if (node == null) return;

        // Debug print node info
        //System.out.println("Testing node - Object: " + node.objectRef.getClass().getSimpleName() + " | BBox: " + node.objectRef.getBB());

        if (node.objectRef.getBB().bbIntersects(ray)) {
            //System.out.println("  ✓ BBox HIT");

            Intersection[] nodeIntersections = node.objectRef.intersect(ray);
            //System.out.println("  Found " + (nodeIntersections != null ? nodeIntersections.length : 0) + " intersections");

            if (nodeIntersections != null) {
                for (Intersection i : nodeIntersections) {
                    if (i != null) {
                        System.out.println("    Intersection at distance: " + i.distance);
                        intersections.add(i);
                    }
                }
            }

            traverse(node.left, ray, intersections);
            traverse(node.right, ray, intersections);
        } else {
            //System.out.println("  ✗ BBox MISS");
            // Debug why miss occurs
            Vector3D rayOrigin = ray.origin;
            Vector3D rayDir = ray.direction;
            BoundingBox bbox = node.objectRef.getBB();
            //System.out.println("  Ray: O=" + rayOrigin + " D=" + rayDir);
            //System.out.println("  BBox Min=" + bbox.getMin() + " Max=" + bbox.getMax());
        }
    }

     */


    public void printTree(Node node, int level) {
        if (node == null || node.objectRef == null) return;
        System.out.println(" ".repeat(level*2) +
                node.objectRef + " (depth=" + level%3 +
                ", color=" + (node.isRed ? "RED" : "BLACK") +
                ", bbox=" + node.objectRef.getBB());

        printTree(node.left, level+1);
        printTree(node.right, level+1);
    }

}
