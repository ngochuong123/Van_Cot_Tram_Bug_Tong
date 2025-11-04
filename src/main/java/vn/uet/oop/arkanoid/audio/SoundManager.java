package vn.uet.oop.arkanoid.audio;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.ArrayList; // <-- Import
import java.util.HashMap;
import java.util.List;      // <-- Import
import java.util.Map;

/**
 * Quản lý SFX, CÓ Caching và Sửa lỗi Garbage Collector (GC).
 */
public class SoundManager {
    private static Map<String, AudioClip> soundCache = new HashMap<>();

    // --- SỬA LỖI GC: Thêm một List để "giữ" các clip, chống bị GC dọn dẹp ---
    private static final List<AudioClip> clipHolder = new ArrayList<>();

    private static boolean isMuted = false;

    public static final String HIT_PADDLE = "hit_paddle";
    public static final String HIT_BRICK = "hit_brick";
    public static final String HIT_WALL = "hit_wall";
    public static final String POWERUP_PICKUP = "powerup_pickup";
    public static final String BREAK_BRICK = "brick_break";
    public static final String GAME_OVER = "game_over";

    public static void loadSounds() {
        // Tải 1 lần khi khởi động
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

            // 1. Tải và tạo AudioClip MỘT LẦN
            AudioClip clip = new AudioClip(resourceUrl.toExternalForm());

            // 2. Lưu vào cache để truy cập nhanh
            soundCache.put(name, clip);

            // 3. Lưu vào list để chống GC "dọn dẹp"
            clipHolder.add(clip);

        } catch (Exception e) {
            System.err.println("Lỗi khi tải âm thanh: " + resourcePath);
            e.printStackTrace();
        }
    }

    public static void play(String name) {
        if (isMuted) return;

        // --- QUAN TRỌNG: Chỉ LẤY clip từ cache, không tạo mới ---
        AudioClip clip = soundCache.get(name);

        if (clip != null) {
            clip.play(); // Đã ở trong bộ nhớ, phát ngay lập tức (không delay)
        } else {
            System.err.println("Âm thanh chưa được tải: " + name);
        }
    }

    // (Các hàm setMuted và isMuted...)
    public static void setMuted(boolean muted) {
        isMuted = muted;
    }

    public static boolean isMuted() {
        return isMuted;
    }
}