package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.Ball;

import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.image.Image;

public class FireBallPowerUp extends PowerUp {

    private static final double DURATION = 5.0;

    public FireBallPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
    }

    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof java.util.List<?>) {
            for (Object o : (java.util.List<?>) obj) {
                if (o instanceof Ball) {
                    Ball ball = (Ball) o;
                    ball.setFireMode(true);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ball.setFireMode(false);
                        }
                    }, 5000);
                }
            }
        }
    }

    Image FireBall = new Image("file:src/main/java/vn/uet/oop/arkanoid/resources/image/FireBallPowerUp.png");
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(FireBall, getX(), getY(), getWidth(), getHeight());
    }
}
