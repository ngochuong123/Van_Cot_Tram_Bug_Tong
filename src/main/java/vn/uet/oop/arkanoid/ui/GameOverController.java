package vn.uet.oop.arkanoid.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import vn.uet.oop.arkanoid.core.SceneRouter;

public class GameOverController {
    private Stage gameOverStage;
    private SceneRouter router; // THÊM SceneRouter reference
    private Stage parentStage;

    // CONSTRUCTOR MỚI - nhận SceneRouter
    public GameOverController(Stage parentStage, int finalScore, SceneRouter router) {
        this.parentStage = parentStage;
        this.router = router;
        createGameOverMenu(parentStage, finalScore);
    }

    // CONSTRUCTOR CŨ (tương thích)
    public GameOverController(Stage parentStage, int finalScore) {
        this(parentStage, finalScore, null);
    }

    private void createGameOverMenu(Stage parentStage, int finalScore) {
        gameOverStage = new Stage();
        gameOverStage.initOwner(parentStage);
        gameOverStage.initModality(Modality.APPLICATION_MODAL);
        gameOverStage.initStyle(StageStyle.TRANSPARENT);
        gameOverStage.setResizable(false);

        // Title
        Label titleLabel = new Label("GAME OVER");
        titleLabel.setFont(Font.font("Arial", 48));
        titleLabel.setTextFill(Color.RED);

        // Score
        Label scoreLabel = new Label("Final Score: " + finalScore);
        scoreLabel.setFont(Font.font("Arial", 24));
        scoreLabel.setTextFill(Color.WHITE);

        // High Score (✅ THÊM - nếu có)
        Label highScoreLabel = new Label("High Score: " + getHighScore(finalScore));
        highScoreLabel.setFont(Font.font("Arial", 20));
        highScoreLabel.setTextFill(Color.GOLD);

        // Buttons
        Button restartButton = createMenuButton("Play Again", 200, 50);
        Button menuButton = createMenuButton("Main Menu", 200, 50);
        Button quitButton = createMenuButton("Quit Game", 200, 50); // ✅ THÊM NÚT QUIT

        // ✅ SỬA: Button actions với SceneRouter
        restartButton.setOnAction(e -> {
            System.out.println("🔄 Play Again requested - Score: " + finalScore);
            if (router != null) {
                router.startNewGame(); // ✅ Dùng router
            }
            gameOverStage.close();
        });

        menuButton.setOnAction(e -> {
            System.out.println("🏠 Main Menu requested");
            if (router != null) {
                router.showMainMenu(); // ✅ Dùng router
            }
            gameOverStage.close();
        });

        quitButton.setOnAction(e -> {
            System.out.println("🚪 Quit Game requested");
            if (router != null) {
                router.exitGame(); // ✅ Dùng router
            } else {
                System.exit(0); // Fallback
            }
            gameOverStage.close();
        });

        // Layout
        VBox layout = new VBox(20);
        layout.getChildren().addAll(
                titleLabel,
                scoreLabel,
                highScoreLabel, // ✅ THÊM HIGH SCORE
                restartButton,
                menuButton,
                quitButton // ✅ THÊM QUIT BUTTON
        );
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.95); -fx-padding: 40; -fx-background-radius: 15;");

        Scene scene = new Scene(layout, 500, 450); // ✅ TĂNG chiều cao
        scene.setFill(Color.TRANSPARENT);
        gameOverStage.setScene(scene);

        // Center on parent
        gameOverStage.setOnShown(e -> {
            gameOverStage.setX(parentStage.getX() + (parentStage.getWidth() - gameOverStage.getWidth()) / 2);
            gameOverStage.setY(parentStage.getY() + (parentStage.getHeight() - gameOverStage.getHeight()) / 2);
        });
    }

    // ✅ THÊM: High score logic (có thể mở rộng sau)
    private int getHighScore(int currentScore) {
        // TODO: Load từ file/settings
        return Math.max(currentScore, 1000); // Placeholder
    }

    private Button createMenuButton(String text, double width, double height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setFont(Font.font("Arial", 18));
        button.setTextFill(Color.WHITE);
        button.setStyle(
                "-fx-background-color: #FF3366; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #FFFFFF; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10;");

        // Hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #00BFFF; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-color: #FFFFFF; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10;");
            button.setScaleX(1.05); // THÊM hiệu ứng scale
            button.setScaleY(1.05);
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: #FF3366; " +
                            "-fx-background-radius: 10; " +
                            "-fx-border-color: #FFFFFF; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10;");
            button.setScaleX(1.0); // RESET scale
            button.setScaleY(1.0);
        });

        return button;
    }

    public void show() {
        // XOÁ reset flags
        gameOverStage.show();
        gameOverStage.requestFocus();
    }

    // Method để đóng game over menu
    public void close() {
        if (gameOverStage != null) {
            gameOverStage.close();
        }
    }

    // Update score (cho trường hợp hiển thị lại)
    public void updateScore(int newScore) {
        // Có thể cập nhật UI nếu cần
        System.out.println("🎯 GameOver score updated: " + newScore);
    }
}