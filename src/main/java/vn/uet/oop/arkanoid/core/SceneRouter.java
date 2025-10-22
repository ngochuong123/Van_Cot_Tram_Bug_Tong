package vn.uet.oop.arkanoid.core;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.ui.HUD;

public class SceneRouter {
    private GameManager gameManager;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Canvas canvas = new Canvas(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
    public GraphicsContext gc = canvas.getGraphicsContext2D();

    public void playgame(Stage stage) {
        // --- Xử lý nút X (đóng cửa sổ) - THÊM NGAY TẠI ĐÂY ---
        stage.setOnCloseRequest(event -> {
            System.out.println("Người dùng nhấn nút X - Đóng game");
            // Dừng game loop và thoát chương trình
            Platform.exit();
            System.exit(0);
        });
        // --- Tạo layout chính ---
        gameManager = new GameManager();

        Image image = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/backgroudgame.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(GameConfig.SCREEN_WIDTH);
        imageView.setFitHeight(GameConfig.SCREEN_HEIGHT);

        StackPane layer = new StackPane(imageView, canvas);
        // --- Khởi tạo Game và HUD ---
        BorderPane root = new BorderPane(layer);
        Scene scene = new Scene(root);
        HUD hud = new HUD(stage, 0, scene);
        hud.createHUD();

        stage.setScene(scene);
        stage.setTitle("Arkanoid - JavaFX Edition");
        stage.show();

        // --- Điều khiển bàn phím ---
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                leftPressed = true;
            } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                rightPressed = true;
            }
            if (event.getCode() == KeyCode.SPACE) {
                gameManager.launchBall();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                leftPressed = false;
            } else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                rightPressed = false;
            }
        });

        // --- Game Loop ---
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1e9; // nano giây -> giây
                lastTime = now;

                gameManager.update(deltaTime, leftPressed, rightPressed);
                gc.clearRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
                Image bg = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/backgroudgame.png");
                gc.drawImage(bg, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
                gameManager.render(gc);
            }
        };
        timer.start();
    }
}
