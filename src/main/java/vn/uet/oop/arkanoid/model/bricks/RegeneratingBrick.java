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
    private boolean isRegenerating = false;

    public RegeneratingBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height);
        this.durabilityPoints = durabilityPoints;
        this.maxDurability = durabilityPoints;
    }

    @Override
    public int takeHit() {
        if (isRegenerating) return 0;

        durabilityPoints--;
        if (durabilityPoints <= 0) {
            isRegenerating = true;
            startRegeneration();
        }
        return durabilityPoints;
    }

    @Override
    public boolean isBroken() {
        return false;
    }

    @Override
    public void update(double deltaTime) {
        // no-op (regeneration handled by PauseTransition)
    }

    private void startRegeneration() {
        PauseTransition delay = new PauseTransition(Duration.seconds(respawnSeconds));
        delay.setOnFinished(e -> {
            Platform.runLater(() -> {
                this.durabilityPoints = maxDurability;
                this.isRegenerating = false;
            });
        });
        delay.play();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isRegenerating) {
            // effect while generating
            gc.setFill(Color.rgb(255, 0, 0, 0.3));
        } else {
            gc.setFill(Color.RED);
        }
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    public boolean isRegenerating() {
        return isRegenerating;
    }
}
