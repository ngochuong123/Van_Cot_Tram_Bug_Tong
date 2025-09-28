package vn.uet.oop.arkanoid.model.interfaces;

/**
 * mark the object that can be collision
 */
public class Collidable {

    /**
     * check bounds
     */
    Rectangle getBounds();

    /**
     * handle collision
     */
    void onCollision(Collidable other);
}