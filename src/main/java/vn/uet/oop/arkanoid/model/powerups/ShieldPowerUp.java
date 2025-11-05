package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.Paddle;
import javafx.scene.image.Image;

/**
 * ShieldPowerUp class
 */
public class ShieldPowerUp extends PowerUp {

    private final Image shieldPowerUp;

    /**
     * ShieldPowerUp's constructor.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param dY
     */
    public ShieldPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
        shieldPowerUp = new Image(getClass().getResourceAsStream("/image/ShieldPowerUp.png"));
    }

    /**
     * ShieldPowerUp's effect
     * @param o
     */
    @Override
    public void applyEffect(Object o) {
        if (o instanceof Paddle) {
            Paddle paddle = (Paddle) o;
            paddle.setHasShield(true);
        }
    }

    //render ShieldPowerUp image
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(shieldPowerUp, getX(), getY(), getWidth(), getHeight());
    }
}
