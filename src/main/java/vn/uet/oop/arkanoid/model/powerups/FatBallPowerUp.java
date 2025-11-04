package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Ball;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FatBallPowerUp extends PowerUp {

    private final Image bigBall;

    public FatBallPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
        bigBall = new Image(getClass().getResourceAsStream("/image/BigBall.png"));
    }

    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof List<?>) {
            List<Ball> balls = (List<Ball>) obj;
            for (Ball ball : balls) {
                if (ball.isHasFatBallEffect()) {
                    return;
                }
                ball.setHasFatBallEffect(true);
                ball.setRadius(ball.getRadius() * 2);
                ball.setHeight(ball.getRadius() * 2);
                ball.setWidth(ball.getRadius() * 2);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ball.setRadius(GameConfig.BALL_RADIUS);
                        ball.setHeight(GameConfig.BALL_RADIUS * 2);
                        ball.setWidth(GameConfig.BALL_RADIUS * 2);
                        ball.setHasFatBallEffect(false);
                    }
                }, 5000);
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(bigBall, getX(), getY(), getWidth(), getHeight());
    }
}
