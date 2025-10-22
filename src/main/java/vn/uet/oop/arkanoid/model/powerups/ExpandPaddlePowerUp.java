package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.model.Paddle;
import vn.uet.oop.arkanoid.model.bricks.BrickType;
import vn.uet.oop.arkanoid.model.Ball;

import java.util.Timer;
import java.util.TimerTask;

public class ExpandPaddlePowerUp extends PowerUp {
    private Image toPaddle;

    public ExpandPaddlePowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
        toPaddle = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/toPaddle.png");
    }

    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof Paddle) {

            Paddle paddle = (Paddle) obj;
            if (paddle.isHasActiveEffect()) {
                return;
            }
            paddle.setHasActiveEffect(true);
            double originalWidth = paddle.getWidth();
            paddle.setWidth(paddle.getWidth() * 1.5);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    paddle.setWidth(originalWidth);
                    paddle.setHasActiveEffect(false);
                }
            }, 5000);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(toPaddle, getX(), getY(), getWidth() * 2, getHeight() * 2);
    }
}
