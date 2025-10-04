package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;

import java.awt.*;

public abstract class GameObject implements Collidable {
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

    @Override
    public Rectangle getBounds() {
        // Trả về khung bao (bounding box) của object — dùng để check collision
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }

    @Override
    public void onCollision(Collidable other) {
        // Mặc định không làm gì cả — class con có thể override
    }
}
