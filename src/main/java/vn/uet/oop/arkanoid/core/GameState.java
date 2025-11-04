package vn.uet.oop.arkanoid.core;

public enum GameState {
    MENU("Main Menu"),
    PLAYING("In Game"),
    PAUSED("Game Paused"),
    GAME_OVER("Game Over"),
    LEVEL_COMPLETE("Level Complete"),
    TRANSITION("Level Transition");

    private final String description;

    GameState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Kiểm tra xem game có đang active không
    public boolean isGameActive() {
        return this == PLAYING || this == PAUSED;
    }

    // Kiểm tra xem có thể pause không
    public boolean canPause() {
        return this == PLAYING;
    }

    // Kiểm tra xem có thể resume không
    public boolean canResume() {
        return this == PAUSED;
    }
}