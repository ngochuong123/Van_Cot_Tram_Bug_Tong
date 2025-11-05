package vn.uet.oop.arkanoid;

import javafx.application.Application;
import javafx.stage.Stage;
import vn.uet.oop.arkanoid.audio.AudioEngine;
import vn.uet.oop.arkanoid.core.SceneRouter;
import vn.uet.oop.arkanoid.config.GameConfig;

/**
 * Lớp chính khởi chạy ứng dụng Arkanoid
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            // Tải tất cả âm thanh vào bộ nhớ TRƯỚC KHI game bắt đầu
            AudioEngine.init();

            // Cấu hình stage chính
            primaryStage.setTitle("Arkanoid Game");
            primaryStage.setResizable(false);

            // Khởi tạo SceneRouter - bộ điều hướng chính của game
            SceneRouter router = new SceneRouter(primaryStage);

            // Hiển thị menu chính
            router.showMainMenu();

            // Hiển thị cửa sổ
            primaryStage.show();

            System.out.println("🚀 Arkanoid Game started successfully!");

        } catch (Exception e) {
            System.err.println("❌ Failed to start game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Cleanup khi ứng dụng dừng
        System.out.println("🛑 Game application stopping...");
    }

    public static void main(String[] args) {
        System.out.println("🎮 Starting Arkanoid Game...");
        launch(args);
    }
}