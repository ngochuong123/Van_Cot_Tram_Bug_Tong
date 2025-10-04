package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;


public abstract class MovableObject extends GameObject {
    protected double x, y;          // tọa độ
    protected double width, height; // kích thước
    public double dx, dy;        // vận tốc

    public MovableObject(double x, double y, double width, double height, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = dx;
        this.dy = dy;
    }
}