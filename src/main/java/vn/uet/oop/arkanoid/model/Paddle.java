package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;

import java.util.Timer;


public class Paddle extends MovableObject {
    private boolean hasActiveEffect;
    private boolean hasShield;
    // Hiệu ứng nhún nhẹ

    private final Image paddle;
    private double bounceOffset = 0; // độ lún hiện tại
    private double bounceTimer = 0;  // thời gian nhún

    public Paddle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, 0, 0);
        this.hasActiveEffect = false;
        this.hasShield = false;
        paddle = new Image(getClass().getResourceAsStream("/image/paddle.png"));
    }

    // tạo paddle
    public static Paddle createPaddle() {
        return new Paddle(
                (GameConfig.SCREEN_WIDTH - GameConfig.PADDLE_WIDTH) / 2,
                GameConfig.SCREEN_HEIGHT - 40,
                GameConfig.PADDLE_WIDTH,
                GameConfig.PADDLE_HEIGHT,
                GameConfig.PADDLE_SPEED
        );
    }


    public boolean isHasActiveEffect() { return hasActiveEffect; }
    public void setHasActiveEffect(boolean hasActiveEffect) { this.hasActiveEffect = hasActiveEffect; }

    public boolean isHasShield() { return hasShield; }
    public void setHasShield(boolean hasShield) { this.hasShield = hasShield; }

    // Gọi khi bóng va chạm paddle
    public void onBallHit() {
        bounceTimer = 0.2; // thời gian hiệu ứng
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(paddle, getX(), getY()+bounceOffset, getWidth(), getHeight());
    }

    public void update(double deltaTime, boolean leftPressed, boolean rightPressed) {
        double dx = 0;

        if (leftPressed) dx -= GameConfig.PADDLE_SPEED * deltaTime;
        if (rightPressed) dx += GameConfig.PADDLE_SPEED * deltaTime;

        // Di chuyển ngang
        setX(getX() + dx);

        // Giới hạn trong màn hình
        if (getX() < 0) setX(0);
        if (getX() + getWidth() > GameConfig.SCREEN_WIDTH)
            setX(GameConfig.SCREEN_WIDTH - getWidth());

        //  Hiệu ứng nhún nhẹ
        if (bounceTimer > 0) {
            bounceTimer -= deltaTime;
            // Dùng sin để lún xuống rồi trở lại êm
            double progress = 1 - (bounceTimer / 0.15);
            bounceOffset = Math.sin(progress * Math.PI) * 3; // lún tối đa 3px
            if (bounceTimer <= 0) {
                bounceOffset = 0;
            }
        }
    }

    public void update(double deltaTime) {
        // Không dùng ở đây
    }
}
