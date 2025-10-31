
package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;

public class Ball extends MovableObject {
    private double radius;
    private boolean launched = false;

    private boolean hasActiveEffect;
    private boolean fireMode = false;
    private double fireTimer = 0;


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

    private boolean outOfScreen = false;

    public boolean isOutOfScreen() {
        return outOfScreen;
    }

    public void setOutOfScreen(boolean outOfScreen) {
        this.outOfScreen = outOfScreen;
    }


    public boolean isHasActiveEffect() {
        return hasActiveEffect;
    }

    public void setHasActiveEffect(boolean hasActiveEffect) {
        this.hasActiveEffect = hasActiveEffect;
    }

    public boolean isFireMode() {
        return fireMode;
    }

    public void setFireMode(boolean fireMode) {
        this.fireMode = fireMode;
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
        if (fireMode) {
            // Hiệu ứng bóng lửa
            gc.setFill(Color.ORANGE);
            gc.fillOval(getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4);

            gc.setFill(Color.RED);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
        } else {
            gc.setFill(Color.WHITE);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
        }
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

