package vn.uet.oop.arkanoid.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.core.GameManager;
import vn.uet.oop.arkanoid.core.GameState;
import vn.uet.oop.arkanoid.model.*;
import vn.uet.oop.arkanoid.model.bricks.Brick;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;

import java.util.List;

/**
 * Class chuyên trách việc render tất cả các thành phần của game
 */
public class GameRenderer {
    private final GameManager gameManager;

    public GameRenderer(GameManager gameManager, HUD hud) {
        this.gameManager = gameManager;
        // HUD được render riêng qua JavaFX nodes, không cần tham chiếu ở đây
    }

    public void render(GraphicsContext gc) {
        // Clear canvas
        clearCanvas(gc);

        // Chỉ render khi đang ở trạng thái gameplay
        GameState currentState = gameManager.getCurrentState();

        if (currentState == GameState.PLAYING || currentState == GameState.LEVEL_COMPLETE) {
            renderGameplay(gc);

            // Thêm overlay level complete nếu cần
            if (currentState == GameState.LEVEL_COMPLETE) {
                renderLevelCompleteOverlay(gc);
            }
        }
        // Các trạng thái khác (MENU, PAUSED, GAME_OVER) được xử lý bởi các Controller
        // riêng
        // GameRenderer không cần render gì cả
    }

    /**
     * Render tất cả các thành phần gameplay
     */
    private void renderGameplay(GraphicsContext gc) {
        renderBackground(gc);
        renderBricks(gc);
        renderPaddle(gc);
        renderBalls(gc);
        renderPowerUps(gc);
    }

    /**
     * Xóa canvas với màu nền
     */
    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    }

    /**
     * Render background đơn giản
     */
    private void renderBackground(GraphicsContext gc) {
        // Màu nền xanh đậm
        gc.setFill(Color.rgb(25, 25, 50));
        gc.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // Grid nhẹ để tạo chiều sâu
        gc.setStroke(Color.rgb(255, 255, 255, 0.1));
        gc.setLineWidth(0.5);

        // Vẽ grid
        for (int x = 0; x < GameConfig.SCREEN_WIDTH; x += 40) {
            gc.strokeLine(x, 0, x, GameConfig.SCREEN_HEIGHT);
        }
        for (int y = 0; y < GameConfig.SCREEN_HEIGHT; y += 40) {
            gc.strokeLine(0, y, GameConfig.SCREEN_WIDTH, y);
        }
    }

    /**
     * Render tất cả bricks - GỌI render() CỦA TỪNG BRICK
     */
    private void renderBricks(GraphicsContext gc) {
        List<Brick> bricks = gameManager.getBricks();
        if (bricks != null) {
            for (Brick brick : bricks) {
                if (brick != null) {
                    brick.render(gc); // Gọi render() của brick
                }
            }
        }
    }

    /**
     * Render paddle - GỌI render() CỦA PADDLE
     */
    private void renderPaddle(GraphicsContext gc) {
        Paddle paddle = gameManager.getPaddle();
        if (paddle != null) {
            paddle.render(gc); // Gọi render() của paddle
        }
    }

    /**
     * Render tất cả balls - GỌI render() CỦA TỪNG BALL
     */
    private void renderBalls(GraphicsContext gc) {
        List<Ball> balls = gameManager.getBalls();
        if (balls != null) {
            for (Ball ball : balls) {
                if (ball != null) {
                    ball.render(gc); // Gọi render() của ball
                }
            }
        }
    }

    /**
     * Render power-ups
     */
    private void renderPowerUps(GraphicsContext gc) {
        List<PowerUp> powerUps = gameManager.getPowerUps();
        if (powerUps != null) {
            for (PowerUp powerUp : powerUps) {
                if (powerUp != null && powerUp.isActive()) {
                    powerUp.render(gc); // Gọi render() của power-up
                }
            }
        }
    }

    /**
     * Render level complete overlay (đơn giản, tự động ẩn sau vài giây)
     */
    private void renderLevelCompleteOverlay(GraphicsContext gc) {
        // Background overlay nhẹ
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // Level complete text
        gc.setFill(Color.GREEN);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        String completeText = "LEVEL " + (gameManager.getCurrentLevel() - 1) + " COMPLETE!";
        double textWidth = calculateTextWidth(completeText, gc.getFont());
        gc.fillText(completeText,
                (GameConfig.SCREEN_WIDTH - textWidth) / 2,
                GameConfig.SCREEN_HEIGHT / 2);

        // Continue instruction
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        String continueText = "Loading next level...";
        double continueWidth = calculateTextWidth(continueText, gc.getFont());
        gc.fillText(continueText,
                (GameConfig.SCREEN_WIDTH - continueWidth) / 2,
                GameConfig.SCREEN_HEIGHT / 2 + 40);
    }

    /**
     * Tính toán chiều rộng của text (ước lượng)
     */
    private double calculateTextWidth(String text, Font font) {
        return text.length() * font.getSize() * 0.6;
    }

    /**
     * Cleanup resources
     */
    public void cleanup() {
        // Cleanup nếu cần
    }
}