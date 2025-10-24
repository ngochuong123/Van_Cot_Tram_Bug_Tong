
package vn.uet.oop.arkanoid.core;

import javafx.scene.canvas.GraphicsContext;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.*;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import vn.uet.oop.arkanoid.systems.PhysicsSystem;
import vn.uet.oop.arkanoid.model.bricks.ResourceLevelLoader;
import vn.uet.oop.arkanoid.model.bricks.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import vn.uet.oop.arkanoid.model.bricks.BrickType;
import vn.uet.oop.arkanoid.systems.PowerUpSystem;

public class GameManager {
    private static GameManager instance;
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private PhysicsSystem physicsSystem;
    private PowerUpSystem powerUpSystem;

    public GameManager() {
        initGame();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
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
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        powerUpSystem = new PowerUpSystem(powerUps, paddle, ball);
        physicsSystem = new PhysicsSystem();

        // Load level from classpath resource (place your file at `src/main/resources/levels/level1.txt`)
        loadLevelFromClasspath("/levels/level1.txt");

        ball.stickTo(paddle);
    }

    public void launchBall() {
        if (!ball.isLaunched()) {
            ball.launch();
        }
    }

    public void loadLevelFromClasspath(String resourcePath) {
        try {
            bricks = ResourceLevelLoader.loadFromResource(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
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

        if (bricks.isEmpty()) {
            System.out.println("Level cleared! Loading next level...");
            loadLevelFromClasspath("/levels/level2.txt");
            ball.stickTo(paddle);
        }
    }

    public void render(GraphicsContext gc) {
        ball.render(gc);
        paddle.render(gc);
        if (bricks != null) {
            for (Brick brick : bricks) {
                if (brick != null) brick.render(gc);
            }
        }
        if (powerUps != null) {
            for (PowerUp p : powerUps) {
                if (p != null) p.render(gc);
            }
        }
    }

    public List<Brick> getBricks() {
        return bricks;
    }


}
