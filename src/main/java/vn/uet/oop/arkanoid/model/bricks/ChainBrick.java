package vn.uet.oop.arkanoid.model.bricks;

import vn.uet.oop.arkanoid.core.GameManager;
import java.util.List;

public class ChainBrick extends Brick {
    private final int chainId;

    public ChainBrick(double x, double y, double width, double height, int durabilityPoints, int chainId) {
        super(x, y, width, height, durabilityPoints);
        this.chainId = chainId;
    }

    @Override
    public int takeHit() {
        durabilityPoints--;
        if (isBroken()) triggerChain();
        return durabilityPoints;
    }

    private void triggerChain() {
        List<Brick> bricks = GameManager.getInstance().getBricks();
        for (Brick b : bricks) {
            if (b instanceof ChainBrick cb && cb.chainId == this.chainId && !cb.isBroken()) {
                cb.durabilityPoints = 0;
            }
        }
        // TODO: add chain reaction effect if needed
    }

    @Override
    public boolean isBroken() {
        return durabilityPoints <= 0;
    }

    public int getChainId() {
        return chainId;
    }

    @Override
    public void update(double deltaTime) {
        // not need update
    }

    @Override
    public void render(javafx.scene.canvas.GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.ORANGE);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());

        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());
    }
}
