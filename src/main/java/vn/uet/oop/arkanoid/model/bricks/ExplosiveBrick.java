// File: vn/uet/oop/arkanoid/model/bricks/ExplosiveBrick.java
package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ExplosiveBrick extends Brick {
    private final int blastRadius;

    private final Image explosiveBrickImage;

    public ExplosiveBrick(double x, double y, double width, double height, int durabilityPoints, int blastRadius) {
        super(x, y, width, height, durabilityPoints);
        this.blastRadius = Math.max(1, blastRadius);

        this.durabilityPoints = Math.min(durabilityPoints, 4);
        explosiveBrickImage = new Image(getClass().getResourceAsStream("/image/ExplosiveBrick.png"));
    }

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
            gc.drawImage(explosiveBrickImage, getX(), getY(), getWidth(), getHeight());
            Image crackImage = getCrackImage();
            if (crackImage != null) {
                gc.drawImage(crackImage, getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    public int getBlastRadius() { return blastRadius; }
}
