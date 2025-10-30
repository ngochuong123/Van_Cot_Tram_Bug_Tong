package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.GameObject;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.bricks.*;
import vn.uet.oop.arkanoid.model.powerups.ExpandPaddlePowerUp;
import vn.uet.oop.arkanoid.model.powerups.FastBallPowerUp;
import vn.uet.oop.arkanoid.model.powerups.MultiBallPowerUp;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import vn.uet.oop.arkanoid.model.powerups.ShieldPowerUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * manage physic state
 */
public class PhysicsSystem {

    /*
     * update ball position
     */
    public void updateBall(Ball ball, double deltaTime) {
        if (ball.isLaunched() == false) {
            return;
        }
        ball.setPosition(ball.getX() + ball.getDx() * deltaTime, ball.getY() + ball.getDy() * deltaTime);
    }

    /*
     * check ball statement with wall
     */
    public void bounceBallOnWalls(Ball ball, Paddle paddle) {
        // va cham trai phai
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setDx(Math.abs(ball.getDx()));
        } else if (ball.getX() + ball.getWidth() >= GameConfig.SCREEN_WIDTH) {
            ball.setX(GameConfig.SCREEN_WIDTH - ball.getWidth());
            ball.setDx(-Math.abs(ball.getDx()));
        }

        // va cham tran
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setDy(Math.abs(ball.getDy()));
        }

        if (ball.getY() >= GameConfig.SCREEN_HEIGHT) {
            if (ball.getY() >= GameConfig.SCREEN_HEIGHT) {
                // Nếu paddle có shield thì bóng nảy lại, không reset
                if (paddle.isHasShield()) {
                    ball.setDy(-Math.abs(ball.getDy())); // Bật lên
                    ball.setY(GameConfig.SCREEN_HEIGHT - ball.getHeight() - 5);
                    paddle.setHasShield(false); // Shield chỉ dùng 1 lần
                } else {
                    ball.stickTo(paddle);
                    ball.setLaunched(false);
                }
            }
        }
    }

    /*
     * check ball statement with paddle
     */
    public void bounceBallOnPaddle(Ball ball, Paddle paddle) {
        if (!CollisionSystem.checkBallPaddle(ball, paddle)) {
            return;
        }

        // Tính vị trí chạm tương đối
        double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
        double ballCenter = ball.getX() + ball.getWidth() / 2.0;
        double hitOffset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);
        // hitOffset ∈ [-1, 1] → -1 là rìa trái, 1 là rìa phải

        // Tốc độ tổng hiện tại của bóng (để giữ đà bay đều)
        double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());

        // Giới hạn góc phản xạ: không bay ngang hoàn toàn
        double maxAngle = Math.toRadians(60); // 60 độ lệch tối đa

        // Góc phản xạ (so với trục dọc)
        double angle = hitOffset * maxAngle;

        // Cập nhật vector vận tốc
        ball.setDx(speed * Math.sin(angle));
        ball.setDy(-Math.abs(speed * Math.cos(angle))); // luôn bay lên
    }


    /**
     * check collision on left/right or under/above.
     * 
     * @param ball   ball
     * @param bricks list bricks need to check
     */
    public void bounceBallOnBricks(Ball ball, List<Brick> bricks, List<PowerUp> powerUps) {

        Brick hitBrick = CollisionSystem.getCollidedBrick(ball, bricks);
        if (hitBrick == null) return;

        double ballCenterX = ball.getX() + ball.getWidth() / 2;
        double ballCenterY = ball.getY() + ball.getHeight() / 2;
        double brickCenterX = hitBrick.getX() + hitBrick.getWidth() / 2;
        double brickCenterY = hitBrick.getY() + hitBrick.getHeight() / 2;

        double dx = (ballCenterX - brickCenterX) / hitBrick.getWidth();
        double dy = (ballCenterY - brickCenterY) / hitBrick.getHeight();

        if (Math.abs(dx) > Math.abs(dy)) {
            ball.setDx(-ball.getDx());
            if (dx > 0) {
                ball.setX(hitBrick.getX() + hitBrick.getWidth());
            } else {
                ball.setX(hitBrick.getX() - ball.getWidth());
            }
        } else {
            ball.setDy(-ball.getDy());
            if (dy > 0) {
                ball.setY(hitBrick.getY() + hitBrick.getHeight());
            } else {
                ball.setY(hitBrick.getY() - ball.getHeight());
            }
        }

        hitBrick.takeHit();
        List<Brick> bricksToRemove = new ArrayList<>();

        if (hitBrick.isBroken() && !(hitBrick instanceof RegeneratingBrick)) {
            bricksToRemove.add(hitBrick);


            if (hitBrick instanceof ChainBrick) {
                ChainBrick chain = (ChainBrick) hitBrick;
                int cid = chain.getChainId();

                for (Brick b : bricks) {
                    if (b == hitBrick) continue;
                    if (b instanceof ChainBrick) {
                        ChainBrick other = (ChainBrick) b;
                        if (other.getChainId() == cid) {
                            bricksToRemove.add(other);
                        }
                    }
                }
            }
        }

        if (!bricksToRemove.isEmpty()) {
            Random rand = new Random();

            for (Brick dead : bricksToRemove) {
                double dropChance = 0.3;
                if (rand.nextDouble() < dropChance) {
                    PowerUp newPowerUp = createRandomPowerUpAt(dead);
                    if (newPowerUp != null) {
                        powerUps.add(newPowerUp);
                    }
                }

                bricks.remove(dead);
            }
        }
    }


    private PowerUp createRandomPowerUpAt(Brick brick) {
        Random rand = new Random();
        double typeChance = rand.nextDouble();
        double spawnX = brick.getX() + brick.getWidth() / 2;
        double spawnY = brick.getY() + brick.getHeight() / 2;
        double w = 20;
        double h = 20;
        double fallSpeed = 70;

        if (typeChance < 0.5) {
            return new ExpandPaddlePowerUp(spawnX, spawnY, w, h, fallSpeed);
        } else if (typeChance < 0.75) {
            return new FastBallPowerUp(spawnX, spawnY, w, h, fallSpeed);
        } else if (typeChance < 0.9) {
            return new MultiBallPowerUp(spawnX, spawnY, w, h, fallSpeed);
        } else {
            return new ShieldPowerUp(spawnX, spawnY, w, h, fallSpeed);
        }
    }

}