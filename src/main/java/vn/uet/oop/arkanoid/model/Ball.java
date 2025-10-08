
package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    private final double radius;

    public Ball(double x, double y, double radius, double dx, double dy) {
        super( x,  y, 0,0, dx, dy);
        this.radius = radius;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}

