package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

    public class Ball extends MovableObject {
        private final double radius;
        private final int worldWidth, worldHeight;

        public Ball(double x, double y, double radius, double dx, double dy, int worldWidth, int worldHeight) {
            super(x, y, radius * 2, radius * 2, dx, dy);
            this.radius = radius;
            this.worldWidth = worldWidth;
            this.worldHeight = worldHeight;
        }

        //cai nay cung xoa luon
        @Override
        public void move() {
            super.move();
            // Va chạm tường
            if (x <= 0 || x + width >= worldWidth) dx = -dx;
            if (y <= 0 || y + height >= worldHeight) dy = -dy;
        }
    }
