// java
package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import vn.uet.oop.arkanoid.core.GameManager;

import java.util.List;

public class ExplosiveBrick extends Brick {
    private int blastRadius = 1;

    public ExplosiveBrick(double x, double y, double width, double height, int durabilityPoints, int blastRadius) {
        super(x, y, width, height, durabilityPoints);
        this.blastRadius = blastRadius;
    }

    @Override
    public int takeHit() {
        durabilityPoints--;
        if (isBroken()) {
            explode();
        }
        return durabilityPoints;
    }

    private void explode() {
        List<Brick> bricks = GameManager.getInstance().getBricks();
        for (Brick b : bricks) {
            if (b == this) continue;

            double dx = Math.abs(b.getX() - this.getX());
            double dy = Math.abs(b.getY() - this.getY());
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= blastRadius * this.getWidth()) {
                if (!(b instanceof UnbreakableBrick)) {
                    b.takeHit();
                }
            }
        }
        // add explosion effect if needed

    }

    @Override
    public boolean isBroken() {
        return durabilityPoints <= 0;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(GraphicsContext gc) {

    }
}
