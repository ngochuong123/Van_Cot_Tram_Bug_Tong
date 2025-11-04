package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import vn.uet.oop.arkanoid.model.Ball;
import javafx.scene.image.Image;

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
        if (obj instanceof Ball) {
            Ball ball = (Ball) obj;
            if (ball.isHasFatBallEffect()) {
                return;
            }
            ball.setHasFatBallEffect(true);
            double originalRadius = ball.getRadius();
            ball.setRadius(ball.getRadius() * 2);
            ball.setHeight(ball.getRadius() * 2);
            ball.setWidth(ball.getRadius() * 2);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ball.setRadius(originalRadius);
                    ball.setHeight(originalRadius * 2);
                    ball.setWidth(originalRadius * 2);
                    ball.setHasFatBallEffect(false);
                }
            }, 5000);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(bigBall, getX(), getY(), getWidth(), getHeight());
    }
}
