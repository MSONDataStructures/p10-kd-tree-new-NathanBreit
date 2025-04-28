/**
 * Description: Represents a set of points in the unit square
 * (all points have x- and y-coordinates between 0 and 1)
 * using <code>algs4.Point2D</code> to represent a point,
 * <code>algs4.RectHV</code> to represent a rectangle,
 * a red-black BST (used in <code>algs4.SET</code> or <code>java.util.TreeSet</code>)
 * to support range search (find all the points contained in a query rectangle)
 * and nearest-neighbor search (find a closest point to a query point).
 * <p>
 * This is the efficient implementation using a 2D-Tree.
 */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.TreeSet;

public class KdTree {

    /**
     * Node inner class for your KD-Tree implementation,
     * from the FAQ (feel free to modify).
     */
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }

    /**
     * An integer for the size of the canvas.
     */
    private static final int CANVAS_SIZE = 640;

    /**
     * A double for the size of the pen radius.
     */
    private static final double PEN_RADIUS = 0.01;

    /**
     * Constructs an empty set of points in the unit square.
     */
    //simple fields to keep as I go
    private int size;
    private Node root;

    public KdTree() {
        // TODO: your code here
        // Make Tree Empty to Start
        size = 0;
        root = null;

    }

    /**
     * Returns true if the set is empty.
     * @return whether the set is empty
     */
    public boolean isEmpty() {
        // TODO: your code here
        return size == 0;
    }

    /**
     * Returns the number of points in the set.
     * @return the number of points in the set
     */
    public int size() {
        // TODO: your code here
        return size;
    }

    /**
     * Adds the point to the set (if not already in the set).
     * @param p the point to be inserted
     * @throws IllegalArgumentException if p is null
     */
    public void insert(Point2D p) {
        // TODO: your code here
        if (p == null) throw new IllegalArgumentException();

        // deals with issue of having root be null in the call
        if (root == null) {
            root = new Node();
            root.p = p;
            root.rect = new RectHV(0, 0, 1, 1);
            size++;
        } else {
            // used for insertion. carries current location (to be traversed if not null), Point
            // (to be created into a Node and added once at null location), the rectangle
            // corresponding to the current Node (will be updated every recursion
            // by checking x-value of point if vertical or y-value of point if horizontal),
            // and depth to determine whether split is vertical vs. horizontal

            //* odd value for layer == vertical, even value == horizontal
            root = insertHelper(root, p, 0, 0, 1, 1, 1);
        }
    }

    private Node insertHelper(Node current, Point2D p, double minX, double minY, double maxX, double maxY, int layer) {
        if (current == null) {
            Node newNode = new Node();
            newNode.p = p;
            newNode.rect = new RectHV(minX, minY, maxX, maxY);
            size++;
            return newNode;
        }

        // if vertical
        if (layer % 2 == 1) {
            // if go left
            if (p.x() < current.p.x()) {

                // recurse with new rectangle
                current.lb = insertHelper(current.lb, p, minX, minY, current.p.x(), maxY, layer + 1);
            }
            // if go right
            if (p.x() >= current.p.x()) {

                //recurse with new rectangle
                current.rt = insertHelper(current.rt, p, current.p.x(), minY, maxX, maxY, layer + 1);
            }
        }

        // if horizontal
        if (layer % 2 == 0) {
            // if go down
            if (p.y() < current.p.y()) {

                // recurse with new rectangle
                current.lb = insertHelper(current.lb, p, minX, minY, maxX, current.p.y(), layer + 1);
            }
            // if go up
            if (p.y() >= current.p.y()) {
                // recurse with new rectangle
                current.rt = insertHelper(current.rt, p, minX, current.p.y(), maxX, maxY, layer + 1);
            }
        }
        return current;
    }
    /**
     * Returns true if the set contains point p.
     * @param p the point to be checked for
     * @return true if the set contains point p
     * @throws IllegalArgumentException if p is null
     */
    public boolean contains(Point2D p) {
        // TODO: your code here
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return false;

        return containsHelper(root, p, 1);
    }

    private boolean containsHelper(Node current, Point2D p, int layer) {
        //fixed null pointer exception. It was an educated guess. yay me.
        if (current == null) return false;

        // what I like to call the if statement of success for recursion
        if (p.equals(current.p)) {
            return true;
        }

        // code is effectively the same as in insert method
        if (layer % 2 == 1) {
            if (p.x() < current.p.x()) {
                return containsHelper(current.lb, p, layer + 1);
            } else {
                return containsHelper(current.rt, p, layer + 1);
            }
        }
        if (layer % 2 == 0) {
            if (p.y() < current.p.y()) {
                return containsHelper(current.lb, p, layer + 1);
            } else {
                return containsHelper(current.rt, p, layer + 1);
            }
        }
        return false;
    }
    /**
     * Draws all points to standard draw.
     */
    public void draw() {
        // TODO: your code here - feel free to modify what is here
        StdDraw.setCanvasSize(CANVAS_SIZE, CANVAS_SIZE);
        StdDraw.setPenRadius(PEN_RADIUS);
    }

    /**
     * Returns all the points that are inside the rectangle.
     * @param rect the rectangle
     * @return all the points that are inside the rectangle
     * @throws IllegalArgumentException if rect is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        // TODO: your code here
        return new ArrayList<>();
    }

    /**
     * Returns a nearest neighbor in the set to point p; null if the set is empty.
     * @param p the point to be checked
     * @return a nearest neighbor in the set to point p; null if the set is empty
     * @throws IllegalArgumentException if p is null
     */
    public Point2D nearest(Point2D p) {
        // TODO: your code here
        return new Point2D(0.0, 0.0);
    }

    /**
     * Optional method for your testing.
     * @param args the arguments
     */
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));

        kdTree.Print(kdTree.root);
        System.out.println(kdTree.contains(new Point2D(0.9, 0.6)));
        System.out.println(kdTree.contains(new Point2D(0.5, 0.5)));
        System.out.println(kdTree.contains(new Point2D(0.5, 0.4)));
        System.out.println(kdTree.contains(new Point2D(0.7, 0.2)));
    }

    private void Print(Node node) {
        if (node == null) return;
        System.out.println(node.p);
        Print(node.lb);
        Print(node.rt);
    }
}
