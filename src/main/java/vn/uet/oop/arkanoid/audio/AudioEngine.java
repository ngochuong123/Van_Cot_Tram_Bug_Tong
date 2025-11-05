package vn.uet.oop.arkanoid.audio;

/**
 * Control sound and music class.
 */
public class AudioEngine {

    private static boolean globalMute = false;

    /**
     * create sound system.
     */
    public static void init() {
        System.out.println("--- AUDIO ENGINE: ĐANG TẢI ÂM THANH... ---");
        SoundManager.loadSounds();
    }

    /**
     * turn off all the sound.
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
     * play a sound.
     */
    public static void playSound(String soundName) {
        System.out.println("--- AUDIO ENGINE: YÊU CẦU PHÁT: " + soundName);
        SoundManager.play(soundName);
    }

    // --- CÁC PHƯƠNG THỨC BỊ THIẾU ---

    /**
     * play menu music.
     */
    public static void playMenuMusic() {
        MusicPlayer.playMenuMusic();
    }

    /**
     * play background music.
     */
    public static void playGameMusic() {
        MusicPlayer.playGameMusic();
    }

    /**
     * stop every music is playing.
     */
    public static void stopMusic() {
        MusicPlayer.stop();
    }
}