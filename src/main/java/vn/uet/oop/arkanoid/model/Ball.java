package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;
import java.util.LinkedList;
import java.util.List;

public class Ball extends MovableObject {
    private double radius;
    private boolean launched = false;
    private final List<double[]> trail = new LinkedList<>();
    private static final int MAX_TRAIL_SIZE = 10;

    private boolean fireMode = false;
    private boolean outOfScreen = false;

    private double launchAngle = 0;
    private static final double MAX_ANGLE = 80;
    private double angleSpeed = 60;
    private boolean angleIncreasing = true;

    public Ball(double x, double y, double radius, double dx, double dy) {
        super(x, y, radius * 2, radius * 2, dx, dy);
        this.radius = radius;
    }

    public static Ball createBall(double x, double y) {
        return new Ball(x, y, GameConfig.BALL_RADIUS, 0, 0);
    }

    // Getter / Setter
    public boolean isLaunched() { return launched; }
    public void setLaunched(boolean launched) { this.launched = launched; }
    public boolean isFireMode() { return fireMode; }
    public void setFireMode(boolean fireMode) { this.fireMode = fireMode; }
    public boolean isOutOfScreen() { return outOfScreen; }
    public void setOutOfScreen(boolean outOfScreen) { this.outOfScreen = outOfScreen; }
    public double getRadius() { return radius; }

    public void setRadius(double radius) {
        this.radius = radius;
        setWidth(radius * 2);
        setHeight(radius * 2);
    }

    /**
     * stick ball to paddle.
     * @param paddle
     */
    public void stickTo(Paddle paddle) {
        double cx = paddle.getX() + paddle.getWidth() / 2.0;
        setX(cx - getWidth() / 2.0);
        setY(paddle.getY() - getHeight());
    }

    /**
     * launch the ball.
     */
    public void launch() {
        double rad = Math.toRadians(launchAngle);
        setDx(GameConfig.BALL_SPEED * Math.sin(rad));
        setDy(-GameConfig.BALL_SPEED * Math.cos(rad));
        launched = true;
    }

    @Override
    public void update(double deltaTime) {
        if (!isLaunched()) {
            // Tự động xoay góc trước khi bắn
            autoAdjustAngle(deltaTime);
            return;
        }

        setPosition(getX() + getDx() * deltaTime, getY() + getDy() * deltaTime);

        // line fire trail
        trail.add(0, new double[]{getX(), getY()});
        if (trail.size() > MAX_TRAIL_SIZE) trail.remove(trail.size() - 1);
    }

    private void autoAdjustAngle(double deltaTime) {
        double change = angleSpeed * deltaTime;
        if (angleIncreasing) {
            launchAngle += change;
            if (launchAngle >= MAX_ANGLE) {
                launchAngle = MAX_ANGLE;
                angleIncreasing = false;
            }
        } else {
            launchAngle -= change;
            if (launchAngle <= -MAX_ANGLE) {
                launchAngle = -MAX_ANGLE;
                angleIncreasing = true;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        double diameter = this.radius * 2;

        if (!isLaunched()) {
            double cx = getX() + radius;
            double cy = getY() + radius;
            double len = 80;
            double rad = Math.toRadians(launchAngle);

            double tx = cx + len * Math.sin(rad);
            double ty = cy - len * Math.cos(rad);

            gc.setStroke(Color.RED);
            gc.setLineWidth(2.5);
            gc.strokeLine(cx, cy, tx, ty);

            gc.setFill(Color.rgb(255, 80, 80, 0.7));
            gc.fillOval(tx - 5, ty - 5, 10, 10);
        }

        if (fireMode) {
            gc.setFill(Color.ORANGE);
            gc.fillOval(getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4);
            gc.setFill(Color.RED);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
        } else {
            gc.setFill(Color.WHITE);
            gc.fillOval(getX(), getY(), getWidth(), getHeight());
        }

        // pattern trail
        double alpha = 0.9;
        double sizeFactor = 1.0;
        double hueShift = 0;

        for (int i = 0; i < trail.size(); i++) {
            double[] pos = trail.get(i);
            double currentDiameter = diameter * sizeFactor;
            Color flameColor = Color.hsb(40 + hueShift, 1.0, 1.0, alpha);

            gc.setFill(flameColor);
            gc.fillOval(
                    pos[0] + (diameter - currentDiameter) / 2,
                    pos[1] + (diameter - currentDiameter) / 2,
                    currentDiameter, currentDiameter
            );

            alpha *= 0.7;
            sizeFactor *= 0.85;
            hueShift += 20;
        }

        // smoke trail
        for (int i = 1; i < trail.size(); i += 2) {
            double[] pos = trail.get(i);
            double smokeSize = diameter * 0.6 * (1.0 - (double) i / trail.size());
            gc.setFill(new Color(0.8, 0.8, 0.8, 0.2));
            gc.fillOval(pos[0], pos[1], smokeSize, smokeSize);
        }

        gc.setFill(Color.BLACK);
        gc.fillOval(getX(), getY(), diameter, diameter);
        gc.setFill(new Color(1, 1, 0.7, 0.9));
        gc.fillOval(getX() + diameter / 3, getY() + diameter / 3, diameter / 5, diameter / 5);
    }
}
