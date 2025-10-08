package vn.uet.oop.arkanoid.model.interfaces;

import javafx.geometry.Rectangle2D;

/**
 * mark the object that can be collision
 */
public interface Collidable {

    /**
     * check bounds
     */
    Rectangle2D getBounds();
}