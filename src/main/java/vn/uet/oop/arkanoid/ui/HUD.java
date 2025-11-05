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
    private Label scoreLabel;
    private Label levelLabel;
    private Label livesLabel;
    private Label messageLabel;
    private List<ImageView> heartIcons = new ArrayList<>();
    private HBox heartsContainer = new HBox(5);
    private VBox hudContainer = new VBox(10);
    private final Image heartImage;

    public HUD() {
        initializeUI();
        heartImage = loadHeartImage();
    }

    private void initializeUI() {
        scoreLabel = new Label("SCORE: 0");
        scoreLabel.setStyle(
                "-fx-font-family: 'Impact';" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #7cfc00;" +
                        "-fx-effect: dropshadow(gaussian, #006400, 8, 0.8, 2, 2);");

        levelLabel = new Label("LEVEL: 1");
        levelLabel.setStyle(
                "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 20px;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-weight: bold;");

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
            System.err.println(" Could not load heart image: " + e.getMessage());
            return null;
        }
    }


    /**
     * update score
     * @param score current score
     */
    public void setScore(int score) {
        scoreLabel.setText("SCORE: " + score);
    }

    /**
     * update lives from GameManager
     */
    public void setLives(int lives) {
        updateHeartsDisplay(lives);
    }

    /**
     * update level display
     */
    public void setLevel(int level) {
        levelLabel.setText("LEVEL: " + level);
    }

    /**
     * update heart icons display
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
            livesLabel.setText("LIVES: " + lives);
        }
    }

    /**
     * Reset HUD
     */
    public void reset() {
        setScore(0);
        setLives(3);
        setLevel(1);
    }

    /**
     * get the HUD container
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