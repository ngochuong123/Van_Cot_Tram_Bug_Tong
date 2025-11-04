package vn.uet.oop.arkanoid.core;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.*;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import vn.uet.oop.arkanoid.systems.PhysicsSystem;
import vn.uet.oop.arkanoid.model.bricks.ResourceLevelLoader;
import vn.uet.oop.arkanoid.model.bricks.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import vn.uet.oop.arkanoid.systems.PowerUpSystem;
import vn.uet.oop.arkanoid.ui.HUD;

public class GameManager {
    private static GameManager instance;

    // Core game objects
    private final List<Ball> balls;
    private final Paddle paddle;
    private List<Brick> bricks;
    private final List<PowerUp> powerUps;
    private final PhysicsSystem physicsSystem;
    private final PowerUpSystem powerUpSystem;

    // Game state
    private HUD hud;
    private int score;
    private int currentLevel = 1;
    private boolean gameOver = false;
    private boolean levelCompleted = false;

    // Optimization: reuse collections to avoid GC
    private final List<Ball> ballsToRemove = new ArrayList<>();
    private final List<Brick> bricksToRemove = new ArrayList<>();
    private final List<PowerUp> powerUpsToRemove = new ArrayList<>();

    public GameManager() {
        this.balls = new ArrayList<>();
        this.paddle = Paddle.createPaddle();
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.physicsSystem = new PhysicsSystem();
        this.powerUpSystem = new PowerUpSystem(powerUps, paddle, balls);
        initGame();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public GameManager(HUD hud) {
        this();
        this.hud = hud;
        this.score = 0;
    }

    private void initGame() {
        // Create main ball
        Ball mainBall = Ball.createBall(GameConfig.SCREEN_WIDTH / 2, GameConfig.SCREEN_HEIGHT / 2);
        balls.add(mainBall);
        mainBall.stickTo(paddle);
        loadLevelFromClasspath("/levels/level2.txt");
    }

    // handle launch ball
    public void launchBall() {
        if (!balls.isEmpty() && !balls.get(0).isLaunched()) {
            balls.get(0).launch();
        }
    }
    public void update(double deltaTime, boolean leftPressed, boolean rightPressed) {
        if (gameOver) {
            return;
        }

        paddle.update(deltaTime, leftPressed, rightPressed);

        if (balls.size() == 1 && !balls.get(0).isLaunched()) {
            balls.get(0).stickTo(paddle);
            return;
        }

        int bricksBefore = bricks.size();

        updateBalls(deltaTime);
        for (Brick b : bricks) {
            if (b != null) b.update(deltaTime);
        }
        updatePowerUps(deltaTime);
        cleanupObjects();
        calculateScore(bricksBefore);
        checkLevelCompletion();
    }

    private void updateBalls(double deltaTime) {
        if (balls.isEmpty()) return;

        // Cập nhật chuyển động và va chạm cho từng bóng
        for (Ball ball : balls) {
            updateSingleBall(ball, deltaTime);
        }

        // Sau khi cập nhật, kiểm tra nếu tất cả bóng đã rơi khỏi màn hình
        checkBallsOutOfScreen();
    }

    /**
     * Kiểm tra nếu tất cả bóng đều rơi khỏi màn hình
     * -> Trừ mạng và reset bóng
     */
    private void checkBallsOutOfScreen() {
        // Nếu đang có khiên thì không trừ mạng
        if (paddle.isHasShield()) return;

        boolean allBallsOut = true;
        for (Ball ball : balls) {
            if (!ball.isOutOfScreen()) {
                allBallsOut = false;
                break;
            }
        }

        if (allBallsOut) {
            LoseLife();
            for (Ball ball : balls) {
                ball.setOutOfScreen(false);
            }
        }


    }
    private void LoseLife() {
        if (hud != null) {
            hud.loseLife();
            System.out.println("Life lost! Hearts remaining: " + hud.getHeartCount());
            if (hud.getHeartCount() <= 0) {
                handleGameOver();
                return;
            }
        }

        resetBall();
    }

    private void updateSingleBall(Ball ball, double deltaTime) {
        ball.update(deltaTime);
        physicsSystem.bounceBallOnWalls(ball, paddle);
        physicsSystem.bounceBallOnPaddle(ball, paddle);
        Brick hitBrick = physicsSystem.bounceBallOnBricks(ball, bricks);

        // Spawn powerup theo viên vừa chạm (nếu sau xử lý vẫn còn hợp lệ)
        powerUpSystem.spawnPowerUps(hitBrick);
    }

    private void updatePowerUps(double deltaTime) {
        powerUpSystem.updatePowerUps(deltaTime);
        powerUpSystem.checkAndApply();
    }

    // TÍNH ĐIỂM: dựa vào số gạch bị remove thực sự trong frame (Explosive/Chain cũng tính đúng)
    private void calculateScore(int bricksBefore) {
        int bricksDestroyed = bricksBefore - bricks.size();
        if (bricksDestroyed > 0 && hud != null) {
            for (int i = 0; i < bricksDestroyed; i++) {
                hud.updateScore();
                score += GameConfig.addscore;
            }
        }
    }

    private void checkLevelCompletion() {
        if (!levelCompleted && bricks.isEmpty()) {
            handleLevelComplete();
        }
    }

    private void handleLevelComplete() {
        levelCompleted = true;
        currentLevel++;
        // System.out.println("Level " + (currentLevel - 1) + " completed! Loading level " + currentLevel);

        loadNextLevel();
        resetBall();

        resetPowerUp();
        levelCompleted = false; // sẵn sàng cho level mới
    }

    public void loadLevelFromClasspath(String resourcePath) {
        try {
            bricks = ResourceLevelLoader.loadFromResource(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNextLevel() {
        // Đổi sang đường dẫn đúng: /levels/...
        if (currentLevel == 2) {
            loadLevelFromClasspath("/levels/level2.txt");
        } else {
            currentLevel = 1;
            loadLevelFromClasspath("/levels/level1.txt");
        }
    }

    private void handleGameOver() {
        gameOver = true;
        System.out.println("GAME OVER! Final Score: " + score);
    }

    private void resetBall() {
        balls.clear();
        Ball newBall = Ball.createBall(
                paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_RADIUS,
                paddle.getY() - GameConfig.BALL_RADIUS * 2
        );
        newBall.stickTo(paddle);
        balls.add(newBall);
    }

    private void resetPowerUp() {
        powerUps.clear();
    }

    private void cleanupObjects() {
        bricksToRemove.clear();
        powerUpsToRemove.clear();
    }

    public void render(GraphicsContext gc) {
        renderBalls(gc);
        renderPaddle(gc);
        renderBricks(gc);
        renderPowerUps(gc);
        renderShield(gc);
        renderGameOver(gc);
    }

    private void renderBalls(GraphicsContext gc) {
        for (Ball ball : balls) {
            ball.render(gc);
        }
    }

    private void renderPaddle(GraphicsContext gc) {
        paddle.render(gc);
    }

    private void renderBricks(GraphicsContext gc) {
        for (Brick brick : bricks) {
            if (brick != null) brick.render(gc);
        }
    }

    private void renderPowerUps(GraphicsContext gc) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp != null) powerUp.render(gc);
        }
    }

    private void renderShield(GraphicsContext gc) {
        if (paddle.isHasShield()) {
            gc.setFill(Color.CYAN);
            gc.fillRect(0, GameConfig.SCREEN_HEIGHT - 5, GameConfig.SCREEN_WIDTH, 5);
        }
    }

    private void renderGameOver(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.setFont(new javafx.scene.text.Font(48));
            gc.fillText("GAME OVER", GameConfig.SCREEN_WIDTH / 2 - 120, GameConfig.SCREEN_HEIGHT / 2);

            gc.setFont(new javafx.scene.text.Font(24));
            gc.fillText("Final Score: " + score, GameConfig.SCREEN_WIDTH / 2 - 80, GameConfig.SCREEN_HEIGHT / 2 + 40);
        }
    }

    // Getter methods
    public List<Ball> getBalls() { return balls; }
    public List<Brick> getBricks() { return bricks; }
    public int getScore() { return score; }
    public int getCurrentLevel() { return currentLevel; }
    public boolean isGameOver() { return gameOver; }
    public int getBricksCount() { return bricks.size(); }
    public Paddle getPaddle() { return paddle; }
}
