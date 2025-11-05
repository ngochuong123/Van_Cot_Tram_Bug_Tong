package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.image.Image;
import vn.uet.oop.arkanoid.model.GameObject;

public abstract class Brick extends GameObject {
    protected int durabilityPoints;
    int maxDurability;

    public abstract int takeHit();
    public abstract boolean isBroken();
    private static Image crack1, crack2, crack3;

    public Brick(double x, double y, double width, double height) {
        super(x, y, width, height);

    }

    public Brick(double x, double y, double width, double height, int durabilityPoints) {
        super(x, y, width, height);
        this.durabilityPoints = durabilityPoints;
        this.maxDurability = durabilityPoints;
    }

    static {
        try {
            crack1 = new Image(Brick.class.getResourceAsStream("/image/crack_1.png"));
            crack2 = new Image(Brick.class.getResourceAsStream("/image/crack_2.png"));
            crack3 = new Image(Brick.class.getResourceAsStream("/image/crack_3.png"));
        } catch (Exception e) {
            System.err.println("Failed to load crack images: " + e.getMessage());
        }
    }

    /**
     * Get the crack image based on how many times the brick has been hit.
     * @return The crack image.
     */
    protected Image getCrackImage() {
        int timesHit = maxDurability - durabilityPoints;

        if (timesHit == 0) {
            return null;
        } else if (timesHit == 1) {
            return crack1;
        } else if (timesHit == 2) {
            return crack2;
        } else {
            return crack3;
        }
    }




}
