package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;
import vn.uet.oop.arkanoid.model.powerups.*;

import java.util.List;

import static vn.uet.oop.arkanoid.systems.CollisionSystem.checkRectCollision;

public class PowerUpSystem {
    private List<PowerUp> powerUps;
    private Paddle paddle;
    private List<Ball> balls;

    public PowerUpSystem(List<PowerUp> powerUps, Paddle paddle, List<Ball> balls) {
        this.powerUps = powerUps;
        this.paddle = paddle;
        this.balls = balls;
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
                } else if (p instanceof FastBallPowerUp) {
                    for (Ball ball : balls) {
                        p.applyEffect(ball);
                    }
                } else if (p instanceof MultiBallPowerUp) {
                    // truyền nguyên danh sách bóng vào
                    p.applyEffect(balls);
                }

                if (p instanceof ShieldPowerUp) {
                    p.applyEffect(paddle);
                }

                if (p instanceof FireBallPowerUp) {
                    p.applyEffect(balls);
                }


                powerUps.remove(i);
                i--;
            }

        }
    }
}