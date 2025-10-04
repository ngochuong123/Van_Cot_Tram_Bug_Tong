package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {
    private double x;
    private double y;
    private double width;
    private double height;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getter
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    //Setter
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);
}

