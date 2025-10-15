package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.GameObject;

public class NormalBrick extends Brick {
    private int durabilityPoints = 1;

    public NormalBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
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
        //render
        if (!isBroken()) {
            gc.setFill(javafx.scene.paint.Color.ORANGE);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());

            gc.setStroke(javafx.scene.paint.Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeRect(getX(), getY(), getWidth(), getHeight());
        }
    }


}
