// File: vn/uet/oop/arkanoid/model/bricks/InvisibleBrick.java
package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class InvisibleBrick extends Brick {
    private boolean revealed = false;

    private final Image invisibleBrickImage;

    public InvisibleBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);
        this.durabilityPoints = Math.min(durabilityPoints, 4);
        invisibleBrickImage = new Image(getClass().getResourceAsStream("/image/InvisibleBrick.png"));
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
            if (!isBroken()) {
                gc.drawImage(invisibleBrickImage, getX(), getY(), getWidth(), getHeight());
            }
        }

    }
}
