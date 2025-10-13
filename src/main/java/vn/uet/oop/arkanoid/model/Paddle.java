package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;

public class Paddle extends MovableObject {
    private final double speed;

    public Paddle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, 0, 0);
        this.speed = speed;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(getX(),getY(), getWidth(), getHeight());
    }

    public void update(double deltaTime, boolean leftPressed, boolean rightPressed) {
        double dx = 0;

        if (leftPressed) {
            dx -= GameConfig.PADDLE_SPEED * deltaTime;
        }
        if (rightPressed) {
            dx += GameConfig.PADDLE_SPEED * deltaTime;
        }

        // Cập nhật vị trí
        setX(getX() + dx);

        // Giới hạn trong màn hình
        if (getX() < 0) setX(0);
        if (getX() + getWidth() > GameConfig.SCREEN_WIDTH)
            setX(GameConfig.SCREEN_WIDTH - getWidth());
    }

    @Override
    public void update(double deltaTime) {
        // update
    }
}