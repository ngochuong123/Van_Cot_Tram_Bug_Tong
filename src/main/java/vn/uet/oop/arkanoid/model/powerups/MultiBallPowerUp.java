package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.Ball;

import java.util.List;
import javafx.scene.image.Image;

public class MultiBallPowerUp extends PowerUp {

    public MultiBallPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
    }

    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof List<?>) {
            List<Ball> balls = (List<Ball>) obj;
            Ball original = balls.get(0);
            Ball b1 = new Ball(original.getX(), original.getY(), original.getRadius(),
                        original.getDx(), -original.getDy());
            Ball b2 = new Ball(original.getX(), original.getY(), original.getRadius(),
                        -original.getDx(), original.getDy());

            b1.setLaunched(true);
            b2.setLaunched(true);

            balls.add(b1);
            balls.add(b2);
        }
    }

    Image Multi = new Image("file:src/main/java/vn/uet/oop/arkanoid/resources/image/x3_Ball.png");
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(Multi, getX(), getY(), getWidth(), getHeight());
    }
}
