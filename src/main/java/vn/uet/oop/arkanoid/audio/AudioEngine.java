package vn.uet.oop.arkanoid.audio;

/**
 * Lớp điều khiển âm thanh tổng (Facade Pattern).
 */
public class AudioEngine {

    private static boolean globalMute = false;

    /**
     * Khởi tạo hệ thống âm thanh, tải các SFX vào bộ nhớ.
     */
    public static void init() {
        System.out.println("--- AUDIO ENGINE: ĐANG TẢI ÂM THANH... ---");
        SoundManager.loadSounds();
    }

    /**
     * Tắt/Bật tất cả âm thanh (cả nhạc và SFX).
     */
    public static void setGlobalMute(boolean mute) {
        globalMute = mute;
        SoundManager.setMuted(mute);
//        MusicPlayer.setMuted(mute); // <-- BẠN BỊ THIẾU CÁI NÀY
    }

    public static boolean isGlobalMuted() {
        return globalMute;
    }

    /**
     * Phát một hiệu ứng âm thanh (SFX).
     */
    public static void playSound(String soundName) {
        System.out.println("--- AUDIO ENGINE: YÊU CẦU PHÁT: " + soundName);
        SoundManager.play(soundName);
    }

    // --- CÁC PHƯƠNG THỨC BỊ THIẾU ---

    /**
     * Phát nhạc nền của menu.
     */
    public static void playMenuMusic() {
        MusicPlayer.playMenuMusic();
    }

    /**
     * Phát nhạc nền khi chơi game.
     */
    public static void playGameMusic() {
        MusicPlayer.playGameMusic();
    }

    /**
     * Dừng mọi nhạc nền đang phát.
     */
    public static void stopMusic() {
        MusicPlayer.stop();
    }
}