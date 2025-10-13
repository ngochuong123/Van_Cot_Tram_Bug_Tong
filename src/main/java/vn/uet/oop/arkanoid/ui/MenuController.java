package vn.uet.oop.arkanoid.ui;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.core.GameManager;
import javafx.stage.Stage;

/**
 * Controller cho màn hình menu chính - Xây dựng UI hoàn toàn bằng code
 * Xử lý các sự kiện: bắt đầu game, cài đặt, thoát, v.v.
 */
public class MenuController {
    private Stage primaryStage;
    private Scene menuScene;
    // private MediaPlayer backgroundMusic;
    // Các thành phần UI
    private BorderPane root;
    private HBox menuHBox;
    private VBox menuVBox;
    private Label titleLabel;
    private Button startButton;
    private Button settingsButton;
    private Button exitButton;
    private Runnable onStartGame;

    public MenuController(Stage stage, Runnable onStartGame) {
        this.primaryStage = stage;
        this.onStartGame = onStartGame;
        createMenu();
        eventMenu();
    }

    /*
     * ham khoi tao cac nut, tieu de cua menu.
     */
    public void createMenu() {
        createTitle();
        this.startButton = createButton("Start", 200, 60, 15);
        this.exitButton = createButton("Exit", 200, 60, 15);
        this.settingsButton = createButton("Setting", 100, 50, 30);

        this.menuVBox = new VBox(150);
        this.menuHBox = new HBox(100);
        this.menuHBox.getChildren().addAll(this.startButton, this.exitButton);
        this.menuHBox.setAlignment(Pos.CENTER);
        this.menuVBox.getChildren().addAll(this.titleLabel, this.menuHBox);
        this.menuVBox.setAlignment(Pos.CENTER);

        this.root = new BorderPane();
        root.setCenter(this.menuVBox);

        HBox topBox = new HBox(this.settingsButton);
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setStyle("-fx-padding: 20;"); // cách mép 20px
        root.setTop(topBox);

        this.menuScene = new Scene(root, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        this.primaryStage.setScene(menuScene);
        this.primaryStage.setTitle("ARKANOID");
        this.primaryStage.show();
    }

    public void createTitle() {
        // label hien chu.
        this.titleLabel = new Label("Let'S GO");
        this.titleLabel.setFont(Font.font("Impact", 72)); // font kiểu khối
        this.titleLabel.setTextFill(Color.web("#FF3366")); // màu vàng
        this.titleLabel.setStyle("-fx-font-weight: bold;");
        // hieu ung do bong 3d nhe
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#FF0040"));
        shadow.setRadius(25);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        this.titleLabel.setEffect(shadow);
        // hieu ung phong to nho lien tuc
        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.5), titleLabel);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(ScaleTransition.INDEFINITE);
        scale.play();
    }

    public Button createButton(String text, int W, int H, int R) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", 20));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #FF3366; -fx-background-radius: " + R + ";");
        button.setPrefWidth(W);
        button.setPrefHeight(H);
        return button;
    }

    public void eventMenu() {
        addHoverEffect(startButton, "#FF3366", "#00BFFF", 15);
        addHoverEffect(settingsButton, "#FF3366", "#00BFFF", 15);
        addHoverEffect(exitButton, "#FF3366", "#00BFFF", 15);

        startButton.setOnAction(e -> {
            System.out.println("Người chơi bấm START");
            // ví dụ: chuyển sang màn chơi
            if (onStartGame != null) {
                onStartGame.run();
            }
        });

        settingsButton.setOnAction(e -> {
            System.out.println("Người chơi bấm SETTINGS");
            // ví dụ: mở cửa sổ cài đặt
            // showSettingsWindow();
        });

        exitButton.setOnAction(e -> {
            System.out.println("Người chơi bấm EXIT");
            // thoát game
            primaryStage.close();
        });
    }

    /**
     * hieu ung cua button.
     * 
     * @param button
     * @param colorNormal
     * @param colorHover
     * @param radius
     */
    public void addHoverEffect(Button button, String colorNormal, String colorHover, int radius) {
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
            button.setStyle("-fx-background-color: " + colorHover + "; -fx-background-radius: " + radius + ";");
        });

        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            button.setStyle("-fx-background-color: " + colorNormal + "; -fx-background-radius: " + radius + ";");
        });
    }

    public Scene getScene() {
        return this.menuScene;
    }
}
