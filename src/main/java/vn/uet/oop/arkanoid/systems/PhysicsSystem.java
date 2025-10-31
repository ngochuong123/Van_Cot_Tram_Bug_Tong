package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.GameObject;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.bricks.*;
import vn.uet.oop.arkanoid.model.powerups.*;

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
        //va cham trai phai
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setDx(Math.abs(ball.getDx()));
        } else if (ball.getX() + ball.getWidth() >= GameConfig.SCREEN_WIDTH) {
            ball.setX(GameConfig.SCREEN_WIDTH - ball.getWidth());
            ball.setDx(-Math.abs(ball.getDx()));
        }


        //va cham tran
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

        // Tính điểm va chạm trên paddle
        double hitPos = (ball.getX() + ball.getWidth() / 2 - paddle.getX()) / paddle.getWidth() - 0.5;

        // Bóng bật lên
        ball.setDy(-Math.abs(ball.getDy()));

        // Thay đổi hướng X theo vị trí va chạm (lệch trái/phải)
        ball.setDx(hitPos * 400); // 400 là độ mạnh, có thể chỉnh
    }

    /**
     * check collision on left/right or under/above.
     * @param ball ball
     * @param bricks list bricks need to check
     */
    public void bounceBallOnBricks(Ball ball, List<Brick> bricks, List<PowerUp> powerUps) {
        Brick hitBrick = CollisionSystem.getCollidedBrick(ball, bricks);
        if (hitBrick == null) return;

        if (ball.isFireMode()) {
            // 🔥 FireBall mode
            if (hitBrick instanceof UnbreakableBrick) {
                // Không phá được → vẫn nảy lại
                double ballCenterX = ball.getX() + ball.getWidth() / 2;
                double ballCenterY = ball.getY() + ball.getHeight() / 2;
                double brickCenterX = hitBrick.getX() + hitBrick.getWidth() / 2;
                double brickCenterY = hitBrick.getY() + hitBrick.getHeight() / 2;

                double dx = (ballCenterX - brickCenterX) / hitBrick.getWidth();
                double dy = (ballCenterY - brickCenterY) / hitBrick.getHeight();

                if (Math.abs(dx) > Math.abs(dy)) {
                    ball.setDx(-ball.getDx());
                } else {
                    ball.setDy(-ball.getDy());
                }
            } else {
                // 🔥 Phá gạch thường ngay lập tức
                if (hitBrick.isBroken()) {
                    bricks.remove(hitBrick);
                }
                spawnPowerUp(hitBrick, powerUps);
            }
        } else {
            // ⚪ Bình thường: bật lại như vật lý
            double ballCenterX = ball.getX() + ball.getWidth() / 2;
            double ballCenterY = ball.getY() + ball.getHeight() / 2;
            double brickCenterX = hitBrick.getX() + hitBrick.getWidth() / 2;
            double brickCenterY = hitBrick.getY() + hitBrick.getHeight() / 2;

            double dx = (ballCenterX - brickCenterX) / hitBrick.getWidth();
            double dy = (ballCenterY - brickCenterY) / hitBrick.getHeight();

            if (Math.abs(dx) > Math.abs(dy)) {
                ball.setDx(-ball.getDx());
                if (dx > 0)
                    ball.setX(hitBrick.getX() + hitBrick.getWidth());
                else
                    ball.setX(hitBrick.getX() - ball.getWidth());
            } else {
                ball.setDy(-ball.getDy());
                if (dy > 0)
                    ball.setY(hitBrick.getY() + hitBrick.getHeight());
                else
                    ball.setY(hitBrick.getY() - ball.getHeight());
            }

            if (!(hitBrick instanceof UnbreakableBrick)) {
                if (hitBrick.isBroken()) {
                    bricks.remove(hitBrick);
                    spawnPowerUp(hitBrick, powerUps);
                }
            }
        }
    }


    private void spawnPowerUp(Brick hitBrick, List<PowerUp> powerUps) {
        Random rand = new Random();

        double dropChance = 0.3; // Xác suất tổng: 30%
        if (rand.nextDouble() >= dropChance) return;

        // 🎲 Xác suất riêng từng loại (tổng = 1.0)
        double expandChance = 0.35; // Dễ rơi
        double fastChance = 0.25;
        double shieldChance = 0.25;
        double fireChance = 0.15;   // Hiếm hơn

        double roll = rand.nextDouble();
        PowerUp newPowerUp;
        double px = hitBrick.getX() + hitBrick.getWidth() / 2;
        double py = hitBrick.getY() + hitBrick.getHeight() / 2;

        if (roll < expandChance) {
            newPowerUp = new ExpandPaddlePowerUp(px, py, 20, 20, 70);
        } else if (roll < expandChance + fastChance) {
            newPowerUp = new FastBallPowerUp(px, py, 20, 20, 70);
        } else if (roll < expandChance + fastChance + shieldChance) {
            newPowerUp = new ShieldPowerUp(px, py, 20, 20, 70);
        } else {
            newPowerUp = new FireBallPowerUp(px, py, 20, 20, 70);
        }

        powerUps.add(newPowerUp);
    }

}
