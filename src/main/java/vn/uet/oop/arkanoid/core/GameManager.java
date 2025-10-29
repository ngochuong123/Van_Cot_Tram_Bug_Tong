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
    private boolean showRestartMessage = false; // Thêm flag hiển thị thông báo chơi lại

    // Optimization: reuse collections to avoid GC
    private final List<Ball> ballsToRemove = new ArrayList<>();
    private final List<Brick> bricksToRemove = new ArrayList<>();
    private final List<PowerUp> powerUpsToRemove = new ArrayList<>();

    public GameManager() {
        this.balls = new ArrayList<>();
        this.paddle = createPaddle();
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

    private Paddle createPaddle() {
        return new Paddle(
                (GameConfig.SCREEN_WIDTH - GameConfig.PADDLE_WIDTH) / 2,
                GameConfig.SCREEN_HEIGHT - 40,
                GameConfig.PADDLE_WIDTH,
                GameConfig.PADDLE_HEIGHT,
                GameConfig.PADDLE_SPEED);
    }

    private void initGame() {
        // Create main ball
        Ball mainBall = createBall(GameConfig.SCREEN_WIDTH / 2, GameConfig.SCREEN_HEIGHT / 2);
        balls.add(mainBall);

        // Load first level
        loadLevel(vn.uet.oop.arkanoid.config.Levels.LEVEL_1);
        mainBall.stickTo(paddle);
    }

    private Ball createBall(double x, double y) {
        return new Ball(x, y, GameConfig.BALL_RADIUS, 0, 0);
    }

    public void launchBall() {
        if (!balls.isEmpty() && !balls.get(0).isLaunched()) {
            balls.get(0).launch();
        }
    }

    /**
     * Xử lý sự kiện phím - THÊM PHƯƠNG THỨC MỚI
     * Khi game over, ấn phím cách để chơi lại
     */
    public void handleKeyPress(String keyCode) {
        if (gameOver && "SPACE".equals(keyCode)) {
            restartGame();
        }
    }

    /**
     * Chơi lại game - THÊM PHƯƠNG THỨC MỚI
     */
    public void restartGame() {
        // Reset game state
        gameOver = false;
        levelCompleted = false;
        showRestartMessage = false;
        currentLevel = 1;
        score = 0;

        // Clear all game objects
        balls.clear();
        bricks.clear();
        powerUps.clear();
        ballsToRemove.clear();
        bricksToRemove.clear();
        powerUpsToRemove.clear();

        // Reset paddle position
        paddle.setX((GameConfig.SCREEN_WIDTH - GameConfig.PADDLE_WIDTH) / 2);
        paddle.setY(GameConfig.SCREEN_HEIGHT - 40);

        // Reinitialize game
        initGame();

        System.out.println("Game restarted by SPACE key!");
    }

    private void loadLevel(int[][] pattern) {
        bricks = new ArrayList<>();
        levelCompleted = false;

        int rows = pattern.length;
        int cols = pattern[0].length;

        double totalWidth = cols * GameConfig.BRICK_WIDTH + (cols - 1) * GameConfig.BRICK_SPACING;
        double totalHeight = rows * GameConfig.BRICK_HEIGHT + (rows - 1) * GameConfig.BRICK_SPACING;

        double startX = (GameConfig.SCREEN_WIDTH - totalWidth) / 2.0;
        double startY = 50;

        // Pre-calculate positions to avoid repeated calculations
        double brickStepX = GameConfig.BRICK_WIDTH + GameConfig.BRICK_SPACING;
        double brickStepY = GameConfig.BRICK_HEIGHT + GameConfig.BRICK_SPACING;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int code = pattern[row][col];
                BrickType type = toBrickType(code);
                if (type == null)
                    continue;

                double x = startX + col * brickStepX;
                double y = startY + row * brickStepY;

                int durability = switch (type) {
                    case NORMAL -> 1;
                    case STRONG -> 2;
                    case UNBREAKABLE -> -1; // Không thể phá hủy
                    case REGENERATING -> 2;
                    case INVISIBLE -> 1;
                    case EXPLOSIVE -> 1;
                    case CHAIN -> 1;
                    default -> 1;
                };

                Brick brick = BrickFactory.create(type, x, y,
                        GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT, durability, null);
                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }

        System.out.println("Level " + currentLevel + " loaded with " + bricks.size() + " bricks");
    }

    private BrickType toBrickType(int code) {
        return switch (code) {
            case 1 -> BrickType.NORMAL;
            case 2 -> BrickType.STRONG;
            case 10 -> BrickType.UNBREAKABLE;
            // Thêm các code khác nếu cần
            default -> null;
        };
    }

    public void update(double deltaTime, boolean leftPressed, boolean rightPressed) {
        if (gameOver)
            return;

        updatePaddle(deltaTime, leftPressed, rightPressed);

        if (isAllBallsStuck()) {
            balls.get(0).stickTo(paddle);
            return;
        }

        // LƯU SỐ GẠCH TRƯỚC KHI UPDATE để tính điểm
        int bricksBefore = bricks.size();

        updateBalls(deltaTime);
        updatePowerUps(deltaTime);
        cleanupObjects();

        // TÍNH ĐIỂM SAU KHI UPDATE BALLS
        calculateScore(bricksBefore);
        checkLevelCompletion();
    }

    private void updatePaddle(double deltaTime, boolean leftPressed, boolean rightPressed) {
        paddle.update(deltaTime, leftPressed, rightPressed);
    }

    private boolean isAllBallsStuck() {
        if (balls.isEmpty())
            return false;

        for (Ball ball : balls) {
            if (ball.isLaunched()) {
                return false;
            }
        }
        return true;
    }

    private void updateBalls(double deltaTime) {
        ballsToRemove.clear();

        // Use iterator for safe removal during iteration
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            updateSingleBall(ball, deltaTime);

            if (ball.getY() + ball.getRadius() > GameConfig.SCREEN_HEIGHT) {
                ballsToRemove.add(ball);
            }
        }

        // Remove fallen balls
        balls.removeAll(ballsToRemove);

        // Handle ball loss - TRỪ MẠNG KHI MẤT BÓNG
        if (!ballsToRemove.isEmpty()) {
            handleBallLoss();
        }

        // Reset if no balls left
        if (balls.isEmpty() && !gameOver) {
            resetBall();
        }
    }

    private void updateSingleBall(Ball ball, double deltaTime) {
        physicsSystem.updateBall(ball, deltaTime);
        physicsSystem.bounceBallOnWalls(ball, paddle);
        physicsSystem.bounceBallOnPaddle(ball, paddle);
        physicsSystem.bounceBallOnBricks(ball, bricks, powerUps);
    }

    private void updatePowerUps(double deltaTime) {
        powerUpSystem.updatePowerUps(deltaTime);
        powerUpSystem.checkAndApply();
    }

    // THÊM LẠI LOGIC TÍNH ĐIỂM
    private void calculateScore(int bricksBefore) {
        int bricksDestroyed = bricksBefore - bricks.size();
        if (bricksDestroyed > 0 && hud != null) {
            for (int i = 0; i < bricksDestroyed; i++) {
                hud.updateScore();
                score += GameConfig.addscore;
            }
            System.out.println("Destroyed " + bricksDestroyed + " bricks. Score: " + score);
        }
    }

    private void handleBallLoss() {
        if (hud != null) {
            // TRỪ MẠNG CHO MỖI BÓNG MẤT
            for (Ball ball : ballsToRemove) {
                hud.loseLife();
                System.out.println("Life lost! Hearts remaining: " + hud.getHeartCount());

                if (hud.getHeartCount() <= 0) {
                    handleGameOver();
                    break;
                }
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
        System.out.println("Level " + (currentLevel - 1) + " completed! Loading level " + currentLevel);

        // Stop current ball
        if (!balls.isEmpty()) {
            balls.get(0).setLaunched(false);
        }

        // Load next level
        loadNextLevel();
        resetBall();
    }

    private void loadNextLevel() {
        if (currentLevel == 2) {
            loadLevel(vn.uet.oop.arkanoid.config.Levels.LEVEL_2);
        } else {
            currentLevel = 1;
            loadLevel(vn.uet.oop.arkanoid.config.Levels.LEVEL_1);
        }
    }

    private void handleGameOver() {
        gameOver = true;
        showRestartMessage = true; // Hiển thị thông báo chơi lại
        System.out.println("GAME OVER! Final Score: " + score);
        System.out.println("Press SPACE to play again!");

        // Reset HUD
        if (hud != null) {
            hud.reset();
        }

    }

    private void resetBall() {
        balls.clear();
        Ball newBall = createBall(
                paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_RADIUS,
                paddle.getY() - GameConfig.BALL_RADIUS * 2);
        newBall.stickTo(paddle);
        balls.add(newBall);
        System.out.println("Ball reset to paddle");
    }

    private void cleanupObjects() {
        // Clear temporary lists for next frame
        bricksToRemove.clear();
        powerUpsToRemove.clear();
    }

    public void render(GraphicsContext gc) {
        // Render in optimal order
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
            if (brick != null)
                brick.render(gc);
        }
    }

    private void renderPowerUps(GraphicsContext gc) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp != null)
                powerUp.render(gc);
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

            // THÊM THÔNG BÁO ẤN PHÍM CÁCH ĐỂ CHƠI LẠI
            if (showRestartMessage) {
                gc.setFill(javafx.scene.paint.Color.BLACK);
                gc.setFont(new javafx.scene.text.Font(20));
                gc.fillText("Press SPACE to play again", GameConfig.SCREEN_WIDTH / 2 - 100,
                        GameConfig.SCREEN_HEIGHT / 2 + 80);
            }
        }
    }

    // Getter methods
    public List<Ball> getBalls() {
        return balls;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public int getScore() {
        return score;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getBricksCount() {
        return bricks.size();
    }

    public Paddle getPaddle() {
        return paddle;
    }
}