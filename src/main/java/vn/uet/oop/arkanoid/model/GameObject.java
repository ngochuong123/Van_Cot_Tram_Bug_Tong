package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {
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

    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);

}
