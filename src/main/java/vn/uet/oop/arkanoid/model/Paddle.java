package vn.uet.oop.arkanoid.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

    public class Paddle extends MovableObject {
        private final double speed;
        private final int worldWidth;

        public Paddle(double x, double y, double width, double height, double speed, int worldWidth) {
            super(x, y, width, height, 0, 0);
            this.speed = speed;
            this.worldWidth = worldWidth;
        }

        public void moveLeft() {
            x -= speed;
            if (x < 0) x = 0;
        }

        public void moveRight() {
            x += speed;
            if (x + width > worldWidth) x = worldWidth - width;
        }
    }

