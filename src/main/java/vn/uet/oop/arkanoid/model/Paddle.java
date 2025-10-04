package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

    public class Paddle extends MovableObject {
        private final double speed;
        private final int worldWidth;
        private boolean movingLeft = false;
        private boolean movingRight = false;

        public Paddle(double x, double y, double width, double height, double speed, int worldWidth) {
            super(x, y, width, height, 0, 0);
            this.speed = speed;
            this.worldWidth = worldWidth;
        }

        // Gọi từ input handler (khi nhấn phím)
        public void setMovingLeft(boolean movingLeft) {
            this.movingLeft = movingLeft;
        }

        public void setMovingRight(boolean movingRight) {
            this.movingRight = movingRight;
        }

        @Override
        public void update(double dt) {
            // Tính toán hướng di chuyển
            double vx = 0;

            if (movingLeft) vx -= speed;
            if (movingRight) vx += speed;

            // Cập nhật vị trí theo deltaTime
            x += vx * dt;

            // Giới hạn trong màn hình
            if (x < 0) x = 0;
            if (x + width > worldWidth) x = worldWidth - width;
        }

    }

