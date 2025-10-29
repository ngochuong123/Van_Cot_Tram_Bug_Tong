package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.GameObject;

public class NormalBrick extends Brick {

    public NormalBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);
        try {
            BrickType.brick1Image = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/brick1.png");
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
        // render
        if (!isBroken()) {
            gc.drawImage(BrickType.brick1Image, getX(), getY(), getWidth(), getHeight());
        }
    }

}
