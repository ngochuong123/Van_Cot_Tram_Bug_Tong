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
    private static GameManager instance = null;

    private GameState currentState = GameState.MENU;

    private boolean paused = false;
    private boolean gameOver = false;

    // Core game objects
    private final List<Ball> balls;
    private final Paddle paddle;
    private List<Brick> bricks;
    private final List<PowerUp> powerUps;
    private final PhysicsSystem physicsSystem;
    private final PowerUpSystem powerUpSystem;

    // Game state
    private int score = 0;
    private int lives = 5;
    private int currentLevel = 1;
    private boolean levelCompleted = false;

    // Tối ưu hóa: tái sử dụng các collection để tránh GC
    private final List<Ball> ballsToRemove = new ArrayList<>();
    private final List<Brick> bricksToRemove = new ArrayList<>();
    private final List<PowerUp> powerUpsToRemove = new ArrayList<>();

    // Khởi tạo GameManager
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

    private void initGame() {
        Ball mainBall = Ball.createBall(GameConfig.SCREEN_WIDTH / 2, GameConfig.SCREEN_HEIGHT / 2);
        balls.add(mainBall);
        mainBall.stickTo(paddle);
        loadLevelFromClasspath("/levels/level2.txt"); // Bắt đầu từ level 1
    }

    public void launchBall() {
        if (!balls.isEmpty() && !balls.get(0).isLaunched()) {
            balls.get(0).launch();
        }
    }

    public void update(double deltaTime, boolean leftPressed, boolean rightPressed) {
        if (currentState != GameState.PLAYING) {
            return;
        }

        paddle.update(deltaTime, leftPressed, rightPressed);

        // Giữ bóng trên paddle nếu chưa launch
        if (balls.size() == 1 && !balls.get(0).isLaunched()) {
            balls.get(0).stickTo(paddle);
        }

        int bricksBefore = bricks.size();
        updateBalls(deltaTime);

        // Cập nhật tất cả bricks
        for (Brick brick : bricks) {
            if (brick != null) {
                brick.update(deltaTime);
            }
        }

        updatePowerUps(deltaTime);
        cleanupObjects();
        calculateScore(bricksBefore);

        // Kiểm tra hoàn thành level
        checkAndHandleLevelCompletion();
        checkStateTransitions();
    }

    private void checkStateTransitions() {
        if (currentState == GameState.PLAYING) {
            if (!isAlive()) {
                setState(GameState.GAME_OVER);
            }
        }
    }

    public void setState(GameState newState) {
        GameState oldState = this.currentState;
        this.currentState = newState;
        onStateChange(oldState, newState);
    }

    private void onStateChange(GameState oldState, GameState newState) {
        System.out.println("🔄 GameState changed: " + oldState + " → " + newState);
    }

    private void updateBalls(double deltaTime) {
        if (balls.isEmpty()) return;

        for (Ball ball : balls) {
            updateSingleBall(ball, deltaTime);
        }

        checkBallsOutOfScreen();
    }

    private void checkBallsOutOfScreen() {
        if (paddle.isHasShield()) return;

        boolean allBallsOut = true;
        for (Ball ball : balls) {
            if (!ball.isOutOfScreen()) {
                allBallsOut = false;
                break;
            }
        }

        if (allBallsOut) {
            loseLife();
            for (Ball ball : balls) {
                ball.setOutOfScreen(false);
            }
        }
    }

    private void loseLife() {
        this.lives--;
        System.out.println("💔 Lost a life! Remaining: " + this.lives);

        if (this.lives <= 0) {
            this.gameOver = true;
            setState(GameState.GAME_OVER);
            System.out.println("GAME OVER! Final Score: " + score);
        } else {
            resetBall();
        }
    }

    private void updateSingleBall(Ball ball, double deltaTime) {
        ball.update(deltaTime);
        physicsSystem.bounceBallOnWalls(ball, paddle);
        physicsSystem.bounceBallOnPaddle(ball, paddle);
        Brick hitBrick = physicsSystem.bounceBallOnBricks(ball, bricks);
        powerUpSystem.spawnPowerUps(hitBrick);
    }

    private void updatePowerUps(double deltaTime) {
        powerUpSystem.updatePowerUps(deltaTime);
        powerUpSystem.checkAndApply();
    }

    private void calculateScore(int bricksBefore) {
        int bricksDestroyed = bricksBefore - bricks.size();
        if (bricksDestroyed > 0) {
            int pointsEarned = bricksDestroyed * GameConfig.addscore;
            this.score += pointsEarned;
            System.out.println("🎯 Destroyed " + bricksDestroyed + " bricks! +" + pointsEarned + " points");
        }
    }

    /**
     * Kiểm tra level có hoàn thành không
     * - UnbreakableBrick: không cần phá
     * - RegeneratingBrick: phải đang bị phá (isBroken = true)
     * - Các brick khác: không được tồn tại
     */
    private boolean checkLevelComplete() {
        for (Brick brick : bricks) {
            if (brick instanceof UnbreakableBrick) {
                continue;
            }

            if (brick instanceof RegeneratingBrick) {
                RegeneratingBrick regenBrick = (RegeneratingBrick) brick;
                if (!regenBrick.isBroken()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Kiểm tra và xử lý hoàn thành level
     */
    private void checkAndHandleLevelCompletion() {
        if (levelCompleted) return;

        if (checkLevelComplete()) {
            levelCompleted = true;
            currentLevel++;
            System.out.println("🎉 Level " + (currentLevel - 1) + " completed!");
            setState(GameState.LEVEL_COMPLETE);
        }
    }

    /**
     * Load level tiếp theo và reset trạng thái
     */
    public void loadNextLevel() {
        System.out.println("🔄 Loading Level " + currentLevel);

        resetBall();
        resetPowerUp();

        String levelPath = "/levels/level" + currentLevel + ".txt";
        loadLevelFromClasspath(levelPath);

        levelCompleted = false;
        setState(GameState.PLAYING);
    }

    public void loadLevelFromClasspath(String resourcePath) {
        try {
            bricks = ResourceLevelLoader.loadFromResource(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetBall() {
        balls.clear();
        Ball newBall = Ball.createBall(
                paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_RADIUS,
                paddle.getY() - GameConfig.BALL_RADIUS * 2);
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

    public void resetGame() {
        this.balls.clear();
        this.bricks.clear();
        this.powerUps.clear();
        this.score = 0;
        this.lives = 5;
        this.currentLevel = 1;
        this.levelCompleted = false;
        this.paused = false;
        this.gameOver = false;

        initGame();
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

    public int getBricksCount() {
        return bricks.size();
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public int getLives() {
        return lives;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public boolean isLevelCompleteState() {
        return currentState == GameState.LEVEL_COMPLETE;
    }
}
