package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StrongBrick extends Brick {

    public StrongBrick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height, durabilityPoints);

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
            gc.setFill(Color.DARKRED);
        } else if (durabilityPoints == 1) {
            gc.setFill(Color.ORANGE);
        }

        gc.fillRect(getX(), getY(), getWidth(), getHeight());


        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());

    }


}
