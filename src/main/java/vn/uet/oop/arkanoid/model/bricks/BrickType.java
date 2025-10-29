package vn.uet.oop.arkanoid.model.bricks;

public enum BrickType {
    NORMAL,
    STRONG,
    UNBREAKABLE,
    REGENERATING,
    INVISIBLE,
    EXPLOSIVE,
    CHAIN;

    public static BrickType from(String s) {
        return BrickType.valueOf(s.trim().toUpperCase());
    }
}
