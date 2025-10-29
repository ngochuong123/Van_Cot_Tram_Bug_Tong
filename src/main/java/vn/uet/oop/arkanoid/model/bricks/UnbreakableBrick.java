package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick {

    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
        try {
            BrickType.brick3Image = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/brick3.png");
        } catch (Exception e) {
            System.err.println("Không thể tải hình ảnh brick");
            // Có thể set hình mặc định ở đây
        }
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
        gc.drawImage(BrickType.brick3Image, getX(), getY(), getWidth(), getHeight());
    }
}
