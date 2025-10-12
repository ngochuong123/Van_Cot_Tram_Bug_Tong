
package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;

public class Ball extends MovableObject {
    private final double radius;
    private boolean launched = false;

    public boolean isLaunched() {
        return launched;
    }
    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public void stickTo(Paddle paddle) {
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
        super( x,  y, radius * 2,radius * 2, dx, dy);
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

