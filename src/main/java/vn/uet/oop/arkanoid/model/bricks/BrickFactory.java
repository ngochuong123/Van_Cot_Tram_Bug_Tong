package vn.uet.oop.arkanoid.model.bricks;

public class BrickFactory {
    public static Brick createBrick(BrickType.type type, double x, double y, double width, double height) {
        switch (type) {
            case NORMAL:
                return new NormalBrick(x, y, width, height);
            case STRONG:
                return new StrongBrick(x, y, width, height);
            case UNBREAKABLE:
                return new UnbreakableBrick(x, y, width, height);
            default:
                return null;
        }
    }
}
