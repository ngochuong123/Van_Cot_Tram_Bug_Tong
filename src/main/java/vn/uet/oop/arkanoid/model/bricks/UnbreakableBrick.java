package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick {

    private final Image unbreakableBrickImage;

    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
        unbreakableBrickImage = new Image(getClass().getResourceAsStream("/image/UnbreakableBrick.png"));
    }

    @Override
    public int takeHit() {
        // Do nothing, unbreakable brick
        return 10;
    }

    @Override
    public boolean isBroken() {
        return false;
    }

    @Override
    public void update(double deltaTime) {
        // not need update
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(unbreakableBrickImage, getX(), getY(), getWidth(), getHeight());
    }
}
