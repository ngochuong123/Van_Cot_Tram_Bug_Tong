package vn.uet.oop.arkanoid.model.bricks;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public final class ResourceLevelLoader {
    private ResourceLevelLoader() {}

    /**
     * Load level from resource file.
     *
     * @param resourcePath the resource path
     * @return list of bricks
     * @throws IOException if I/O errors
     */
    public static List<Brick> loadFromResource(String resourcePath) throws IOException {
        if (resourcePath == null || resourcePath.isEmpty()) {
            System.err.println("ResourceLevelLoader: invalid resource path");
            return new ArrayList<>();
        }

        String p = resourcePath.replace('\\','/').trim();
        if (p.startsWith("/")) p = p.substring(1);

        String fileName = p.substring(p.lastIndexOf('/') + 1);
        String classpathPath = "levels/" + fileName;

        try (InputStream in = ResourceLevelLoader.class
                .getClassLoader()
                .getResourceAsStream(classpathPath)) {

            if (in == null) {
                System.err.println(" ResourceLevelLoader: resource not found: " + classpathPath);
                System.err.println("ensure file exist in: src/main/resources/" + classpathPath);
                return new ArrayList<>();
            }

            Path temp = Files.createTempFile("level-", ".txt");
            temp.toFile().deleteOnExit();
            Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
            return TxtLevelLoader.load(temp);
        }
    }
}
