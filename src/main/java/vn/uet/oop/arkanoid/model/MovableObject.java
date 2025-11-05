package vn.uet.oop.arkanoid.model;
import javafx.geometry.Rectangle2D;
import vn.uet.oop.arkanoid.model.interfaces.Collidable;
import vn.uet.oop.arkanoid.model.interfaces.Renderable;


public abstract class MovableObject extends GameObject implements Collidable, Renderable {
    private double dx, dy;        // vận tốc

    public MovableObject(double x, double y, double width, double height, double dx, double dy) {
        super(x,y,width,height);
        this.dx = dx;
        this.dy = dy;
    }
    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }
    public void setPosition(double x, double y) {
        setX(x);
        setY(y);
    }

    //Getter
    public double getDx() {
        return dx;
    }
    public double getDy() {
        return dy;
    }

    //Setter
    public void setDx(double dx) {
        this.dx = dx;
    }
    public void setDy(double dy) {
        this.dy = dy;
    }


}
