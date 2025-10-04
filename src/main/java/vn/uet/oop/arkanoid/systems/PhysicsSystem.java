package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.GameObject;
import vn.uet.oop.arkanoid.model.Paddle;

/*
 * manage physic state
 */
public class PhysicsSystem {

    /*
     * update ball position
     */
    public void updateBall(Ball ball) {
        ball.setPosition(ball.getX() + ball.dx, ball.getY() + ball.dy);
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
        if (!ball.getBounds().intersects(paddle.getBounds())) {
            return;
        }

        ball.dy = -Math.abs(ball.dy);
    }
}