package vn.uet.oop.arkanoid.model.interfaces;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for objects that can be rendered on a JavaFX canvas.
 */
public interface Renderable {
    /*
     * Render the object on the given GraphicsContext
     */
    void render(GraphicsContext gc);
}
