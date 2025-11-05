package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Ball;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * FireBallPowerUp class.
 * set ball to fire mode for a duration.
 */
public class FireBallPowerUp extends PowerUp {

    private final Image fireBallImage;

    // --- Biến static quản lý hiệu ứng toàn cục ---
    private static boolean isFireBallActive = false;
    private static Timer fireBallTimer;

    public FireBallPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
        fireBallImage = new Image(getClass().getResourceAsStream("/image/FireBallPowerUp.png"));
    }

    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof List<?>) {
            List<Ball> balls = (List<Ball>) obj;

            if (isFireBallActive) {
                if (fireBallTimer != null) {
                    fireBallTimer.cancel();
                }
                fireBallTimer = new Timer();
                fireBallTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        resetFireBall(balls);
                    }
                }, GameConfig.DURATION_POWERUP);
                return;
            }

            isFireBallActive = true;
            for (Ball ball : balls) {
                ball.setFireMode(true);
            }
            fireBallTimer = new Timer();
            fireBallTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    resetFireBall(balls);
                }
            }, GameConfig.DURATION_POWERUP);
        }
    }

    private void resetFireBall(List<Ball> balls) {
        for (Ball ball : balls) {
            ball.setFireMode(false);
        }
        isFireBallActive = false;
    }
    public static boolean isFireBallActive() {
        return isFireBallActive;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(fireBallImage, getX(), getY(), getWidth(), getHeight());
    }
}
