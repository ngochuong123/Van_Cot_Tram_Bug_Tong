package vn.uet.oop.arkanoid.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.core.GameManager;
import vn.uet.oop.arkanoid.core.GameState;
import vn.uet.oop.arkanoid.model.*;
import vn.uet.oop.arkanoid.model.bricks.Brick;
import vn.uet.oop.arkanoid.model.powerups.PowerUp;
import vn.uet.oop.arkanoid.model.Paddle;
import java.util.List;


public class GameRenderer {
    private final GameManager gameManager;
    
    public GameRenderer(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void render(GraphicsContext gc) {
        clearCanvas(gc);
        GameState currentState = gameManager.getCurrentState();
        if (currentState == GameState.PLAYING || currentState == GameState.LEVEL_COMPLETE) {
            renderGameplay(gc);
            if (currentState == GameState.LEVEL_COMPLETE) {
                renderLevelCompleteOverlay(gc);
            }
        }
    }

    /**
     * Render all gameplay elements
     */
    private void renderGameplay(GraphicsContext gc) {
        renderBackground(gc);
        renderBricks(gc);
        renderPaddle(gc);
        renderBalls(gc);
        renderPowerUps(gc);
    }

    /**
     * reset canvas
     */
    private void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    }

    /**
     * Render background
     */
    private void renderBackground(GraphicsContext gc) {
        Image bgImage = new Image(getClass().getResourceAsStream("/image/Background_game.png"));
        gc.drawImage(bgImage, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

    }

    /**
     * Render bricks
     */
    private void renderBricks(GraphicsContext gc) {
        List<Brick> bricks = gameManager.getBricks();
        if (bricks != null) {
            for (Brick brick : bricks) {
                if (brick != null) {
                    brick.render(gc);
                }
            }
        }
    }

    /**
     * Render paddle
     */
    private void renderPaddle(GraphicsContext gc) {
        Paddle paddle = gameManager.getPaddle();
        if (paddle != null) {
            paddle.render(gc);
        }
        if (paddle.isHasShield()) {
            gc.setFill(Color.CYAN);
            gc.fillRect(0, GameConfig.SCREEN_HEIGHT - 5, GameConfig.SCREEN_WIDTH, 5);
        }
    }

    /**
     * Render all balls
     */
    private void renderBalls(GraphicsContext gc) {
        List<Ball> balls = gameManager.getBalls();
        if (balls != null) {
            for (Ball ball : balls) {
                if (ball != null) {
                    ball.render(gc);
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
     * Render level complete overlay
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
     * calculate text width
     */
    private double calculateTextWidth(String text, Font font) {
        return text.length() * font.getSize() * 0.6;
    }

    /**
     * Cleanup resources
     */
    public void cleanup() {
    }
}