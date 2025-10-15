
package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    private double radius;
    private boolean hasActiveEffect;
    public Ball(double x, double y, double radius, double dx, double dy) {
        super( x,  y, radius * 2,radius * 2, dx, dy);
        this.radius = radius;
        this.hasActiveEffect = false;
    }

    public boolean isHasActiveEffect() {
        return hasActiveEffect;
    }

    public void setHasActiveEffect(boolean hasActiveEffect) {
        this.hasActiveEffect = hasActiveEffect;
    }

    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void update(double deltaTime) {
        // update
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}

