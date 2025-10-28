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

        balls = new ArrayList<>();
        Ball mainBall = new Ball(
                GameConfig.SCREEN_WIDTH / 2,
                GameConfig.SCREEN_HEIGHT / 2,
                GameConfig.BALL_RADIUS,
                0,
                0
        );
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        powerUpSystem = new PowerUpSystem(powerUps, paddle, balls);
        physicsSystem = new PhysicsSystem();

        // Load level from classpath resource (place your file at `src/main/resources/levels/level1.txt`)
        loadLevelFromClasspath("/levels/level1.txt");

        ball.stickTo(paddle);
    }

    public void launchBall() {
        // chỉ phóng nếu quả bóng chính chưa bay
        if (!balls.get(0).isLaunched()) {
            balls.get(0).launch();
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
        //Cập nhật paddle
        paddle.update(deltaTime, leftPressed, rightPressed);

        //Nếu tất cả bóng hiện tại đều chưa phóng -> dính theo paddle (trạng thái ban đầu)
        if (balls.size() == 1 && !balls.get(0).isLaunched()) {
            balls.get(0).stickTo(paddle);
            return;
        }

        //Danh sách bóng rơi khỏi màn hình
        List<Ball> toRemove = new ArrayList<>();

        //Cập nhật từng bóng
        for (Ball ball : new ArrayList<>(balls)) {
            physicsSystem.updateBall(ball, deltaTime);
            physicsSystem.bounceBallOnWalls(ball, paddle);
            physicsSystem.bounceBallOnPaddle(ball, paddle);
            physicsSystem.bounceBallOnBricks(ball, bricks, powerUps);

            // Nếu bóng rơi khỏi màn hình thì đánh dấu để xóa
            if (ball.getY() > GameConfig.SCREEN_HEIGHT) {
                toRemove.add(ball);
            }
        }

        //Xóa bóng rơi
        balls.removeAll(toRemove);

        //Nếu không còn bóng nào -> tạo lại 1 bóng mới dính paddle
        if (balls.isEmpty()) {
            Ball newBall = new Ball(
                    paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_RADIUS,
                    paddle.getY() - GameConfig.BALL_RADIUS * 2,
                    GameConfig.BALL_RADIUS,
                    0,
                    0
            );
            newBall.stickTo(paddle);
            balls.add(newBall);
        }

        //Cập nhật PowerUp
        powerUpSystem.updatePowerUps(deltaTime);
        powerUpSystem.checkAndApply();

        //Nếu qua màn
        if (bricks.isEmpty()) {
            System.out.println("Level cleared! Loading next level...");
            loadLevelFromClasspath("/levels/level2.txt");

            // Reset về 1 bóng mới trên paddle
            balls.clear();
            Ball newBall = new Ball(
                    paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_RADIUS,
                    paddle.getY() - GameConfig.BALL_RADIUS * 2,
                    GameConfig.BALL_RADIUS,
                    0,
                    0
            );
            newBall.stickTo(paddle);
            balls.add(newBall);
        }
    }


    public void render(GraphicsContext gc) {
        for (Ball ball : balls) {
            ball.render(gc);
        }
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


    // getter cho balls để MultiBallPowerUp truy cập
    public List<Ball> getBalls() {
        return balls;
    }
}
