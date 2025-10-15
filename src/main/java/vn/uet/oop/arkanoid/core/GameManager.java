package vn.uet.oop.arkanoid.core;

import javafx.scene.canvas.GraphicsContext;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.*;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import vn.uet.oop.arkanoid.systems.PhysicsSystem;
import vn.uet.oop.arkanoid.model.bricks.*;
import java.util.ArrayList;
import java.util.List;
import vn.uet.oop.arkanoid.model.bricks.BrickType;
import vn.uet.oop.arkanoid.systems.PowerUpSystem;


public class GameManager {

    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private  List<PowerUp> powerUps;
    private PhysicsSystem physicsSystem;
    private PowerUpSystem powerUpSystem;


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
                0,
                0
        );

        powerUps = new ArrayList<>();
        powerUpSystem = new PowerUpSystem(powerUps, paddle, ball);
        physicsSystem = new PhysicsSystem();

        loadLevel(vn.uet.oop.arkanoid.config.Levels.LEVEL_1);

        ball.stickTo(paddle);
    }
        bricks = new ArrayList<>();

        int rows = pattern.length;
        int cols = pattern[0].length;
    private BrickType.type toType(int code) {
        return switch (code) {
            case 1 -> BrickType.type.NORMAL;
            case 2 -> BrickType.type.STRONG;
            case 10 -> BrickType.type.UNBREAKABLE;
            default -> BrickType.type.EMPTY;
        };
    }

    public void launchBall() {
        if (!ball.isLaunched()) {
            ball.launch();
        }
    }

        // calculate total width and height of the brick layout
        double totalW = cols * GameConfig.BRICK_WIDTH + (cols - 1) * GameConfig.BRICK_SPACING;
        double totalH = rows * GameConfig.BRICK_HEIGHT + (rows - 1) * GameConfig.BRICK_SPACING;

        double startX = (GameConfig.SCREEN_WIDTH  - totalW) / 2.0;
    private void loadLevel(int[][] pattern) {
        bricks = new ArrayList<>();

        int rows = pattern.length;
        int cols = pattern[0].length;

        // calculate total width and height of the brick layout
        double totalW = cols * GameConfig.BRICK_WIDTH + (cols - 1) * GameConfig.BRICK_SPACING;
        double totalH = rows * GameConfig.BRICK_HEIGHT + (rows - 1) * GameConfig.BRICK_SPACING;

        double startX = (GameConfig.SCREEN_WIDTH  - totalW) / 2.0;
        double startY = 50;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int code = pattern[r][c];
                BrickType.type type = toType(code);
                if (type == BrickType.type.EMPTY) continue;

                double x = startX + c * (GameConfig.BRICK_WIDTH  + GameConfig.BRICK_SPACING);
                double y = startY + r * (GameConfig.BRICK_HEIGHT + GameConfig.BRICK_SPACING);

                Brick b = BrickFactory.createBrick(type, x, y, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT);
                if (b != null) bricks.add(b);
            }
        }
    }

    public void update(double deltaTime, boolean leftPressed, boolean rightPressed) {

        paddle.update(deltaTime, leftPressed, rightPressed);

        if (!ball.isLaunched()) {
            ball.stickTo(paddle);
            return;
        }
        physicsSystem.updateBall(ball, deltaTime);
        physicsSystem.bounceBallOnWalls(ball, paddle);
        physicsSystem.bounceBallOnPaddle(ball, paddle);
        physicsSystem.bounceBallOnBricks(ball, bricks, powerUps);
        powerUpSystem.updatePowerUps(deltaTime);
        powerUpSystem.checkAndApply();
        physicsSystem.bounceBallOnBricks(ball, bricks, powerUps);

        if (bricks.isEmpty()) {
            System.out.println("Level cleared! Loading next level...");
            loadLevel(vn.uet.oop.arkanoid.config.Levels.LEVEL_2);
            ball.stickTo(paddle);
        }


    }

    public void render(GraphicsContext gc) {
        ball.render(gc);
        paddle.render(gc);
        for (Brick brick : bricks) {
            brick.render(gc);
        }
        for (PowerUp p : powerUps) {
            p.render(gc);
        }
    }
}