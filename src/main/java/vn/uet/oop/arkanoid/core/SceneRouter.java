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

    // Tối ưu: Cache background image
    private Image backgroundImage;

    // Tối ưu: Fixed timestep for physics
    private static final double FIXED_TIMESTEP = 1.0 / 60.0; // 60 FPS
    private double accumulatedTime = 0.0;

    public void playgame(Stage stage) {
        stage.setOnCloseRequest(event -> {
            System.out.println("Người dùng nhấn nút X - Đóng game");
            Platform.exit();
            System.exit(0);
        });

        // --- Tạo layout chính ---
        // Tối ưu: Load background once
        backgroundImage = new Image("file:src/main/java/vn/uet/oop/arkanoid/config/image/backgroudgame.png");
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setFitWidth(GameConfig.SCREEN_WIDTH);
        imageView.setFitHeight(GameConfig.SCREEN_HEIGHT);

        StackPane layer = new StackPane(imageView, canvas);

        // --- Khởi tạo HUD trước ---
        BorderPane root = new BorderPane(layer);
        Scene scene = new Scene(root);
        HUD hud = new HUD(stage, 0, scene);
        hud.createHUD();

        // --- Khởi tạo GameManager với HUD ---
        gameManager = new GameManager(hud);

        stage.setScene(scene);
        stage.setTitle("Arkanoid - JavaFX Edition");
        stage.setResizable(false); // Tối ưu: Fixed window size
        stage.show();

        // --- Điều khiển bàn phím - Tối ưu hóa ---
        setupInputHandling(scene);

        // --- Tối ưu Game Loop với Fixed Timestep ---
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1e9;
                lastTime = now;

                // Fixed timestep for consistent physics
                accumulatedTime += deltaTime;

                while (accumulatedTime >= FIXED_TIMESTEP) {
                    gameManager.update(FIXED_TIMESTEP, leftPressed, rightPressed);
                    accumulatedTime -= FIXED_TIMESTEP;
                }

                render();
            }
        };
        timer.start();
    }

    private void setupInputHandling(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                leftPressed = true;
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                rightPressed = true;
            } else if (code == KeyCode.SPACE) {
                gameManager.launchBall();
            }
            // Ngăn xử lý sự kiện tiếp theo nếu là phím điều khiển
            if (code == KeyCode.LEFT || code == KeyCode.RIGHT ||
                    code == KeyCode.A || code == KeyCode.D ||
                    code == KeyCode.SPACE) {
                event.consume();
            }
        });

        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                leftPressed = false;
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                rightPressed = false;
            }
            // Ngăn xử lý sự kiện tiếp theo nếu là phím điều khiển
            if (code == KeyCode.LEFT || code == KeyCode.RIGHT ||
                    code == KeyCode.A || code == KeyCode.D) {
                event.consume();
            }
        });
    }

    private void render() {
        // Tối ưu: Clear chỉ khu vực cần thiết nếu có thể
        gc.clearRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        // Tối ưu: Sử dụng cached background
        gc.drawImage(backgroundImage, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        gameManager.render(gc);
    }
}