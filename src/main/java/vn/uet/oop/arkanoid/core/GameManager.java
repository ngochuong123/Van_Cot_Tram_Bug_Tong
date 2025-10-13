package vn.uet.oop.arkanoid.core;

import javafx.scene.canvas.GraphicsContext;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.*;
import vn.uet.oop.arkanoid.systems.PhysicsSystem;
import vn.uet.oop.arkanoid.model.bricks.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private PhysicsSystem physicsSystem;


    public GameManager() {
        initGame();
    }

    private void initGame() {
        paddle = new Paddle(
                (GameConfig.SCREEN_WIDTH - GameConfig.PADDLE_WIDTH) / 2,
                GameConfig.SCREEN_HEIGHT - 40,
                GameConfig.PADDLE_WIDTH,
                GameConfig.PADDLE_HEIGHT,
                GameConfig.PADDLE_SPEED
        );

        ball = new Ball(
                GameConfig.SCREEN_WIDTH / 2,
                GameConfig.SCREEN_HEIGHT / 2,
                GameConfig.BALL_RADIUS,
                GameConfig.BALL_SPEED,
                -GameConfig.BALL_SPEED
        );

        physicsSystem = new PhysicsSystem();

        bricks = new ArrayList<>();
        createBricks();
    }

    private void createBricks() {
        double startX = 50;
        double startY = 50;
        for (int row = 0; row < GameConfig.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConfig.BRICK_COLUMNS; col++) {
                double x = startX + col * (GameConfig.BRICK_WIDTH + GameConfig.BRICK_SPACING);
                double y = startY + row * (GameConfig.BRICK_HEIGHT + GameConfig.BRICK_SPACING);
                bricks.add(new NormalBrick(x, y, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT));
            }
        }
    }

    public void update(double deltaTime, boolean leftPressed, boolean rightPressed) {

        paddle.update(deltaTime, leftPressed, rightPressed);
        physicsSystem.updateBall(ball, deltaTime);
        physicsSystem.bounceBallOnWalls(ball);
        physicsSystem.bounceBallOnPaddle(ball, paddle);
        physicsSystem.bounceBallOnBricks(ball, bricks);


    }

    public void render(GraphicsContext gc) {
        ball.render(gc);
        paddle.render(gc);
        for (Brick brick : bricks) {
            brick.render(gc);
        }
    }
}