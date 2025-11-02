// File: vn/uet/oop/arkanoid/model/bricks/InvisibleBrick.java
package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InvisibleBrick extends Brick {
    private boolean revealed = false;

    public InvisibleBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);
    }

    /**
     * first hit just reveals the brick without reducing durability
     *
     * @return
     */
    @Override
    public int takeHit() {
        if (!revealed) {
            revealed = true;
            return durabilityPoints;
        }
        durabilityPoints = Math.max(0, durabilityPoints - 1);
        return durabilityPoints;
    }

    public boolean isRevealed() { return revealed; }

    @Override
    public boolean isBroken() {
        return revealed && durabilityPoints <= 0;
    }

    @Override
    public void update(double deltaTime) { }

    @Override
    public void render(GraphicsContext gc) {
        if (revealed) {
            gc.setFill(Color.GREEN);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        }

    }
}
