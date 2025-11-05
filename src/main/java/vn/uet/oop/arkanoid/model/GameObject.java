package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;
import vn.uet.oop.arkanoid.model.interfaces.Renderable;

public abstract class GameObject implements Collidable,Renderable {
    private double x;               // Top-left corner x
    private double y;               // Top-left corner y

    private double width;           // Width of the object
    private double height;          // Height of the object

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D((int)x, (int)y, (int)width, (int)height);
    }

    // Getter
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    //Setter
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);

}
