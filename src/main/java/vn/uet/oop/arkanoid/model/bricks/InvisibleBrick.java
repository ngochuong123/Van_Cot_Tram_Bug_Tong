package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InvisibleBrick extends Brick {
    private boolean revealed = false;

    public InvisibleBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);

    }

    @Override
    public int takeHit() {
        if (!revealed) {
            revealed = true;
            // TODO: turn on some visual effect to indicate the brick is revealed
            return durabilityPoints; // first hit just reveals the brick
        }

        durabilityPoints--;
        return durabilityPoints;
    }

    @Override
    public boolean isBroken() {
        return durabilityPoints <= 0;
    }

    public boolean isRevealed() {
        return revealed;
    }

    @Override
    public void update(double deltaTime) {
        // not need update
    }

    @Override
    public void render(GraphicsContext gc) {
        if (revealed) {
            gc.setFill(Color.ORANGE);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());

            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        }
        // Nếu chưa lộ diện thì không vẽ gì
    }
}
