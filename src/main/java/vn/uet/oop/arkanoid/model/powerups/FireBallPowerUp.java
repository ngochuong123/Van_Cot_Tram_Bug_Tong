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
 * Làm bóng có thể xuyên gạch trong một khoảng thời gian.
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

            // Nếu đã có hiệu ứng FireBall đang hoạt động → chỉ reset thời gian
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

            // Bắt đầu hiệu ứng mới
            isFireBallActive = true;

            // Bật chế độ fire cho tất cả bóng hiện tại
            for (Ball ball : balls) {
                ball.setFireMode(true);
            }

            // Hẹn giờ tắt hiệu ứng
            fireBallTimer = new Timer();
            fireBallTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    resetFireBall(balls);
                }
            }, GameConfig.DURATION_POWERUP);
        }
    }

    // Hết hiệu ứng → tắt fire mode
    private void resetFireBall(List<Ball> balls) {
        for (Ball ball : balls) {
            ball.setFireMode(false);
        }
        isFireBallActive = false;
    }

    // Cho phép lớp khác (như MultiBallPowerUp) kiểm tra hiệu ứng đang bật không
    public static boolean isFireBallActive() {
        return isFireBallActive;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(fireBallImage, getX(), getY(), getWidth(), getHeight());
    }
}
