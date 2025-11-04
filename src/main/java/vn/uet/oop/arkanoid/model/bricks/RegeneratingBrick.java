// File: vn/uet/oop/arkanoid/model/bricks/RegeneratingBrick.java
package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;

public class RegeneratingBrick extends Brick {
    private final int maxDurability;
    private final double respawnSeconds = GameConfig.BRICK_RESPAWN_TIME;
    private boolean destroyed = false;
    private boolean permanentlyRemoved = false; // be exploded to remove permanently
    private double respawnTimer = 0.0;

    public RegeneratingBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);
        this.maxDurability = durabilityPoints;
    }

    /**
     * destroy the brick permanently
     */
    public void destroyPermanently() {
        permanentlyRemoved = true;
        destroyed = true;
        durabilityPoints = 0;
    }

    @Override
    public int takeHit() {
        if (permanentlyRemoved) return 0;
        if (destroyed) return 0;

        durabilityPoints = Math.max(0, durabilityPoints - 1);
        if (durabilityPoints == 0) {
            destroyed = true;
            respawnTimer = respawnSeconds;
        }
        return durabilityPoints;
    }

    @Override
    public boolean isBroken() {
        return destroyed || permanentlyRemoved;
    }

    @Override
    public void update(double deltaTime) {
        if (permanentlyRemoved) return;
        if (destroyed) {
            respawnTimer -= deltaTime;
            if (respawnTimer <= 0) {
                destroyed = false;
                durabilityPoints = maxDurability;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isBroken()) {
            gc.setFill(Color.SALMON);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}
