package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StrongBrick extends Brick {
    private int durabilityPoints = 2;

    public StrongBrick(double x, double y, double width, double height) {
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
    public boolean isDestroyed() {
        return durabilityPoints == 0;
    }

    @Override
    public void update(double deltaTime) {
        // not need update

    }

    @Override
    public void render(GraphicsContext gc) {
        if (isDestroyed()) {
            return;
        }
        if (durabilityPoints == 2) {
            gc.setFill(Color.DARKRED);
        } else if (durabilityPoints == 1) {
            gc.setFill(Color.GREEN);
        }

        gc.fillRect(getX(), getY(), getWidth(), getHeight());


        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());

    }


}
