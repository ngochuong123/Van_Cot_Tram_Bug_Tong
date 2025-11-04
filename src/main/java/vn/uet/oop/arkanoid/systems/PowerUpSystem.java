package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;
import vn.uet.oop.arkanoid.model.powerups.*;
import vn.uet.oop.arkanoid.model.bricks.Brick;
import vn.uet.oop.arkanoid.model.bricks.UnbreakableBrick;
import vn.uet.oop.arkanoid.model.powerups.ExpandPaddlePowerUp;
import vn.uet.oop.arkanoid.model.powerups.FatBallPowerUp;
import vn.uet.oop.arkanoid.model.powerups.MultiBallPowerUp;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import vn.uet.oop.arkanoid.model.powerups.ShieldPowerUp;

import java.util.List;
import java.util.Random;

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

                if (p instanceof ExpandPaddlePowerUp) {
                    p.applyEffect(paddle);
                } else if (p instanceof FatBallPowerUp) {
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

    public void spawnPowerUps(Brick hitBrick) {
        if (hitBrick == null || hitBrick instanceof UnbreakableBrick) return;
        if (!hitBrick.isBroken()) return;

        Random rand = new Random();
            double dropChance = 0.5; // 30% xác suất rơi PowerUp

            if (rand.nextDouble() < dropChance) {
                PowerUp newPowerUp;
                double typeChance = rand.nextDouble();

                if (typeChance < 0.3) {
                    newPowerUp = new ExpandPaddlePowerUp(
                            hitBrick.getX() + hitBrick.getWidth() / 2,
                            hitBrick.getY() + hitBrick.getHeight() / 2,
                            20, 20, 70
                    );
                } else if (typeChance < 0.5) {
                    newPowerUp = new FatBallPowerUp(
                            hitBrick.getX() + hitBrick.getWidth() / 2,
                            hitBrick.getY() + hitBrick.getHeight() / 2,
                            20, 20, 70
                    );
                } else if (typeChance < 0.7) {
                    newPowerUp = new MultiBallPowerUp(
                            hitBrick.getX() + hitBrick.getWidth() / 2,
                            hitBrick.getY() + hitBrick.getHeight() / 2,
                            20, 20, 70
                    );
                } else if (typeChance < 0.9){
                    newPowerUp = new ShieldPowerUp(
                            hitBrick.getX() + hitBrick.getWidth() / 2,
                            hitBrick.getY() + hitBrick.getHeight() / 2,
                            20, 20, 70
                    );
                } else {
                    newPowerUp = new FireBallPowerUp(
                            hitBrick.getX() + hitBrick.getWidth() / 2,
                            hitBrick.getY() + hitBrick.getHeight() / 2,
                            20, 20, 70
                    );
                }

                powerUps.add(newPowerUp);

        }
    }


}