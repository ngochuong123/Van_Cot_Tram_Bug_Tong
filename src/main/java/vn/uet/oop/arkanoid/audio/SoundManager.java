package vn.uet.oop.arkanoid.audio;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * manage and control sound effect.
 */
public class SoundManager {
    private static Map<String, AudioClip> soundCache = new HashMap<>();
    private static final List<AudioClip> clipHolder = new ArrayList<>();
    private static boolean isMuted = false;

    public static final String HIT_PADDLE = "hit_paddle";
    public static final String HIT_BRICK = "hit_brick";
    public static final String HIT_WALL = "hit_wall";
    public static final String POWERUP_PICKUP = "powerup_pickup";
    public static final String BREAK_BRICK = "brick_break";
    public static final String GAME_OVER = "game_over";

    public static void loadSounds() {
        // load when start
        loadSound(HIT_PADDLE, "Sound/Music/Ball_Touch_Paddle.wav");
        loadSound(HIT_BRICK, "Sound/Music/Ball_Hit_Brick.wav");
        loadSound(HIT_WALL, "Sound/Music/Ball_Touch_Paddle.wav");
        loadSound(POWERUP_PICKUP, "Sound/Music/PowerUp_Sound.wav");
        loadSound(BREAK_BRICK, "Sound/Music/Brick_Break.wav");
        loadSound(GAME_OVER, "Sound/Music/GameOver.wav");
    }

    private static void loadSound(String name, String resourcePath) {
        try {
            URL resourceUrl = SoundManager.class.getClassLoader().getResource(resourcePath);
            if (resourceUrl == null) {
                System.err.println("Không tìm thấy file âm thanh: " + resourcePath);
                return;
            }

            AudioClip clip = new AudioClip(resourceUrl.toExternalForm());

            soundCache.put(name, clip);

            clipHolder.add(clip);

        } catch (Exception e) {
            System.err.println("Lỗi khi tải âm thanh: " + resourcePath);
            e.printStackTrace();
        }
    }

    public static void play(String name) {
        if (isMuted) return;

        AudioClip clip = soundCache.get(name);

        if (clip != null) {
            clip.play();
        } else {
            System.err.println("Âm thanh chưa được tải: " + name);
        }
    }

    public static void setMuted(boolean muted) {
        isMuted = muted;
    }

    public static boolean isMuted() {
        return isMuted;
    }
}