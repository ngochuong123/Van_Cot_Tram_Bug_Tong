package vn.uet.oop.arkanoid.model.bricks;

public final class BrickFactory {

    private BrickFactory() {
    }

    /**
     * reader line in level file.
     *
     * @param t tokenized line
     * @return Brick instance
     */
    public static Brick createFromTokens(String[] t) {
        if (t == null || t.length < 1)
            throw new IllegalArgumentException("Empty line");

        BrickType type = BrickType.from(t[0]);
        double x = getD(t, 1, "x");
        double y = getD(t, 2, "y");
        double w = getD(t, 3, "width");
        double h = getD(t, 4, "height");

        switch (type) {
            case UNBREAKABLE: {
                // UNBREAKABLE x y w h
                ensureLenAtLeast(t, 5, type);
                return new UnbreakableBrick(x, y, w, h);
            }
            case NORMAL: {
                // NORMAL x y w h dp
                ensureLenAtLeast(t, 6, type);
                int dp = getI(t, 5, "durability");
                return new NormalBrick(x, y, w, h, dp);
            }
            case STRONG: {
                ensureLenAtLeast(t, 6, type);
                int dp = getI(t, 5, "durability");
                return new StrongBrick(x, y, w, h, dp);
            }
            case REGENERATING: {
                ensureLenAtLeast(t, 6, type);
                int dp = getI(t, 5, "durability");
                return new RegeneratingBrick(x, y, w, h, dp );
            }
            case INVISIBLE: {
                ensureLenAtLeast(t, 6, type);
                int dp = getI(t, 5, "durability");
                return new InvisibleBrick(x, y, w, h, dp);
            }
            case EXPLOSIVE: {
                // EXPLOSIVE x y w h dp radius
                ensureLenAtLeast(t, 7, type);
                int dp = getI(t, 5, "durability");
                int radius = getI(t, 6, "radius");
                return new ExplosiveBrick(x, y, w, h, dp, radius);
            }
            case CHAIN: {
                // CHAIN x y w h dp chainId
                ensureLenAtLeast(t, 7, type);
                int dp = getI(t, 5, "durability");
                int chainId = getI(t, 6, "chainId");
                return new ChainBrick(x, y, w, h, dp, chainId);
            }
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    /**
     * ensure token length.
     *
     * @param t    token array
     * @param need needed length
     * @param type brick type
     */
    private static void ensureLenAtLeast(String[] t, int need, BrickType type) {
        if (t.length < need) {
            throw new IllegalArgumentException("Line for " + type +
                    " must have at least " + need + " tokens");
        }
    }

    private static int getI(String[] t, int idx, String name) {
        try {
            return Integer.parseInt(t[idx]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid int for " + name + " at #" + idx);
        }
    }

    private static double getD(String[] t, int idx, String name) {
        try {
            return Double.parseDouble(t[idx]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid double for " + name + " at #" + idx);
        }
    }
}