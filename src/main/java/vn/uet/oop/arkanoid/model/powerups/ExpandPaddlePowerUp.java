package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import vn.uet.oop.arkanoid.model.Paddle;
import javafx.scene.image.Image;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ExpandPaddlePowerUp extends PowerUp {

    private final Image expandPaddle;

    public ExpandPaddlePowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
        expandPaddle = new Image(getClass().getResourceAsStream("/image/expand_paddle.png"));
    }

    /**
     * apply effect to paddle.
     *
     * @param obj
     */
    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof Paddle) {

            Paddle paddle = (Paddle) obj;
            if (paddle.isHasActiveEffect()) {
                return;
            }

            paddle.setHasActiveEffect(true);
            double originalWidth = paddle.getWidth();
            // random 30% thu nhỏ paddle, 70% phóng to paddle
            double chance = new Random().nextDouble();
            double scaleFactor = (chance < 0.4) ? 0.5 : 1.5;
            paddle.setWidth(originalWidth * scaleFactor);
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
        gc.drawImage(expandPaddle, getX(), getY(), getWidth(), getHeight());
    }
}
