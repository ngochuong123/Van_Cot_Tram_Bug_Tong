
package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;

public class Ball extends MovableObject {
    private double radius;
    private boolean launched = false;

    private boolean hasActiveEffect;

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public void stickTo(Paddle paddle) {
        launched = false;
        double cx = paddle.getX() + paddle.getWidth() / 2.0;
        setX(cx - getWidth() / 2.0);
        setY(paddle.getY() - getHeight());
    }

    public void launch() {
        setDx(GameConfig.BALL_SPEED);
        setDy(-GameConfig.BALL_SPEED);
        launched = true;
    }

    public Ball(double x, double y, double radius, double dx, double dy) {
        super(x, y, radius * 2, radius * 2, dx, dy);
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
        double diameter = this.radius * 2;

        // Glow effect
        gc.setFill(new Color(1, 1, 1, 0.3));
        gc.fillOval(getX() - 5, getY() - 5, diameter + 10, diameter + 10);

        // Main ball trắng
        gc.setFill(Color.BLACK);
        gc.fillOval(getX(), getY(), diameter, diameter);

        // Hiệu ứng sáng mạnh
        gc.setFill(new Color(1, 1, 1, 0.9));
        gc.fillOval(getX() + diameter / 3, getY() + diameter / 3,
                diameter / 5, diameter / 5);

        // Ánh sáng phản chiếu
        gc.setFill(new Color(1, 1, 1, 0.4));
        gc.fillOval(getX() + diameter / 6, getY() + diameter / 6,
                diameter / 2, diameter / 4);
    }
}
