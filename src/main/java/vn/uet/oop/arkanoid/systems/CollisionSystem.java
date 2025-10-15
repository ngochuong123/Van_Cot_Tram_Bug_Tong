package vn.uet.oop.arkanoid.systems;

import javafx.geometry.Rectangle2D;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.bricks.Brick;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;
import vn.uet.oop.arkanoid.model.Paddle;
import java.util.List;

/*
 * check collision system
 */
public class CollisionSystem {

    /*
     * check if 2 rectango collision
     */
    public static boolean checkRectCollision(Collidable a, Collidable b) {
        return a.getBounds().intersects(b.getBounds());
    }

    /*
     * check ball and paddle collision
     */
    public static boolean checkBallPaddle(Ball ball, Paddle paddle) {
        return checkRectCollision(ball, paddle);
    }

    /*
     * find the brick that has been touch by the ball
     */
    public static Brick getCollidedBrick(Ball ball, List<Brick> bricks) {
        for (Brick brick : bricks) {
            if (!brick.isBroken() && checkRectCollision(ball, brick)) {
                brick.takeHit(); // giảm độ bền hoặc xử lý logic trúng đạn ở đây
                return brick;
            }
        }
        return null;
    }

    /*
     * find the powerup be touched by paddle
     */
    // public static PowerUp getCollidedPowerUp() {
    //
    // }
}