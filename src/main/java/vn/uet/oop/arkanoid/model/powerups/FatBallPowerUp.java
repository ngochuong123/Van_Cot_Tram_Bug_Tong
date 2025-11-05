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
    private static boolean isFatBallActive = false; // hiệu ứng toàn cục
    private static Timer fatBallTimer;              // bộ đếm thời gian toàn cục

    public FatBallPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
        bigBall = new Image(getClass().getResourceAsStream("/image/BigBall.png"));
    }

    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof List<?>) {
            List<Ball> balls = (List<Ball>) obj;

            // Nếu đã có hiệu ứng FatBall đang hoạt động → chỉ reset thời gian
            if (isFatBallActive) {
                if (fatBallTimer != null) fatBallTimer.cancel();
                fatBallTimer = new Timer();
                fatBallTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        resetFatBall(balls);
                    }
                }, 5000);
                return;
            }

            isFatBallActive = true;

            for (Ball ball : balls) {
                ball.setRadius(ball.getRadius() * 2);
                ball.setHeight(ball.getRadius() * 2);
                ball.setWidth(ball.getRadius() * 2);
            }

            fatBallTimer = new Timer();
            fatBallTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    resetFatBall(balls);
                }
            }, 5000);
        }
    }

    private void resetFatBall(List<Ball> balls) {
        for (Ball ball : balls) {
            ball.setRadius(GameConfig.BALL_RADIUS);
            ball.setHeight(GameConfig.BALL_RADIUS * 2);
            ball.setWidth(GameConfig.BALL_RADIUS * 2);
        }
        isFatBallActive = false;
    }

    public static boolean isFatBallActive() {
        return isFatBallActive;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(bigBall, getX(), getY(), getWidth(), getHeight());
    }
}
