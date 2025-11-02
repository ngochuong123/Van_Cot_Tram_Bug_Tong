// File: vn/uet/oop/arkanoid/model/bricks/ChainBrick.java
package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;

public class ChainBrick extends Brick {
    private final int chainId;

    public ChainBrick(double x, double y, double width, double height, int durabilityPoints, int chainId) {
        super(x, y, width, height, durabilityPoints);
        this.chainId = chainId;
    }

    public int getChainId() { return chainId; }

    @Override
    public int takeHit() {
        durabilityPoints = Math.max(0, durabilityPoints - 1);
        return durabilityPoints;
    }

    @Override
    public boolean isBroken() { return durabilityPoints <= 0; }

    @Override
    public void update(double deltaTime) { /* no-op */ }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.ORANGE);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());
    }
}
