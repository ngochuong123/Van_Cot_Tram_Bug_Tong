package vn.uet.oop.arkanoid.model.bricks;
import vn.uet.oop.arkanoid.model.bricks.Brick;
import vn.uet.oop.arkanoid.model.bricks.BrickFactory;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public final class TxtLevelLoader {
    private TxtLevelLoader() {}

    /**
     * Load level from a text file.
     *
     * @param file the path file
     * @return list of bricks
     * @throws IOException if I/O errors
     */
    public static List<Brick> load(Path file) throws IOException {
        List<Brick> list = new ArrayList<>();
        for (String raw : Files.readAllLines(file)) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] tok = line.split("\\s+");
            Brick b = BrickFactory.createFromTokens(tok);
            list.add(b);
        }
        return list;
    }
}