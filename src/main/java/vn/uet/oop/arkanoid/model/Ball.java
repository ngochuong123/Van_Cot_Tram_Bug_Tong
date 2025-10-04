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
        @Override
        public void update(double dt) {
            // Cập nhật vị trí dựa theo vận tốc
            setX(getX() + getDx() * dt);
            setY(getY() + getDy() * dt);
        }

        @Override
        public void render(GraphicsContext gc) {

        }
    }
