package vn.uet.oop.arkanoid.model.interfaces;

import java.awt.*;

/**
 * mark the object that can be collision
 */
public interface Collidable {

    /**
     * check bounds
     */
    Rectangle getBounds();

    /**
     * handle collision
     */
    void onCollision(Collidable other);
}