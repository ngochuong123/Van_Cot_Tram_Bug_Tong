// File: vn/uet/oop/arkanoid/model/bricks/ExplosiveBrick.java
package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;

public class ExplosiveBrick extends Brick {
    private final int blastRadius;

    public ExplosiveBrick(double x, double y, double width, double height, int durabilityPoints, int blastRadius) {
        super(x, y, width, height, durabilityPoints);
        this.blastRadius = Math.max(1, blastRadius);
    }

    public int getBlastRadius() { return blastRadius; }

    @Override
    public int takeHit() {
        durabilityPoints = Math.max(0, durabilityPoints - 1);
        return durabilityPoints;
    }

    @Override
    public boolean isBroken() {
        return durabilityPoints <= 0;
    }

    @Override
    public void update(double deltaTime) { }

    @Override
    public void render(GraphicsContext gc) {
        if (!isBroken()) {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
            gc.setStroke(javafx.scene.paint.Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}
