package vn.uet.oop.arkanoid.config;

public class GameConfig {
    // ----- Màn hình -----
    public static final double SCREEN_WIDTH = 694;
    public static final double SCREEN_HEIGHT = 720;

    // ----- Ball -----
    public static final double BALL_RADIUS = 8;
    public static final double BALL_SPEED = 300;

    // ----- Paddle -----
    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT = 20;
    public static final double PADDLE_SPEED = 400;

    // ----- Brick -----
    public static final int BRICK_ROWS = 7;
    public static final int BRICK_COLUMNS = 9;
    public static final double BRICK_WIDTH = 64;
    public static final double BRICK_HEIGHT = 24;
    public static final double BRICK_SPACING = 6;
    public static final double BRICK_RESPAWN_TIME = 5.0;

    // ----- GameState ----
    public static final int pauseState = 1;
    public static final int settingState = 2;
    public static final int gameOverState = 3;

    // ----- Score -----
    public static final int addscore = 100;

    // ----- PowerUp -----
    public static final int DURATION_POWERUP = 5000;
}
