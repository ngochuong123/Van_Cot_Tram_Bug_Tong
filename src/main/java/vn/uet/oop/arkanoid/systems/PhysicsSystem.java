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
        ball.setPosition(ball.getX() + ball.dx * deltaTime, ball.getY() + ball.dy * deltaTime);
    }

    /*
     * check ball statement with wall
     */
    public void bounceBallOnWalls(Ball ball) {
        // xu ly va cham tuong trai phai
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= GameConfig.SCREEN_WIDTH) {
            ball.dx = -ball.dx;
        }

        //va cham tran
        if (ball.getY() <= 0) {
            ball.dy = -ball.dy;
        }
    }

    /*
     * check ball statement with paddle
     */
    public void bounceBallOnPaddle(Ball ball, Paddle paddle) {
        if (!CollisionSystem.checkBallPaddle(ball, paddle)) {
            return;
        }

        ball.dy = -Math.abs(ball.dy);
    }

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
                ball.dx = -ball.dx;
            } else {
                ball.dy = -ball.dy;
            }
        }
    }
}