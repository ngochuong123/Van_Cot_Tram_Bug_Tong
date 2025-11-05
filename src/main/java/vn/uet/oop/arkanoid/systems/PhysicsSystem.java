package vn.uet.oop.arkanoid.systems;

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
                // Nếu paddle có shield thì bóng nảy lại, không reset
                if (paddle.isHasShield()) {
                    ball.setDy(-Math.abs(ball.getDy())); // Bật lên
                    ball.setY(GameConfig.SCREEN_HEIGHT - ball.getHeight() - 5);
                    paddle.setHasShield(false); // Shield chỉ dùng 1 lần
                } else if (ball.getY() - ball.getRadius() > GameConfig.SCREEN_HEIGHT + 20) {
                // Đánh dấu bóng đã rơi khỏi màn hình, không reset ở đây nữa
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
     * Phát hiện + xử lý logic gạch NGAY TRONG FRAME.
     *
     * @param ball   ball
     * @param bricks list bricks
     * @return brick bị chạm (có thể đã bị remove sau xử lý)
     */
    public Brick bounceBallOnBricks(Ball ball, List<Brick> bricks) {
        Brick hitBrick = CollisionSystem.getCollidedBrick(ball, bricks);
        if (hitBrick == null) return null;

        // --- Phản xạ trước ---
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

    /* ------------ Helpers ------------ */

    private void resolveBounce(Ball ball, Brick hitBrick) {
        double ballCenterX = ball.getX() + ball.getWidth() / 2;
        double ballCenterY = ball.getY() + ball.getHeight() / 2;
        double brickCenterX = hitBrick.getX() + hitBrick.getWidth() / 2;
        double brickCenterY = hitBrick.getY() + hitBrick.getHeight() / 2;

        double dx = (ballCenterX - brickCenterX) / hitBrick.getWidth();
        double dy = (ballCenterY - brickCenterY) / hitBrick.getHeight();

        if (Math.abs(dx) > Math.abs(dy)) {
            ball.setDx(-ball.getDx());
            // Đẩy bóng ra khỏi gạch 1 chút để tránh kẹt
            if (dx > 0) {
                ball.setX(hitBrick.getX() + hitBrick.getWidth());
            } else {
                ball.setX(hitBrick.getX() - ball.getWidth());
            }
        } else {
            ball.setDy(-ball.getDy());
            // Đẩy bóng ra khỏi gạch 1 chút
            if (dy > 0) {
                ball.setY(hitBrick.getY() + hitBrick.getHeight());
            } else {
                ball.setY(hitBrick.getY() - ball.getHeight());
            }
        }
    }

    /**
     * Invisible: lần đầu chỉ reveal (không trừ máu)
     * Explosive: khi vỡ nổ 8 hướng, phá mọi gạch (kể cả Unbreakable), Regenerating bị remove vĩnh viễn
     * Chain: vỡ 1 viên -> remove cả chain cùng id
     * Regenerating: khi vỡ thì destroyed & chờ hồi (update ở lớp gạch); nếu bị nổ thì remove vĩnh viễn
     * Unbreakable: không vỡ bởi bóng (nhưng bị nổ thì remove tại đây)
     * Normal: vỡ thì remove
     */
    private void applyBrickLogicSameFrame(Brick hit, List<Brick> bricks) {

        // 5) Unbreakable: không bao giờ vỡ do bóng
        if (hit instanceof UnbreakableBrick) {
            return;
        }

        // 1) Invisible: lần đầu đập -> chỉ lộ, không trừ máu
        if (hit instanceof InvisibleBrick inv) {
            boolean wasRevealed = inv.isRevealed();
            inv.takeHit(); // nếu chưa lộ thì chỉ set revealed=true
            if (!wasRevealed && inv.isRevealed()) {
                return; // kết thúc frame, chưa phá
            }
        } else {
            // Các loại khác: nhận sát thương ngay
            hit.takeHit();
        }

        // 2) Explosive: khi vỡ -> nổ 8 hướng & remove ngay
        if (hit instanceof ExplosiveBrick && hit.isBroken()) {
            explodeAndRemoveNeighbors((ExplosiveBrick) hit, bricks);
            // remove chính viên nổ (nếu còn trong list)
            bricks.remove(hit);
            return;
        }

        // 3) Chain: nếu vỡ -> remove tất cả ChainBrick cùng chainId
        if (hit instanceof ChainBrick cb && hit.isBroken()) {
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

        // 4) Regenerating: khi vỡ -> ở lại list, tự đếm hồi bằng update(); KHÔNG remove
        if (hit instanceof RegeneratingBrick) {
            return;
        }


        // 6) Gạch thường: vỡ thì remove ngay
        if (hit.isBroken()) {
            bricks.remove(hit);
        }
    }

    /** Nổ 8 hướng theo bước tâm-tâm: step = kích thước ô + spacing */
    private void explodeAndRemoveNeighbors(ExplosiveBrick origin, List<Brick> bricks) {
        List<Brick> toRemove = new ArrayList<>();

        double w  = origin.getWidth();
        double h  = origin.getHeight();
        double cx0 = origin.getX() + w / 2.0;
        double cy0 = origin.getY() + h / 2.0;

        // Bước lưới theo tâm-tâm
        double stepX = w + GameConfig.BRICK_SPACING;
        double stepY = h + GameConfig.BRICK_SPACING;

        // Tolerance: nửa spacing + chút đệm
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
                    toRemove.add(b); // cả Unbreakable cũng remove theo yêu cầu
                }
            }
        }
        bricks.removeAll(toRemove);
    }


}