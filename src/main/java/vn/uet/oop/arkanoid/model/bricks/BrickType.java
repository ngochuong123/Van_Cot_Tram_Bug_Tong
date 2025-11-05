package vn.uet.oop.arkanoid.model.bricks;


public enum BrickType {
    NORMAL,
    STRONG,
    UNBREAKABLE,
    REGENERATING,
    INVISIBLE,
    EXPLOSIVE,
    CHAIN;

    /**
     * get brick type.
     * @param s String type
     * @return BrickType
     */
    public static BrickType from(String s) {
        return BrickType.valueOf(s.trim().toUpperCase());
    }

}
