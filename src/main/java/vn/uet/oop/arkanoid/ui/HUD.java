package vn.uet.oop.arkanoid.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vn.uet.oop.arkanoid.config.GameConfig;
import java.util.ArrayList;
import java.util.List;

public class HUD {
    private Stage HUDStage;
    private Scene HUDScene;
    private int heart;
    private int score;
    private Label scores;
    private List<ImageView> hearts = new ArrayList<>();
    private HBox heartsBox = new HBox(5);
    private VBox heart_score = new VBox(10);

    public HUD(Stage HUDStage, int score, Scene HUDScene) {
        this.HUDStage = HUDStage;
        this.HUDScene = HUDScene;
        this.heart = 5; // Số mạng ban đầu
        this.score = score;
        createScores();
        createHeart();
    }

    // Thêm getter cho số mạng hiện tại
    public int getHeartCount() {
        return heart;
    }

    // Thêm getter cho điểm số
    public int getScore() {
        return score;
    }

    /*
     * ham tao diem so de in len man hinh.
     */
    public void createScores() {
        this.scores = new Label("SCORE: " + this.score);
        this.scores.setStyle(
                "-fx-font-family: 'Impact';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #7cfc00;" + // LawnGreen
                        "-fx-effect: dropshadow(gaussian, #006400, 8, 0.8, 2, 2);" +
                        "-fx-effect: innershadow(gaussian, #90ee90, 3, 0.5, 0, 0);");
    }

    /**
     * ham tao hinh anh mang player.
     */
    public void createHeart() {
        // Đường dẫn ảnh trái tim
        Image heartImage = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/heart.png");

        // Xóa tim cũ (nếu có)
        heartsBox.getChildren().clear();
        hearts.clear();

        // Tạo tim mới
        for (int i = 0; i < heart; i++) {
            ImageView heartView = new ImageView(heartImage);
            heartView.setFitWidth(30);
            heartView.setFitHeight(30);
            hearts.add(heartView);
            heartsBox.getChildren().add(heartView);
        }

        if (!heart_score.getChildren().contains(heartsBox)) {
            heart_score.getChildren().addAll(heartsBox, scores);
        }
    }

    /**
     * ham xoa di trai tim khi bong roi xuong.
     */
    public void loseLife() {
        if (heart > 0) {
            heart--; // Giảm số mạng

            System.out.println("Mất 1 mạng! Mạng còn lại: " + heart);

            // Cập nhật giao diện
            updateHeartDisplay();
        }
    }

    /**
     * Cập nhật hiển thị trái tim
     */
    private void updateHeartDisplay() {
        // Đường dẫn ảnh trái tim
        Image heartImage = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/heart.png");

        // Xóa tim cũ
        heartsBox.getChildren().clear();
        hearts.clear();

        // Tạo tim mới theo số mạng hiện tại
        for (int i = 0; i < heart; i++) {
            ImageView heartView = new ImageView(heartImage);
            heartView.setFitWidth(30);
            heartView.setFitHeight(30);
            hearts.add(heartView);
            heartsBox.getChildren().add(heartView);
        }

        // Đảm bảo HUD được cập nhật
        heart_score.getChildren().setAll(heartsBox, scores);
    }

    /**
     * ham cap nhat diem so.
     */
    public void updateScore() {
        this.score += GameConfig.addscore;
        this.scores.setText("SCORE: " + this.score);
        System.out.println("Điểm: " + this.score);
    }

    /**
     * ham kiem tra xem ban da het mang hay chua.
     */
    public boolean stateHeart(int heart) {
        return heart == 0;
    }

    /**
     * ham ve HUD len man hinh.
     */
    public void createHUD() {
        BorderPane topleft = (BorderPane) HUDScene.getRoot();
        heart_score.setAlignment(Pos.TOP_LEFT);
        heart_score.setStyle("-fx-padding: 25;");
        topleft.setTop(heart_score);
        HUDStage.setScene(HUDScene);
    }
}