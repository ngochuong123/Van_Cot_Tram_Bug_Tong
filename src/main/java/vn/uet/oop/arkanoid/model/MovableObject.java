package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;


public abstract class MovableObject implements Collidable {
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
    // cập nhật vị trí
    public void move() {
        x += dx;
        y += dy;
    }

    //code anh Hồng cần thêm

    // Cho phép hệ thống khác chỉnh vị trí
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

}