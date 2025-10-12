package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.GameObject;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.bricks.*;
import java.util.List;

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
    public void bounceBallOnBricks(Ball ball, List<Brick> bricks) {
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
        }
    }
}