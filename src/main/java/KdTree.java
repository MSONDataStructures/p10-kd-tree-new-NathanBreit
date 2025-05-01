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
        if (rect == null) throw new IllegalArgumentException();

        ArrayList<Point2D> returnList = new ArrayList<>();
        rangeHelper(rect, root, 1, returnList);
        return returnList;
    }

    private void rangeHelper(RectHV targetRect, Node current, int layer, ArrayList<Point2D> returnList) {
        //general if statement of success
        if (current == null) {
            return;
        }

        // specific if statement of success. only add point if it is contained in the rectangle target
        if (targetRect.contains(current.p)) {
            returnList.add(current.p);
        }

        //continue recursion based on the fact that any point left or down of the split
        // created by current MUST be either less than or equal to the subsequent x of y
        // value of the split and vice versa.
        // if vertical
        if (layer % 2 == 1) {
            // if go left
            if (targetRect.xmin() <= current.p.x()) {

                // recurse left
                rangeHelper(targetRect, current.lb, layer + 1, returnList);
            }
            // if go right
            if (targetRect.xmax() >= current.p.x()) {

                //recurse right
                rangeHelper(targetRect, current.rt, layer + 1, returnList);
            }
        }

        // if horizontal
        if (layer % 2 == 0) {
            // if go down
            if (targetRect.ymin() <= current.p.y()) {

                // recurse down
                rangeHelper(targetRect, current.lb, layer + 1, returnList);
            }
            // if go up
            if (targetRect.ymax() >= current.p.y()) {

                // recurse with up
                rangeHelper(targetRect, current.rt, layer + 1, returnList);
            }
        }
    }
    /**
     * Returns a nearest neighbor in the set to point p; null if the set is empty.
     * @param p the point to be checked
     * @return a nearest neighbor in the set to point p; null if the set is empty
     * @throws IllegalArgumentException if p is null
     */
    public Point2D nearest(Point2D p) {
        // TODO: your code here
        if (p == null) throw new IllegalArgumentException("argument is null");

        double closestDistance = Double.POSITIVE_INFINITY;
        Point2D nearest = null;

        return nearestHelper(p, root, 1, closestDistance, nearest);
    }

    private Point2D nearestHelper(Point2D p, Node current, int layer, double closestDistance, Point2D nearest) {
        // if statement of success
        if (current == null) return nearest;

        // specific if statement of success. if current is closer than closest
        // found so far then set it equal to closest
        if (current.p.distanceTo(p) < closestDistance) {
            closestDistance = current.p.distanceTo(p);
            nearest = current.p;
        }

        // begin recursion based on the idea that any point in the subtrees MUST be
        // contained inside the rectangle, so if there is no possible point in
        // the rectangle of said subtree closer than nearest we don't have to look there.
        // if vertical
        if (layer % 2 == 1) {
            // if go left
            if (!(current.lb == null) && current.lb.rect.distanceTo(p) < closestDistance) {
                // recurse left
                nearest = nearestHelper(p, current.lb, layer + 1, closestDistance, nearest);
            }
            // if go right
            if (!(current.rt == null) && current.rt.rect.distanceTo(p) < closestDistance) {

                //recurse right
                nearest = nearestHelper(p, current.rt, layer + 1, closestDistance, nearest);
            }
        }

        // if horizontal
        if (layer % 2 == 0) {
            // if go down
            if (!(current.lb == null) && current.lb.rect.distanceTo(p) < closestDistance) {

                // recurse down
                nearest = nearestHelper(p, current.lb, layer + 1, closestDistance, nearest);
            }
            // if go up
            if (!(current.rt == null) && current.rt.rect.distanceTo(p) < closestDistance) {

                // recurse with up
                nearest = nearestHelper(p, current.rt, layer + 1, closestDistance, nearest);
            }
        }

        return nearest;
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

        // contains test cases
        kdTree.Print(kdTree.root);
        // true
        System.out.println(kdTree.contains(new Point2D(0.9, 0.6)));
        // false
        System.out.println(kdTree.contains(new Point2D(0.5, 0.5)));
        // true
        System.out.println(kdTree.contains(new Point2D(0.5, 0.4)));
        // true
        System.out.println(kdTree.contains(new Point2D(0.7, 0.2)));


        // Range test cases
        RectHV fullRange = new RectHV(0.0, 0.0, 1.0, 1.0);
        System.out.println("range test for all points");
        for (Point2D p : kdTree.range(fullRange)) {
            System.out.println(p);
        }
        RectHV lowerLeft = new RectHV(0.0, 0.0, 0.5, 0.5);
        System.out.println("range test for points (.5, .4) and (.2, .3)");
        for (Point2D p : kdTree.range(lowerLeft)) {
            System.out.println(p);
        }
        RectHV emptyRange = new RectHV(0.0, 0.8, 0.2, 1.0);
        System.out.println("range test for no points");
        for (Point2D p : kdTree.range(emptyRange)) {
            System.out.println(p);
        }
        RectHV onePoint = new RectHV(0.7, 0.2, 0.7, 0.2);
        System.out.println("range test for on border point (.7, .2)");
        for (Point2D p : kdTree.range(onePoint)) {
            System.out.println(p);
        }

        // nearest test cases
        System.out.println("Nearest Tests");
        System.out.println(kdTree.nearest(new Point2D(.2, .7)));
        // answer = (.4, .7)
        System.out.println(kdTree.nearest(new Point2D(.9, .9)));
        // answer = (.9, .6)
        System.out.println(kdTree.nearest(new Point2D(.5, .4)));
        // answer = (.5, .4)
        System.out.println(kdTree.nearest(new Point2D(0.4, .4)));
        // answer (.5, .4)
        System.out.println(kdTree.nearest(new Point2D(0.7, .1)));
        // answer (.7, .2)

    }

    private void Print(Node node) {
        if (node == null) return;
        System.out.println(node.p);
        Print(node.lb);
        Print(node.rt);
    }
}
// we gucci. thanks Mr. Young for the class.