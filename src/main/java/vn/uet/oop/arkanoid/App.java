package vn.uet.oop.arkanoid;

import javafx.application.Application;
import javafx.stage.Stage;
import vn.uet.oop.arkanoid.audio.AudioEngine;
import vn.uet.oop.arkanoid.core.SceneRouter;
import vn.uet.oop.arkanoid.config.GameConfig;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            AudioEngine.init();

            primaryStage.setTitle("Arkanoid Game");
            primaryStage.setResizable(false);

            SceneRouter router = new SceneRouter(primaryStage);

            router.showMainMenu();

            primaryStage.show();

            System.out.println(" Arkanoid Game started successfully!");

        } catch (Exception e) {
            System.err.println("❌ Failed to start game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.out.println(" Game application stopping...");
    }

    public static void main(String[] args) {
        System.out.println("🎮 Starting Arkanoid Game...");
        launch(args);
    }
}