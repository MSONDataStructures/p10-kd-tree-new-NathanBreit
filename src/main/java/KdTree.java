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

        if (root == null) {
            root = new Node();
            root.p = p;
            root.rect = new RectHV(0, 0, 1, 1);
            size++;
        }
        // used for insertion. carries current location (to be traversed if not null), Point
        // (to be created into a Node and added once at null location), the rectangle
        // corresponding to the current Node (will be updated every recursion
        // by checking x-value of point if vertical or y-value of point if horizontal),
        // and depth to determine whether split is vertical vs. horizontal

        //* odd value for layer == vertical, even value == horizontal
        root = insertHelper(root, p, root.rect, 1);
    }

    private Node insertHelper(Node current, Point2D p, RectHV rect, int layer) {
        if (current == null) {
            Node newNode = new Node();
            newNode.p = p;
            newNode.rect = rect;
            size++;
            return newNode;
        }

        // if vertical
        if (layer % 2 == 1) {
            // if go left
                    // how do I change the rect values?
            current.lb = insertHelper(current.lb, p, rect, layer + 1);
            // if go right
            current.rt = insertHelper(current.rt, p, rect, layer + 1);
        }

        // if horizontal
        if (layer % 2 == 0) {
            // if go down
            current.lb = insertHelper(current.lb, p, rect, layer + 1);
            // if go up
            current.rt = insertHelper(current.rt, p, rect, layer + 1);
        }
    }
    /**
     * Returns true if the set contains point p.
     * @param p the point to be checked for
     * @return true if the set contains point p
     * @throws IllegalArgumentException if p is null
     */
    public boolean contains(Point2D p) {
        // TODO: your code here
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
    }
}
