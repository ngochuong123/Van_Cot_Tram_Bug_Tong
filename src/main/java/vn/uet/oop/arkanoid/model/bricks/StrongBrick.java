package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class StrongBrick extends Brick {

    public StrongBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);
        try {
            BrickType.brick2_1Image = new Image(getClass().getResourceAsStream("/image/StrongBrick1.png"));
            BrickType.brick2_2Image = new Image(getClass().getResourceAsStream("/image/StrongBrick2.png"));
        } catch (Exception e) {
            System.err.println("Không thể tải hình ảnh brick");
            // Có thể set hình mặc định ở đây
        }
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
        if (durabilityPoints == 2) {
            gc.drawImage(BrickType.brick2_1Image, getX(), getY(), getWidth(), getHeight());
        } else if (durabilityPoints == 1) {
            gc.drawImage(BrickType.brick2_2Image, getX(), getY(), getWidth(), getHeight());
        }
    }

}
