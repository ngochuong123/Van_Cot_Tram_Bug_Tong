package vn.uet.oop.arkanoid.core;

import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.model.*;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import vn.uet.oop.arkanoid.systems.PhysicsSystem;
import vn.uet.oop.arkanoid.model.bricks.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import vn.uet.oop.arkanoid.systems.PowerUpSystem;

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
        // Create main ball - Tạo bóng chính ở giữa màn hình
        Ball mainBall = Ball.createBall(GameConfig.SCREEN_WIDTH / 2, GameConfig.SCREEN_HEIGHT / 2);
        balls.add(mainBall); // Thêm vào danh sách
        mainBall.stickTo(paddle); // Gắn bóng vào paddle
        loadLevelFromClasspath("/levels/level2.txt"); // Load level 2
    }

    // handle launch ball
    public void launchBall() {
        if (!balls.isEmpty() && !balls.get(0).isLaunched()) {
            balls.get(0).launch();
        }
    }
    public void update(double deltaTime, boolean leftPressed, boolean rightPressed) {
        // Nếu game đang paused hoặc game over, không update
        if (currentState != GameState.PLAYING) {
            return;
        }
        paddle.update(deltaTime, leftPressed, rightPressed); // Di chuyển paddle
        // Nếu chỉ có 1 bóng và chưa launch -> giữ bóng trên paddle
        if (balls.size() == 1 && !balls.get(0).isLaunched()) {
            balls.get(0).stickTo(paddle);
        }
        int bricksBefore = bricks.size(); // Đếm gạch trước khi update
        updateBalls(deltaTime); // Cập nhật bóng
        for (Brick b : bricks) { // Cập nhật gạch
            if (b != null)
                b.update(deltaTime);
        }
        updatePowerUps(deltaTime); // Cập nhật power-up
        cleanupObjects(); // Dọn dẹp object
        calculateScore(bricksBefore); // Tính điểm
        checkLevelCompletion(); // Kiểm tra hoàn thành level
        checkStateTransitions(); // Kiểm tra chuyển trạng thái
    }

    private void checkStateTransitions() {
        if (currentState == GameState.PLAYING) {
            if (!isAlive()) {
                setState(GameState.GAME_OVER);
            } else if (bricks.isEmpty()) {
                setState(GameState.LEVEL_COMPLETE);
            }
        }
    }

    public void setState(GameState newState) {
        GameState oldState = this.currentState;
        this.currentState = newState;
        // Thông báo state change cho các hệ thống khác
        onStateChange(oldState, newState);
    }

    private void onStateChange(GameState oldState, GameState newState) {
        System.out.println("🔄 GameState changed: " + oldState + " → " + newState);
        // Có thể thêm logic xử lý khi state thay đổi
        // Ví dụ: pause/resume audio, stop/start animations, etc.
    }

    private void updateBalls(double deltaTime) {
        ballsToRemove.clear(); // Reset danh sách bóng cần xóa
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            updateSingleBall(ball, deltaTime); // Cập nhật từng bóng
            // Kiểm tra bóng rơi khỏi màn hình
            if (ball.getY() + ball.getRadius() > GameConfig.SCREEN_HEIGHT) {
                ballsToRemove.add(ball); // Đánh dấu để xóa
            }
        }
        balls.removeAll(ballsToRemove); // Xóa bóng đã rơi
        // Xử lý mất bóng
        if (!ballsToRemove.isEmpty()) {
            handleBallLoss(); // Trừ mạng
        }
        // Reset nếu hết bóng
        if (balls.isEmpty() && lives > 0) {
            resetBall(); // Tạo bóng mới
        }

        resetBall();
    }

    private void updateSingleBall(Ball ball, double deltaTime) {
        ball.update(deltaTime); // Di chuyển bóng
        physicsSystem.bounceBallOnWalls(ball, paddle); // Nảy tường
        physicsSystem.bounceBallOnPaddle(ball, paddle); // Nảy paddle
        Brick hitBrick = physicsSystem.bounceBallOnBricks(ball, bricks); // Nảy gạch
        // Spawn powerup nếu phá gạch
        powerUpSystem.spawnPowerUps(hitBrick);
    }

    private void updatePowerUps(double deltaTime) {
        powerUpSystem.updatePowerUps(deltaTime);
        powerUpSystem.checkAndApply();
    }

    // TÍNH ĐIỂM: dựa vào số gạch bị remove thực sự trong frame (Explosive/Chain
    // cũng tính đúng)
    private void calculateScore(int bricksBefore) {
        int bricksDestroyed = bricksBefore - bricks.size();
        if (bricksDestroyed > 0) {
            // CHỈ tính điểm, không gọi HUD
            int pointsEarned = bricksDestroyed * GameConfig.addscore;
            this.score += pointsEarned;
            System.out.println("🎯 Destroyed " + bricksDestroyed + " bricks! +" + pointsEarned + " points");
        }
    }

    private void handleBallLoss() {
        if (!ballsToRemove.isEmpty() && balls.isEmpty()) {
            loseLife(); // Chỉ mất 1 mạng khi bóng roi hết
            System.out.println(ballsToRemove.size() + " balls lost! -1 life");
        } else if (!ballsToRemove.isEmpty()) {
            // Vẫn còn bóng trên màn hình, chỉ thông báo
            System.out
                    .println(ballsToRemove.size() + " balls lost, but still have " + balls.size() + " balls remaining");
        }
    }

    private void loseLife() {
        this.lives--; // CHỈ thay đổi state
        System.out.println("💔 Lost a life! Remaining: " + this.lives);
        if (this.lives <= 0 && balls.isEmpty()) {
            this.gameOver = true;
            System.out.println("GAME OVER! Final Score: " + score);
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
        System.out.println("Level " + (currentLevel - 1) + " completed! Loading level" + currentLevel);
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

    public void loadNextLevel() {
        currentLevel++;
        System.out.println("🔄 Loading Level " + currentLevel);

        resetBall();
        resetPowerUp();

        if (currentLevel == 2) {
            loadLevelFromClasspath("/levels/level2.txt");
        } else {
            currentLevel = 1;
            loadLevelFromClasspath("/levels/level1.txt");
        }

        levelCompleted = false;
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
        // Reset tất cả trạng thái
        this.balls.clear();
        this.bricks.clear();
        this.powerUps.clear();
        this.score = 0;
        this.lives = 5;
        this.currentLevel = 1;
        this.levelCompleted = false;
        this.paused = false;
        this.gameOver = false;

        // Khởi tạo lại game
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

    public boolean isLevelComplete() {
        return currentState == GameState.LEVEL_COMPLETE;
    }
}