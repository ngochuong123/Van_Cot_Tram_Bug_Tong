package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;
import vn.uet.oop.arkanoid.model.powerups.ExpandPaddlePowerUp;
import vn.uet.oop.arkanoid.model.powerups.FastBallPowerUp;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import java.util.List;

import static vn.uet.oop.arkanoid.systems.CollisionSystem.checkRectCollision;

public class PowerUpSystem {
    private List<PowerUp> powerUps;
    private Paddle paddle;
    private Ball ball;

    public PowerUpSystem(List<PowerUp> powerUps, Paddle paddle, Ball ball) {
        this.powerUps = powerUps;
        this.paddle = paddle;
        this.ball = ball;
    }

    /*
     * Cập nhật trạng thái (cho các powerup rơi xuống)
     */
    public void updatePowerUps(double deltaTime) {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            p.update(deltaTime);

            // nếu rơi khỏi màn hình thì xóa
            if (p.getY() > GameConfig.SCREEN_HEIGHT) { // 600 = chiều cao màn hình
                powerUps.remove(i);
                i--;
            }
        }
    }

    /*
     * Kiểm tra và kích hoạt khi paddle chạm powerup
     */
    public void checkAndApply() {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);

            if (checkRectCollision(paddle, p)) {

                // Nếu powerup tác động lên Paddle
                if (p instanceof ExpandPaddlePowerUp) {
                    p.applyEffect(paddle);
                }

                // Nếu powerup tác động lên Ball
                if (p instanceof FastBallPowerUp) {
                    p.applyEffect(ball);
                }
                powerUps.remove(i);
                i--;
            }

        }
    }
}