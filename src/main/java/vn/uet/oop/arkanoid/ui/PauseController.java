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
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.core.SceneRouter;

public class PauseController {
    private Stage pauseStage;
    private SceneRouter router;
    private Stage parentStage;

    // CONSTRUCTOR MỚI - nhận SceneRouter
    public PauseController(Stage parentStage, SceneRouter router) {
        this.parentStage = parentStage;
        this.router = router;
        createPauseMenu(parentStage);
    }

    // CONSTRUCTOR CŨ (tương thích)
    public PauseController(Stage parentStage) {
        this(parentStage, null);
    }

    private void createPauseMenu(Stage parentStage) {
        pauseStage = new Stage();
        pauseStage.initOwner(parentStage);
        pauseStage.initStyle(StageStyle.TRANSPARENT);
        pauseStage.setResizable(false);

        // Title
        Label titleLabel = new Label("GAME PAUSED");
        titleLabel.setFont(Font.font("Arial", 36));
        titleLabel.setTextFill(Color.WHITE);

        // Buttons
        Button resumeButton = createMenuButton("Resume", 200, 50);
        Button restartButton = createMenuButton("Restart", 200, 50);

        // Button actions với SceneRouter
        resumeButton.setOnAction(e -> {
            System.out.println("▶️ Resume game requested");
            if (router != null) {
                router.resumeGame(); // ✅ Dùng router
            }
            pauseStage.close();
        });

        restartButton.setOnAction(e -> {
            System.out.println("🔄 Restart game requested");
            if (router != null) {
                router.startNewGame(); // ✅ Dùng router
            }
            pauseStage.close();
        });


        // Layout
        VBox layout = new VBox(15);
        layout.getChildren().addAll(
                titleLabel,
                resumeButton,
                restartButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9); -fx-padding: 40; -fx-background-radius: 15;");

        Scene scene = new Scene(layout, 400, 450); // ✅ TĂNG chiều cao
        scene.setFill(Color.TRANSPARENT);
        pauseStage.setScene(scene);

        // Center on parent
        pauseStage.setOnShown(e -> {
            pauseStage.setX(parentStage.getX() + (parentStage.getWidth() - pauseStage.getWidth()) / 2);
            pauseStage.setY(parentStage.getY() + (parentStage.getHeight() - pauseStage.getHeight()) / 2);
        });
        pauseStage.setAlwaysOnTop(true);
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
        System.out.println("🎯 Showing pause menu - Non blocking");
        // XOÁ reset flags
        pauseStage.show();

        // Đảm bảo focus vào pause menu
        pauseStage.requestFocus();
    }

    // Method để đóng pause menu (cho SceneRouter)
    public void close() {
        if (pauseStage != null) {
            pauseStage.close();
        }
    }

    // Check nếu pause menu đang hiển thị
    public boolean isShowing() {
        return pauseStage != null && pauseStage.isShowing();
    }
}