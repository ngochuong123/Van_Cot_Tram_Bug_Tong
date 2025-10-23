package vn.uet.oop.arkanoid.core;

import vn.uet.oop.arkanoid.config.GameConfig;
import javafx.stage.Stage;

public class GameState {
    private Stage stateStage;

    public GameState(Stage s) {
        this.stateStage = s;
    }

    public void checkGameState(int x) {
        switch (x) {
            case GameConfig.pauseState:

            case GameConfig.settingState:

            case GameConfig.gameOverState:
        }
    }
}
