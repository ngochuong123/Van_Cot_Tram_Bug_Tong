package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ChainBrick extends Brick {
    private final int chainId;

    private final Image chainBrickImage;

    public ChainBrick(double x, double y, double width, double height, int durabilityPoints, int chainId) {
        super(x, y, width, height, durabilityPoints);
        this.durabilityPoints = Math.min(durabilityPoints, 4);
        this.chainId = chainId;

        chainBrickImage = new Image(getClass().getResourceAsStream("/image/ChainBrick.png"));
    }

    /**
     * substract 1 durability point when hit.
     * @return the remaining durability points
     */
    @Override
    public int takeHit() {
        durabilityPoints = Math.max(0, durabilityPoints - 1);
        return durabilityPoints;
    }

    @Override
    public boolean isBroken() { return durabilityPoints <= 0; }

    @Override
    public void update(double deltaTime) { }

    /**
     * render the brick.
     * @param gc the GraphicsContext to render on
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!isBroken()) {
            gc.drawImage(chainBrickImage, getX(), getY(), getWidth(), getHeight());

            Image crackImage = getCrackImage();
            if (crackImage != null) {
                gc.drawImage(crackImage, getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    public int getChainId() { return chainId; }
}
