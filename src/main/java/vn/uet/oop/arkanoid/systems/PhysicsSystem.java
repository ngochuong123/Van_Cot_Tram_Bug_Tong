package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.GameObject;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.bricks.*;
import vn.uet.oop.arkanoid.model.powerups.ExpandPaddlePowerUp;
import vn.uet.oop.arkanoid.model.powerups.FastBallPowerUp;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;

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
        // xu ly va cham tuong trai phai
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= GameConfig.SCREEN_WIDTH) {
            ball.setDx(-ball.getDx());
        }

        //va cham tran
        if (ball.getY() <= 0) {
            ball.setDy(-ball.getDy());
        }

        if (ball.getY() >= GameConfig.SCREEN_HEIGHT) {
            ball.stickTo(paddle);
            ball.setLaunched(false);
        }
    }

    /*
     * check ball statement with paddle
     */
    public void bounceBallOnPaddle(Ball ball, Paddle paddle) {
        if (!CollisionSystem.checkBallPaddle(ball, paddle)) {
            return;
        }

        ball.setDy(-Math.abs(ball.getDy()));
    }

    /**
     * check collision on left/right or under/above.
     * @param ball ball
     * @param bricks list bricks need to check
     */
    public void bounceBallOnBricks(Ball ball, List<Brick> bricks, List<PowerUp> powerUps) {
        Brick hitBrick = CollisionSystem.getCollidedBrick(ball, bricks);
        if (hitBrick != null) {
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

            if (!(hitBrick instanceof UnbreakableBrick)) {
                bricks.remove(hitBrick);
            }

            Random rand = new Random();

            // Xác suất rơi PowerUp
            double dropChance = 0.3;
            if (rand.nextDouble() < dropChance) {
                // Nếu rơi ra PowerUp thì chọn loại
                double typeChance = rand.nextDouble();
                PowerUp newPowerUp;

                if (typeChance < 0.6) {
                    newPowerUp = new ExpandPaddlePowerUp(
                            hitBrick.getX() + hitBrick.getWidth() / 2,
                            hitBrick.getY() + hitBrick.getHeight() / 2,
                            20, 20, 70
                    );
                } else {
                    newPowerUp = new FastBallPowerUp(
                            hitBrick.getX() + hitBrick.getWidth() / 2,
                            hitBrick.getY() + hitBrick.getHeight() / 2,
                            20, 20, 70
                    );
                }

                powerUps.add(newPowerUp);
            }
        }
    }
}