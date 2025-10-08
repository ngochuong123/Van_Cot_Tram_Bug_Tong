package vn.uet.oop.arkanoid.util;

public class Rect {
    private double x;
    private double y;
    private double width;
    private double height;
    public Rect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Check intersection with other Rect.
     *
     * @param other other Rect
     * @return True if intersect, False if not
     */
    public boolean intersects(Rect other) {
        return this.x + this.width > other.x
                && this.x < other.x + other.width
                && this.y + this.height > other.y
                && this.y < other.y + other.height;

    }


}
