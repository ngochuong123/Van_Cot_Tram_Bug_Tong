package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
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

    }


}
