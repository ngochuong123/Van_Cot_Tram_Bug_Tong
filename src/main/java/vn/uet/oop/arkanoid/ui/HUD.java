package vn.uet.oop.arkanoid.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class HUD {
    private int score;
    private int lives;
    private BufferedImage heartImage;

    public HUD() {
        this.score = 0;
        this.lives = 5;
        try {
            heartImage = ImageIO.read(getClass().getResource("/com/example/image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // getter
    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public BufferedImage getHeartImage() {
        return heartImage;
    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + this.getScore(), 20, 30);

        for (int i = 0; i < this.getLives(); i++) {
            g.drawImage(heartImage, 20 + i * 40, 50, 32, 32, null);
        }
    }

    // public void updateScore() {
    // this.score =
    // }

    // public void updatLives() {
    // this.lives =
    // }

}