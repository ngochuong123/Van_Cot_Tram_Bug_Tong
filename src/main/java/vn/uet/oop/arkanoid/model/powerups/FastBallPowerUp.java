package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.Ball;

import java.util.Timer;
import java.util.TimerTask;

public class FastBallPowerUp extends PowerUp {
    private Image toBall;

    public FastBallPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
        toBall = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/toBall.png");
    }

    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof Ball) {
            Ball ball = (Ball) obj;
            if (ball.isHasActiveEffect()) {
                return;
            }
            ball.setHasActiveEffect(true);
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
                    ball.setHasActiveEffect(false);
                }
            }, 5000);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(toBall, getX(), getY(), getWidth() * 2, getHeight() * 2);
    }
}
