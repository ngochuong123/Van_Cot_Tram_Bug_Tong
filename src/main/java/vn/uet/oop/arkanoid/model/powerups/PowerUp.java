package vn.uet.oop.arkanoid.model.powerups;

import javafx.geometry.Rectangle2D;
import vn.uet.oop.arkanoid.model.GameObject;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;
import vn.uet.oop.arkanoid.model.interfaces.Renderable;

public abstract class PowerUp extends GameObject implements Collidable, Renderable {
    private double dY;

    public PowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height);
        this.dY = dY;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }

    public double getDY() {
        return dY;
    }

    public void setDY(double dY) {
        this.dY = dY;
    }

    // PowerUp rơi xuống
    @Override
    public void update(double deltaTime) {
        setY(getY() + dY * deltaTime);
    }

    public boolean isActive() {
        return getY() < GameConfig.SCREEN_HEIGHT;
    }

    // Mỗi PowerUp sẽ có hiệu ứng riêng
    public abstract void applyEffect(Object o);
}
