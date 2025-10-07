package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;


public abstract class MovableObject extends GameObject {
    private double dx, dy;        // vận tốc

    public MovableObject(double x, double y, double width, double height, double dx, double dy) {
        super(x,y,width,height);
        this.dx = dx;
        this.dy = dy;
    }
    //Getter
    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
    //Setter

    public void setDx(double dx) {
        this.dx = dx;
    }
    public void setDy(double dy) {
        this.dy = dy;
    }
}