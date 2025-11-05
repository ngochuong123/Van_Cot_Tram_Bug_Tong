package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import vn.uet.oop.arkanoid.model.Ball;

import java.util.ArrayList;
import java.util.List;

public class MultiBallPowerUp extends PowerUp {

    public MultiBallPowerUp(double x, double y, double width, double height, double dY) {
        super(x, y, width, height, dY);
    }

    @Override
    public void applyEffect(Object obj) {
        if (obj instanceof List<?>) {
            List<Ball> balls = (List<Ball>) obj;
            List<Ball> newBalls = new ArrayList<>();

            for (Ball original : balls) {
                double speed = Math.sqrt(original.getDx() * original.getDx() + original.getDy() * original.getDy());
                double baseAngle = Math.atan2(original.getDy(), original.getDx());

                // Tạo 2 hướng lệch nhẹ
                double angleOffset1 = Math.toRadians(15 + Math.random() * 10);
                double angleOffset2 = Math.toRadians(-(15 + Math.random() * 10));

                double dx1 = speed * Math.cos(baseAngle + angleOffset1);
                double dy1 = speed * Math.sin(baseAngle + angleOffset1);
                double dx2 = speed * Math.cos(baseAngle + angleOffset2);
                double dy2 = speed * Math.sin(baseAngle + angleOffset2);

                // Tạo bóng mới
                Ball b1 = new Ball(original.getX(), original.getY(), original.getRadius(), dx1, dy1);
                Ball b2 = new Ball(original.getX(), original.getY(), original.getRadius(), dx2, dy2);

                b1.setLaunched(true);
                b2.setLaunched(true);

                // Nếu FatBall đang bật → bóng mới cũng phải to
                if (FatBallPowerUp.isFatBallActive()) {
                    b1.setRadius(original.getRadius());
                    b1.setHeight(original.getRadius() * 2);
                    b1.setWidth(original.getRadius() * 2);

                    b2.setRadius(original.getRadius());
                    b2.setHeight(original.getRadius() * 2);
                    b2.setWidth(original.getRadius() * 2);
                }

                newBalls.add(b1);
                newBalls.add(b2);
            }

            // Thêm bóng mới vào danh sách chính
            balls.addAll(newBalls);
        }
    }

    Image Multi = new Image("file:src/main/resources/image/x3_Ball.png");

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(Multi, getX(), getY(), getWidth(), getHeight());
    }
}
