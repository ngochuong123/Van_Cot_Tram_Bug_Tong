package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NormalBrick extends Brick {

    private final Image brickImage;

    public NormalBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);
        this.durabilityPoints = Math.min(durabilityPoints, 1);
        brickImage = new Image(getClass().getResourceAsStream("/image/NormalBrick.png"));
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
        if (!isBroken()) {
            gc.drawImage(brickImage, getX(), getY(), getWidth(), getHeight());

            Image crackImage = getCrackImage();
            if (crackImage != null) {
                gc.drawImage(crackImage, getX(), getY(), getWidth(), getHeight());
            }
        }
    }

}
