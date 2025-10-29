package vn.uet.oop.arkanoid.model.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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

                // Lấy góc bay hiện tại
                double baseAngle = Math.atan2(original.getDy(), original.getDx());

                // Tạo 2 góc lệch nhẹ, mỗi lần random
                double angleOffset1 = Math.toRadians(15 + Math.random() * 10); // +15° đến +25°
                double angleOffset2 = Math.toRadians(-(15 + Math.random() * 10)); // -15° đến -25°

                // Tính hướng mới
                double dx1 = speed * Math.cos(baseAngle + angleOffset1);
                double dy1 = speed * Math.sin(baseAngle + angleOffset1);

                double dx2 = speed * Math.cos(baseAngle + angleOffset2);
                double dy2 = speed * Math.sin(baseAngle + angleOffset2);

                // Tạo 2 bóng mới từ bóng gốc
                Ball b1 = new Ball(original.getX(), original.getY(), original.getRadius(), dx1, dy1);
                Ball b2 = new Ball(original.getX(), original.getY(), original.getRadius(), dx2, dy2);

                b1.setLaunched(true);
                b2.setLaunched(true);

                newBalls.add(b1);
                newBalls.add(b2);
            }

            // Thêm các bóng mới vào danh sách chính
            balls.addAll(newBalls);
        }
    }


    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GOLD);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}
