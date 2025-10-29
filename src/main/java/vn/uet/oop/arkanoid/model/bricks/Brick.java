package vn.uet.oop.arkanoid.model.bricks;

import vn.uet.oop.arkanoid.model.GameObject;

public abstract class Brick extends GameObject {
    protected int durabilityPoints;           // the number of hit to break brick

    public abstract int takeHit();          // return the remaining durability points after taking hit
    public abstract boolean isBroken();

    public Brick(double x, double y, double width, double height) {
        super(x, y, width, height);

    }

    public Brick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height);
        this.durabilityPoints = durabilityPoints;
    }



}
