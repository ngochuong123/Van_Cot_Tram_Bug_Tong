package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.Paddle;
import javafx.scene.image.Image;

public class ShieldPowerUp extends PowerUp {

    public ShieldPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
    }

    @Override
    public void applyEffect(Object o) {
        if (o instanceof Paddle) {
            Paddle paddle = (Paddle) o;
            paddle.setHasShield(true);
        }
    }

    Image ShieldPowerUp = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/ShieldPowerUp.png");

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(ShieldPowerUp, getX(), getY(), getWidth(), getHeight());
    }
}
