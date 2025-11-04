package vn.uet.oop.arkanoid.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class HUD {
    // UI Components - chỉ làm nhiệm vụ HIỂN THỊ
    private Label scoreLabel;
    private Label levelLabel;
    private Label livesLabel;
    private Label messageLabel;
    private List<ImageView> heartIcons = new ArrayList<>();
    private HBox heartsContainer = new HBox(5);
    private VBox hudContainer = new VBox(10);
    // State - chỉ lưu để hiển thị, KHÔNG logic game
    private final Image heartImage;

    public HUD() {
        // Khởi tạo UI components
        initializeUI();
        heartImage = loadHeartImage();
    }

    private void initializeUI() {
        // Tạo score label
        scoreLabel = new Label("SCORE: 0");
        scoreLabel.setStyle(
                "-fx-font-family: 'Impact';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #7cfc00;" +
                        "-fx-effect: dropshadow(gaussian, #006400, 8, 0.8, 2, 2);");

        // Tạo level label
        levelLabel = new Label("LEVEL: 1");
        levelLabel.setStyle(
                "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 20px;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-weight: bold;");

        // Tạo lives label
        livesLabel = new Label("LIVES: ");
        livesLabel.setStyle(
                "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 20px;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-weight: bold;");

        messageLabel = new Label();
        messageLabel.setStyle(
                "-fx-font-family: 'Impact'; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #FFD700; -fx-effect: dropshadow(gaussian, #FF4500, 10, 0.8, 3, 3);");
        messageLabel.setVisible(false);
        messageLabel.setManaged(false);

        // Setup layout - gom tất cả vào một lần duy nhất
        heartsContainer.setAlignment(Pos.CENTER_LEFT);
        HBox livesBox = new HBox(10, livesLabel, heartsContainer);

        hudContainer.getChildren().addAll(messageLabel, scoreLabel, levelLabel, livesBox);
        hudContainer.setAlignment(Pos.TOP_LEFT);
        hudContainer.setStyle("-fx-padding: 20; -fx-background-color: rgba(0,0,0,0.3);");
    }

    private Image loadHeartImage() {
        try {
            return new Image(getClass().getResourceAsStream("/image/heart.png"));
        } catch (Exception e) {
            System.err.println("❌ Could not load heart image: " + e.getMessage());
            return null;
        }
    }

    // === PUBLIC METHODS - CHỈ NHẬN DATA TỪ GAMEMANAGER ===

    /**
     * Cập nhật điểm số từ GameManager
     */
    public void setScore(int score) {
        scoreLabel.setText("SCORE: " + score);
        System.out.println("📊 HUD: Score updated to " + score);
    }

    /**
     * Cập nhật số mạng từ GameManager
     */
    public void setLives(int lives) {
        updateHeartsDisplay(lives);
        System.out.println("❤️ HUD: Lives updated to " + lives);
    }

    /**
     * Cập nhật level từ GameManager
     */
    public void setLevel(int level) {
        levelLabel.setText("LEVEL: " + level);
        System.out.println("🎯 HUD: Level updated to " + level);
    }

    /**
     * Cập nhật hiển thị trái tim
     */
    private void updateHeartsDisplay(int lives) {
        heartsContainer.getChildren().clear();
        heartIcons.clear();

        if (heartImage != null) {
            for (int i = 0; i < lives; i++) {
                ImageView heartView = new ImageView(heartImage);
                heartView.setFitWidth(30);
                heartView.setFitHeight(30);
                heartIcons.add(heartView);
                heartsContainer.getChildren().add(heartView);
            }
        } else {
            // Fallback: hiển thị số nếu không load được ảnh
            livesLabel.setText("LIVES: " + lives);
        }
    }

    /**
     * Reset HUD về trạng thái ban đầu
     */
    public void reset() {
        setScore(0);
        setLives(3); // hoặc số mạng mặc định
        setLevel(1);
        System.out.println("🔄 HUD: Reset to initial state");
    }

    /**
     * Lấy container để thêm vào scene
     */
    public VBox getContainer() {
        return hudContainer;
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
    }

    public void hideMessage() {
        messageLabel.setVisible(false);
        messageLabel.setManaged(false);
    }
}