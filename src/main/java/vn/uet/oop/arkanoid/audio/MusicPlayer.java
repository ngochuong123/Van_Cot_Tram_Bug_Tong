package vn.uet.oop.arkanoid.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.net.URL;

/**
 * Quản lý và phát nhạc nền (BGM).
 * Sử dụng MediaPlayer để hỗ trợ các file dài và lặp lại.
 */
public class MusicPlayer {

    private static MediaPlayer currentMusicPlayer;
    private static boolean isMuted = false;

    // --- THAY ĐỔI CÁC ĐƯỜNG DẪN NÀY cho đúng với file nhạc của bạn ---
    // (Đảm bảo file nhạc nền ở trong /resources/Sound/Music/)
    private static final String MENU_MUSIC_PATH = "Sound/Music/Menu_Music.wav";
    private static final String BACKGROUND_MUSIC_PATH = "Sound/Music/BackGround_Music.wav";

    /**
     * Phát nhạc nền cho Menu.
     */
    public static void playMenuMusic() {
        playMusic(MENU_MUSIC_PATH);
    }

    /**
     * Phát nhạc nền khi chơi game.
     */
    public static void playGameMusic() {
        playMusic(BACKGROUND_MUSIC_PATH);
    }

    /**
     * Phương thức nội bộ để tải và phát nhạc.
     */
    private static void playMusic(String resourcePath) {
        // Dừng nhạc cũ trước khi phát nhạc mới
        stop();

        try {
            URL resourceUrl = MusicPlayer.class.getClassLoader().getResource(resourcePath);
            if (resourceUrl == null) {
                System.err.println("Không tìm thấy file nhạc: " + resourcePath);
                return;
            }

            Media media = new Media(resourceUrl.toExternalForm());
            currentMusicPlayer = new MediaPlayer(media);
            currentMusicPlayer.setMute(isMuted);

            // Thiết lập lặp lại vô tận
            // Khi nhạc kết thúc, tua về 0 và phát lại
            currentMusicPlayer.setOnEndOfMedia(() ->
                    currentMusicPlayer.seek(Duration.ZERO)
            );

            currentMusicPlayer.play();

        } catch (Exception e) {
            System.err.println("Lỗi khi phát nhạc: " + resourcePath);
            e.printStackTrace();
        }
    }

    /**
     * Dừng phát nhạc hiện tại và giải phóng tài nguyên.
     */
    public static void stop() {
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
            currentMusicPlayer.dispose(); // Quan trọng: Giải phóng bộ nhớ
            currentMusicPlayer = null;
        }
    }

    /**
     * Tắt hoặc bật tiếng nhạc nền.
     */
    public static void setMute(boolean muted) {
        isMuted = muted;
        if (currentMusicPlayer != null) {
            currentMusicPlayer.setMute(isMuted);
        }
    }

    public static boolean isMuted() {
        return isMuted;
    }
}