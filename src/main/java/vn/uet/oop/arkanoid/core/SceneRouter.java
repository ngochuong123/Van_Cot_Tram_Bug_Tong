package vn.uet.oop.arkanoid.core;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vn.uet.oop.arkanoid.config.GameConfig;
import vn.uet.oop.arkanoid.ui.*;

public class SceneRouter {
    private Stage primaryStage;
    private GameManager gameManager;
    private GameRenderer gameRenderer;
    private AnimationTimer gameLoop;

    // Controller references
    private MenuController menuController;
    private PauseController pauseController;
    private GameOverController gameOverController;
    // private SettingsController settingsController;

    private HUD hud;

    // Input states
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    // Game components
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private Scene gameScene;

    public SceneRouter(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeRouter();
    }

    private void initializeRouter() {
        System.out.println("🚀 Initializing SceneRouter...");
        // Pre-initialize các controller chính
        this.menuController = new MenuController(primaryStage, this);
        // Xử lý sự kiện đóng cửa sổ chính
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Main window closing...");
            exitGame();
        });
    }

    // ==================== MAIN NAVIGATION METHODS ====================

    public void showMainMenu() {
        System.out.println("🏠 Showing Main Menu");
        stopGameLoop();
        if (menuController == null) {
            menuController = new MenuController(primaryStage, this);
        }
    }

    public void startNewGame() {
        System.out.println("🎮 Starting New Game");
        cleanup();
        // Khởi tạo game components
        initializeGameComponents();
        // Đặt trạng thái PLAYING
        gameManager.setState(GameState.PLAYING);
        // Tạo và chuyển sang game scene
        switchToGameScene();
        // Bắt đầu game loop
        startGameLoop();
    }

    public void resumeGame() {
        System.out.println("Resuming Game");

        // ĐẢM BẢO pause menu đã đóng
        if (pauseController != null && pauseController.isShowing()) {
            pauseController.close();
            System.out.println("✅ Pause menu closed");
        }

        if (gameManager != null) {
            gameManager.setState(GameState.PLAYING);
            System.out.println("✅ GameState set to PLAYING");
        }

        // RESTART GAME LOOP
        startGameLoop();

        // FOCUS LẠI GAME CANVAS
        if (gameCanvas != null) {
            gameCanvas.requestFocus();
            System.out.println("✅ Game canvas focused");
        }
        System.out.println("✅ Game resumed successfully");
    }

    public void showPauseMenu() {
        System.out.println("⏸️ Showing Pause Menu");

        if (gameManager != null) {
            gameManager.setState(GameState.PAUSED);
        }

        stopGameLoop();

        if (pauseController == null) {
            pauseController = new PauseController(primaryStage, this);
        }
        pauseController.show();
    }

    public void showGameOver(int finalScore) {
        System.out.println("💀 Showing Game Over - Score: " + finalScore);

        if (gameManager != null) {
            gameManager.setState(GameState.GAME_OVER);
        }

        stopGameLoop();

        if (gameOverController == null) {
            gameOverController = new GameOverController(primaryStage, finalScore, this);
        } else {
            gameOverController.updateScore(finalScore);
        }
        gameOverController.show();
    }

    public void showLevelComplete() {
        System.out.println("🎉 Level Complete!");

        if (gameManager != null) {
            gameManager.setState(GameState.LEVEL_COMPLETE);
            // Tạm dừng game loop trong thời gian chuyển level
            stopGameLoop();

            // Sau 2 giây, chuyển level mới
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        loadNextLevel();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void loadNextLevel() {
        System.out.println("🔄 Loading next level...");
        if (gameManager != null) {
            gameManager.loadNextLevel();
            gameManager.setState(GameState.PLAYING);
            startGameLoop();
        }
    }

    public void showSettings() {
        System.out.println("⚙️ Showing Settings");
        // Tạm thời hiển thị thông báo
        System.out.println("Settings feature coming soon!");
    }

    public void showHighScores() {
        System.out.println("🏆 Showing High Scores");
        // Implement HighScoresController
        System.out.println("High Scores feature coming soon!");
    }

    public void exitGame() {
        System.out.println("👋 Exiting Game...");
        stopGameLoop();
        javafx.application.Platform.exit();
        System.exit(0);
    }

    // ==================== GAME SCENE MANAGEMENT ====================

    private void initializeGameComponents() {
        System.out.println("🔄 Initializing game components...");

        // Khởi tạo GameManager (dùng singleton hoặc new)
        this.gameManager = GameManager.getInstance();
        // Hoặc: this.gameManager = new GameManager();

        // Khởi tạo GameRenderer
        this.gameRenderer = new GameRenderer(gameManager);

        // Tạo canvas và graphics context
    if (this.gameCanvas == null) {
        this.gameCanvas = new Canvas(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        this.gc = gameCanvas.getGraphicsContext2D();
    } else {
        this.gc = gameCanvas.getGraphicsContext2D();
    }

    }

    private void switchToGameScene() {
        System.out.println("🎯 Switching to Game Scene");

        if (gameScene == null) {
            createGameScene();
        }

        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Arkanoid Game");
        primaryStage.show();

        // Focus để nhận input
        gameCanvas.requestFocus();
    }

    private void createGameScene() {
        System.out.println("🎨 Creating Game Scene");

        // Tạo root layout với HUD
        StackPane root = new StackPane();

        // Canvas chiếm toàn bộ
        root.getChildren().add(gameCanvas);

        // HUD overlay lên trên
        this.hud = new HUD();
        VBox hudContainer = hud.getContainer();
        hudContainer.setStyle("-fx-background-color: transparent;"); // Background trong suốt
        StackPane.setAlignment(hudContainer, Pos.TOP_LEFT);
        root.getChildren().add(hudContainer);

        this.gameScene = new Scene(root, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        setupGameInputHandling();
    }

    // ==================== GAME LOOP MANAGEMENT ====================

    private void startGameLoop() {
        System.out.println("🔄 Starting Game Loop");

        if (gameLoop != null) {
            gameLoop.stop();
        }

        final long[] lastTime = { System.nanoTime() };

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                try {
                    // Tính delta time
                    double deltaTime = (currentTime - lastTime[0]) / 1_000_000_000.0;
                    lastTime[0] = currentTime;
                    deltaTime = Math.min(deltaTime, 0.1);

                    // Xử lý theo trạng thái game
                    if (gameManager != null) {
                        GameState currentState = gameManager.getCurrentState();

                        switch (currentState) {
                            case PLAYING:
                                // Update game state
                                gameManager.update(deltaTime, leftPressed, rightPressed);

                                // Update HUD
                                updateHUDFromGameState();

                                // Render game
                                gameRenderer.render(gc);

                                // Kiểm tra auto-navigation
                                checkAutoNavigation();
                                break;

                            case PAUSED:
                            case GAME_OVER:
                            case LEVEL_COMPLETE:
                            case MENU:
                                // Chỉ render mà không update logic
                                break;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error in game loop: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        gameLoop.start();
    }

    private void stopGameLoop() {
        System.out.println("🛑 Stopping Game Loop");
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
    }

    // ==================== INPUT HANDLING ====================

    private void setupGameInputHandling() {
        System.out.println("⌨️ Setting up game input handling");
        if (gameManager != null && gameManager.getCurrentState() == GameState.PLAYING) {
            gameScene.setOnKeyPressed(event -> {
                KeyCode code = event.getCode();

                switch (code) {
                    case LEFT:
                    case A:
                        leftPressed = true;
                        break;
                    case RIGHT:
                    case D:
                        rightPressed = true;
                        break;
                    case SPACE:
                        if (!spacePressed) {
                            spacePressed = true;
                            handleSpacePress();
                        }
                        break;
                    case P:
                    case ESCAPE:
                        showPauseMenu();
                        break;
                }
            });
        }

        gameScene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case LEFT:
                case A:
                    leftPressed = false;
                    break;
                case RIGHT:
                case D:
                    rightPressed = false;
                    break;
                case SPACE:
                    spacePressed = false;
                    break;
            }
        });

        // Click để focus
        gameScene.setOnMouseClicked(e -> gameCanvas.requestFocus());
    }

    private void handleSpacePress() {
        if (gameManager != null) {
            GameState state = gameManager.getCurrentState();

            switch (state) {
                case GAME_OVER:
                    startNewGame(); // Restart từ game over
                    break;
                case PLAYING:
                    gameManager.launchBall(); // Launch ball khi đang chơi
                    break;
                case PAUSED:
                    resumeGame(); // Resume từ pause bằng space
                    break;
            }
        }
    }

    // ==================== UTILITY METHODS ====================

    private void updateHUDFromGameState() {
        if (hud != null && gameManager != null) {
            hud.setScore(gameManager.getScore());
            hud.setLives(gameManager.getLives());
            hud.setLevel(gameManager.getCurrentLevel());

            // Hiển thị thông báo trạng thái game
            GameState state = gameManager.getCurrentState();
            switch (state) {
                case PAUSED:
                    hud.showMessage("PAUSED");
                    break;
                case GAME_OVER:
                    hud.showMessage("GAME OVER");
                    break;
                case LEVEL_COMPLETE:
                    hud.showMessage("LEVEL COMPLETE!");
                    break;
                default:
                    hud.hideMessage();
                    break;
            }
        }
    }

    private void checkAutoNavigation() {
        if (gameManager != null) {
            GameState currentState = gameManager.getCurrentState();

            switch (currentState) {
                case GAME_OVER:
                    showGameOver(gameManager.getScore());
                    break;

                case LEVEL_COMPLETE:
                    showLevelComplete();
                    break;

                case PLAYING:
                    // Kiểm tra các điều kiện chuyển trạng thái
                    if (!gameManager.isAlive()) {
                        gameManager.setState(GameState.GAME_OVER);
                    } else if (gameManager.getBricksCount() == 0) {
                        gameManager.setState(GameState.LEVEL_COMPLETE);
                    }
                    break;
            }
        }
    }

    // ==================== GETTER METHODS ====================

    public GameManager getGameManager() {
        return gameManager;
    }

    public boolean isGameRunning() {
        return gameLoop != null && gameManager != null;
    }

    public HUD getHUD() {
        return hud;
    }

    // ==================== CLEANUP ====================

    public void cleanup() {
        System.out.println(" Cleaning up SceneRouter");
        stopGameLoop();

        if (gameManager != null) {
            gameManager.resetGame();
        }
        // Cleanup các controller
        if (pauseController != null) {
            pauseController.close();
        }
        if (gameOverController != null) {
            gameOverController.close();
        }

        gameLoop = null;

        System.out.println(" Cleanup completed");
    }
}