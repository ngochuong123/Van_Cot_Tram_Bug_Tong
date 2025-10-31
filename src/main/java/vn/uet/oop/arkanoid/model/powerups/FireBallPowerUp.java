package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.Ball;

import java.util.Timer;
import java.util.TimerTask;

public class FireBallPowerUp extends PowerUp {

    private static final double DURATION = 5.0;

    public FireBallPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
    }

    @Override
    public void applyEffect(Object obj) {
        Ball ball = (Ball) obj;
        ball.setFireMode(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ball.setFireMode(false);
            }
        }, 5000);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}
