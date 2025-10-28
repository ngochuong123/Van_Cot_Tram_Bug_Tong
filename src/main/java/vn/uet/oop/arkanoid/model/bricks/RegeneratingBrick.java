package vn.uet.oop.arkanoid.model.bricks;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.core.GameManager;

import java.util.List;

public class RegeneratingBrick extends Brick {
    private final int maxDurability;
    private final double respawnSeconds = GameConfig.BRICK_RESPAWN_TIME;
    private boolean destroyed = false;
    // remember index so we can try to reinsert at the same position
    private int originalIndex = -1;

    public RegeneratingBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height);
        this.durabilityPoints = durabilityPoints;
        this.maxDurability = durabilityPoints;
    }

    @Override
    public int takeHit() {
        if (destroyed) return 0;

        durabilityPoints--;
        if (isBroken()) {
            destroyed = true;
            captureOriginalIndex();
            startRegeneration();
        }
        return durabilityPoints;
    }

    private void captureOriginalIndex() {
        GameManager gm = GameManager.getInstance();
        if (gm != null) {
            List<Brick> bricks = gm.getBricks();
            if (bricks != null) {
                originalIndex = bricks.indexOf(this);
                // if index is -1 it means some other code removed it immediately; we'll still attempt to re-add
            }
        }
    }

    /**
     * Starts the regeneration process after a delay and reinserts the brick at the original index if possible.
     */
    private void startRegeneration() {
        PauseTransition delay = new PauseTransition(Duration.seconds(respawnSeconds));
        delay.setOnFinished(e -> {
            // Ensure modifications to the bricks list happen on the JavaFX thread
            Platform.runLater(() -> {
                this.durabilityPoints = maxDurability;
                this.destroyed = false;

                GameManager gm = GameManager.getInstance();
                if (gm == null) {
                    System.err.println("RegeneratingBrick: GameManager is null, cannot reinsert brick.");
                    return;
                }

                List<Brick> bricks = gm.getBricks();
                if (bricks == null) {
                    System.err.println("RegeneratingBrick: bricks list is null, cannot reinsert brick.");
                    return;
                }

                // If already present do nothing
                if (bricks.contains(this)) {
                    // reset done, already in list
                    return;
                }

                // Try to insert at the original index if valid, otherwise append at end
                int insertAt = (originalIndex >= 0 && originalIndex <= bricks.size()) ? originalIndex : bricks.size();
                try {
                    bricks.add(insertAt, this);
                } catch (IndexOutOfBoundsException ex) {
                    // fallback to add at end
                    bricks.add(this);
                }

                System.err.println("RegeneratingBrick: reinserted at index " + insertAt);
            });
        });
        delay.play();
    }

    @Override
    public boolean isBroken() {
        return durabilityPoints <= 0;
    }

    @Override
    public void update(double deltaTime) {
        // no-op (regeneration handled by PauseTransition)
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            gc.setFill(Color.RED);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}
