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

    /**
     * check game is active  or paused
     * @return true if game is active or paused
     */
    public boolean isGameActive() {
        return this == PLAYING || this == PAUSED;
    }

    public boolean canPause() {
        return this == PLAYING;
    }

    public boolean canResume() {
        return this == PAUSED;
    }
}