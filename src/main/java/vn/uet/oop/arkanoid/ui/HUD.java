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
        this.heart = 5;
        this.score = score;
        createScores();
        createHeart();
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
                        "-fx-text-fill: #ff33ddce;" +
                        "-fx-effect: dropshadow(gaussian, #ff3377ff, 15, 0.7, 0, 0);");
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
            ImageView heart = new ImageView(heartImage);
            heart.setFitWidth(30);
            heart.setFitHeight(30);
            hearts.add(heart);
            heartsBox.getChildren().add(heart);
        }

        if (!heart_score.getChildren().contains(heartsBox)) {
            heart_score.getChildren().addAll(heartsBox, scores);
        }
    }

    /**
     * ham xoa di trai tim khi bong roi xuong.
     */
    public void loseLife() {
        if (!hearts.isEmpty()) {
            ImageView lastHeart = hearts.remove(hearts.size() - 1);
            heartsBox.getChildren().remove(lastHeart);
            heart_score.getChildren().addAll(heartsBox, scores);
        }
    }

    /**
     * ham cap nhat diem so.
     */
    public void updateScore() {
        this.score += GameConfig.addscore;
        this.scores.setText("SCORE: " + this.score);
        heart_score.getChildren().addAll(heartsBox, scores);
    }

    /**
     * ham kiem tra xem ban da het mang hay chua.
     * 
     * @param heart
     * @return true or false
     */
    public boolean stateHeart(int heart) {
        if (heart == 0) {
            return true;
        }
        return false;
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