package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;

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
    public boolean isBroken() {
        return durabilityPoints == 0;
    }

    @Override
    public void update(double deltaTime) {
        //update
    }

    @Override
    public void render(GraphicsContext gc) {
        // render
    }


}
