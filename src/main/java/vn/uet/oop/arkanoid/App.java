package vn.uet.oop.arkanoid;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.core.GameManager;
import vn.uet.oop.arkanoid.ui.HUD;
import vn.uet.oop.arkanoid.ui.MenuController;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MenuController menuController = new MenuController(stage);
        menuController.eventMenu();
        // --- Tạo Canvas ---
        Canvas canvas = new Canvas(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // --- Tạo GameManager ---
        gameManager = new GameManager();

        // --- Scene & Stage ---
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Arkanoid - JavaFX Edition");
        stage.show();

        // --- Xử lý phím ---
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
                leftPressed = true;
            } else if (event.getCode() == KeyCode.RIGHT  || event.getCode() == KeyCode.D) {
                rightPressed = true;
            } if (event.getCode() == KeyCode.SPACE) {
                gameManager.launchBall();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT   || event.getCode() == KeyCode.A) {
                leftPressed = false;
            } else if (event.getCode() == KeyCode.RIGHT    || event.getCode() == KeyCode.D) {
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

                double deltaTime = (now - lastTime) / 1e9; // nano -> giây
                lastTime = now;

                // Cập nhật logic
                gameManager.update(deltaTime, leftPressed, rightPressed);

                // Vẽ lại màn hình
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
                gameManager.render(gc);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args); // Khởi động JavaFX Application
    }
}
