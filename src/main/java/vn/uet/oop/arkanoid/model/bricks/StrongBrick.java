package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class StrongBrick extends Brick {

    private final Image strongBrick;

    public StrongBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);
        this.durabilityPoints = Math.min(durabilityPoints, 3);
        strongBrick = new Image(getClass().getResourceAsStream("/image/StrongBrick.png"));
    }

    @Override
    public int takeHit() {
        if (durabilityPoints > 0) {
            durabilityPoints--;
        }
        return durabilityPoints;
    }

    @Override
    public boolean isBroken() {
        return durabilityPoints == 0;
    }

    @Override
    public void update(double deltaTime) {
        // not need update
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isBroken()) {
            return;
        }
        gc.drawImage(strongBrick, getX(), getY(), getWidth(), getHeight());
        Image crackImage = getCrackImage();
        if (crackImage != null) {
            gc.drawImage(crackImage, getX(), getY(), getWidth(), getHeight());
        }
    }
}
