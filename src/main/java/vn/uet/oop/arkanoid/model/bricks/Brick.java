package vn.uet.oop.arkanoid.model.bricks;

import vn.uet.oop.arkanoid.model.GameObject;

public abstract class Brick extends GameObject {
    private int durabilityPoints;           // the number of hit to break brick

    public abstract int takeHit();          // return the remaining durability points after taking hit
    public abstract boolean isBroken();     // return true if the brick is broken

}
