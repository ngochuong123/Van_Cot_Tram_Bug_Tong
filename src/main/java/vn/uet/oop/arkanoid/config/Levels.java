package vn.uet.oop.arkanoid.config;

public final class Levels {
    private Levels() {}

    // 0 = EMPTY, 1 = NORMAL, 2 = STRONG, 10 = UNBREAKABLE
    public static final int[][] LEVEL_1 = {
            {1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,1},
            {1,2,0,0,0,0,0,2,1},
            {1,2,0,1,1,1,0,2,1},
            {1,2,0,0,0,0,0,2,1},
            {1,1,1,1,1,1,1,1,1}
    };

    public static final int[][] LEVEL_2 = {
            {0,0,2,2,2,2,2,0,0},
            {0,1,1,1,1,1,1,1,0},
            {2,1,0,0,0,0,0,1,2},
            {2,1,0,2,2,2,0,1,2},
            {0,1,10,10,1,10,10,1,0},
            {0,0,2,2,10,2,2,0,0}
    };
}
