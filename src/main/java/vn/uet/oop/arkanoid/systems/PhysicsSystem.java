package vn.uet.oop.arkanoid.systems;

import vn.uet.oop.arkanoid.audio.AudioEngine;
import vn.uet.oop.arkanoid.audio.SoundManager;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.Ball;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.bricks.*;

import java.util.ArrayList;
import java.util.List;

/*
 * manage physic state
 */
public class PhysicsSystem {
    /*
     * check ball statement with wall
     */
    public void bounceBallOnWalls(Ball ball, Paddle paddle) {
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setDx(Math.abs(ball.getDx()));
        } else if (ball.getX() + ball.getWidth() >= GameConfig.SCREEN_WIDTH) {
            ball.setX(GameConfig.SCREEN_WIDTH - ball.getWidth());
            ball.setDx(-Math.abs(ball.getDx()));
        }

        // boundary top and bottom
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setDy(Math.abs(ball.getDy()));
        }

            if (ball.getY() >= GameConfig.SCREEN_HEIGHT) {
                // set shield effect
                if (paddle.isHasShield()) {
                    ball.setDy(-Math.abs(ball.getDy()));
                    ball.setY(GameConfig.SCREEN_HEIGHT - ball.getHeight() - 5);
                    paddle.setHasShield(false);
                } else if (ball.getY() - ball.getRadius() > GameConfig.SCREEN_HEIGHT + 20) {
                ball.setOutOfScreen(true);
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
        paddle.onBallHit();
        AudioEngine.playSound(SoundManager.HIT_WALL);

        double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
        double ballCenter = ball.getX() + ball.getWidth() / 2.0;
        double hitOffset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

        double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());

        double maxAngle = Math.toRadians(60); // 60 degrees

        // angle of reflection
        double angle = hitOffset * maxAngle;

        // update ball velocity
        ball.setDx(speed * Math.sin(angle));
        ball.setDy(-Math.abs(speed * Math.cos(angle)));
    }

    /**
     * check logic when ball hits bricks.
     * @param ball the ball
     * @param bricks list of bricks
     */
    public Brick bounceBallOnBricks(Ball ball, List<Brick> bricks) {
        Brick hitBrick = CollisionSystem.getCollidedBrick(ball, bricks);
        if (hitBrick == null) return null;

        AudioEngine.playSound(SoundManager.HIT_BRICK);
        boolean isFireball = ball.isFireMode();
        boolean isUnbreakable = hitBrick instanceof UnbreakableBrick;
        if (isFireball && !isUnbreakable) {
            if (hitBrick instanceof RegeneratingBrick regen) {
                regen.destroyPermanently();
            }

            applyBrickLogicSameFrame(hitBrick, bricks);
        } else {
            applyBrickLogicSameFrame(hitBrick, bricks);
        }


        if (!isFireball || (isFireball && isUnbreakable)) {
            resolveBounce(ball, hitBrick);
        }

        return hitBrick;
    }

    /**
     * check and resolve when ball bounces on brick.
     *
     * @param ball
     * @param hitBrick
     */
    private void resolveBounce(Ball ball, Brick hitBrick) {
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
    }

    /**
     * apply brick logic immediately.
     *
     * @param hit the hit brick
     * @param bricks list of bricks
     */
    private void applyBrickLogicSameFrame(Brick hit, List<Brick> bricks) {

        if (hit instanceof UnbreakableBrick) {
            return;
        }

        // first hit just reveals.
        hit.takeHit();
        if (hit instanceof InvisibleBrick inv && !inv.isBroken()) {
            return;
        }

        // explode neighbors
        if (hit instanceof ExplosiveBrick && hit.isBroken()) {
            AudioEngine.playSound(SoundManager.BREAK_BRICK);
            explodeAndRemoveNeighbors((ExplosiveBrick) hit, bricks);
            bricks.remove(hit);
            return;
        }

        // remove all chain bricks with same id
        if (hit instanceof ChainBrick cb && hit.isBroken()) {
            AudioEngine.playSound(SoundManager.BREAK_BRICK);
            int id = cb.getChainId();
            List<Brick> toRemove = new ArrayList<>();
            for (Brick b : bricks) {
                if (b instanceof ChainBrick other && other.getChainId() == id) {
                    toRemove.add(b);
                }
            }
            bricks.removeAll(toRemove);
            return;
        }

        //regenerating after some time
        if (hit instanceof RegeneratingBrick) {
            return;
        }

        if (hit.isBroken()) {
            AudioEngine.playSound(SoundManager.BREAK_BRICK);
            bricks.remove(hit);
        }
    }

    /**
     * explode and remove neighboring bricks.
     *
     * @param origin the explosive brick
     * @param bricks list of bricks
     */
    private void explodeAndRemoveNeighbors(ExplosiveBrick origin, List<Brick> bricks) {
        List<Brick> toRemove = new ArrayList<>();

        double w  = origin.getWidth();
        double h  = origin.getHeight();
        double cx0 = origin.getX() + w / 2.0;
        double cy0 = origin.getY() + h / 2.0;

        double stepX = w + GameConfig.BRICK_SPACING;
        double stepY = h + GameConfig.BRICK_SPACING;

        double tolX = Math.max(2.0, GameConfig.BRICK_SPACING * 0.6);
        double tolY = Math.max(2.0, GameConfig.BRICK_SPACING * 0.6);

        for (Brick b : bricks) {
            if (b == origin) continue;

            double cx = b.getX() + b.getWidth() / 2.0;
            double cy = b.getY() + b.getHeight() / 2.0;
            double dx = cx - cx0;
            double dy = cy - cy0;

            long gx = Math.round(dx / stepX);
            long gy = Math.round(dy / stepY);

            double snapX = gx * stepX;
            double snapY = gy * stepY;

            boolean closeToGrid = Math.abs(dx - snapX) <= tolX && Math.abs(dy - snapY) <= tolY;
            boolean isNeighbor  = Math.max(Math.abs(gx), Math.abs(gy)) == 1 && !(gx == 0 && gy == 0);

            if (closeToGrid && isNeighbor) {
                if (b instanceof RegeneratingBrick regen) {
                    regen.destroyPermanently();
                    toRemove.add(b);
                } else {
                    toRemove.add(b);
                }
            }
        }
        bricks.removeAll(toRemove);
    }


}