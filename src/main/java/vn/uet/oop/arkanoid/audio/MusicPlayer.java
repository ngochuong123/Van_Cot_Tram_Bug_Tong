package vn.uet.oop.arkanoid.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.net.URL;

/**
 * manage and play music.
 */
public class MusicPlayer {

    private static MediaPlayer currentMusicPlayer;
    private static boolean isMuted = false;
    private static final String MENU_MUSIC_PATH = "Sound/Music/Menu_Music.wav";
    private static final String BACKGROUND_MUSIC_PATH = "Sound/Music/BackGround_Music.wav";

    /**
     * play menu music.
     */
    public static void playMenuMusic() {
        playMusic(MENU_MUSIC_PATH);
    }

    /**
     * play background music.
     */
    public static void playGameMusic() {
        playMusic(BACKGROUND_MUSIC_PATH);
    }

    /**
     * static method to play music.
     */
    private static void playMusic(String resourcePath) {
        // stop previous music to play new music
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
     * stop playing music and release RAM.
     */
    public static void stop() {
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
            currentMusicPlayer.dispose();
            currentMusicPlayer = null;
        }
    }

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