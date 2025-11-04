package vn.uet.oop.arkanoid.model.bricks;

import javafx.scene.image.Image;

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
