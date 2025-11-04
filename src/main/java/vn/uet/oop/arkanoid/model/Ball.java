package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;

import java.util.LinkedList;
import java.util.List;


public class Ball extends MovableObject {
    private double radius;
    private boolean launched = false;
    private final List<double[]> trail = new LinkedList<>();
    private static final int MAX_TRAIL_SIZE = 10;

    private boolean hasFatBallEffect;


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
        super(x, y, radius * 2, radius * 2, dx, dy);
        this.radius = radius;
        this.hasFatBallEffect = false;
        this.launched = false;
    }
    public static Ball createBall(double x, double y) {
        return new Ball(x, y, GameConfig.BALL_RADIUS, 0, 0);
    }


    public boolean isHasFatBallEffect() {
        return hasFatBallEffect;
    }

    public void setHasFatBallEffect(boolean hasActiveEffect) {
        this.hasFatBallEffect = hasActiveEffect;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        setWidth(radius * 2);
        setHeight(radius * 2);
    }

    @Override
    public void update(double deltaTime) {
        // update
        if (!isLaunched()) {
            return;
        }
        setPosition(getX() + getDx() * deltaTime, getY() + getDy() * deltaTime);
        // Thêm vị trí vào danh sách vết
        trail.add(0, new double[]{getX(), getY()});
        if (trail.size() > MAX_TRAIL_SIZE) {
            trail.remove(trail.size() - 1);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        double diameter = this.radius * 2;

        //VẼ VỆT LỬA
        double alpha = 0.9;        // độ trong suốt ban đầu
        double sizeFactor = 1.0;   // kích thước ban đầu
        double hueShift = 0;       // dùng để chuyển dần từ vàng sang đỏ

        for (int i = 0; i < trail.size(); i++) {
            double[] pos = trail.get(i);
            double currentDiameter = diameter * sizeFactor;

            // Tạo màu chuyển từ vàng sang đỏ
            Color flameColor = Color.hsb(40 + hueShift, 1.0, 1.0, alpha);

            gc.setFill(flameColor);
            gc.fillOval(
                    pos[0] + (diameter - currentDiameter) / 2,
                    pos[1] + (diameter - currentDiameter) / 2,
                    currentDiameter,
                    currentDiameter
            );

            // Giảm dần thông số
            alpha *= 0.7;         // mờ dần
            sizeFactor *= 0.85;   // nhỏ dần
            hueShift += 20;       // chuyển từ vàng sang đỏ
        }

        // KHÓI PHÍA SAU
        for (int i = 1; i < trail.size(); i += 2) {
            double[] pos = trail.get(i);
            double smokeSize = diameter * 0.6 * (1.0 - (double) i / trail.size());
            gc.setFill(new Color(0.8, 0.8, 0.8, 0.2)); // xám nhạt
            gc.fillOval(pos[0], pos[1], smokeSize, smokeSize);
        }
        gc.setFill(Color.BLACK);
        gc.fillOval(getX(), getY(), diameter, diameter);

        // highlight
        gc.setFill(new Color(1, 1, 0.7, 0.9));
        gc.fillOval(getX() + diameter / 3, getY() + diameter / 3, diameter / 5, diameter / 5);
    }
}