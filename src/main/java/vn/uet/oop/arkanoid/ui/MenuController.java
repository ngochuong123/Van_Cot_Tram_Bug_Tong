package vn.uet.oop.arkanoid.ui;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.core.SceneRouter;
import javafx.stage.Stage;

public class MenuController {
    private Stage primaryStage;
    private Scene menuScene;
    // Các thành phần UI
    private BorderPane root;
    private HBox menuHBox;
    private VBox menuVBox;
    private Label titleLabel;
    private Button startButton;
    private Button settingsButton;
    private Button exitButton;
    private boolean onStartGame;

    public MenuController(Stage stage) {
        this.primaryStage = stage;
        this.onStartGame = false;
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
        this.menuScene.getRoot().setStyle(
                "-fx-background-image: url('file:src/main/java/vn/uet/oop/arkanoid/config/image/menu.jpg');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center center;");
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
            SceneRouter router = new SceneRouter();
            router.playgame(primaryStage);
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

    public boolean getOnStartGame() {
        return this.onStartGame;
    }
}
