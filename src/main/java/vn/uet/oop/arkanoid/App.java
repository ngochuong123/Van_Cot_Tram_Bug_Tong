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
    }

    public static void main(String[] args) {
        launch(args); // Khởi động JavaFX Application
    }
}
