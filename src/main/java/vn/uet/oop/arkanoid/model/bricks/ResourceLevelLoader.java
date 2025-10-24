// java
// File: src/main/java/vn/uet/oop/arkanoid/model/bricks/ResourceLevelLoader.java
package vn.uet.oop.arkanoid.model.bricks;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public final class ResourceLevelLoader {
    private ResourceLevelLoader() {}

    public static List<Brick> loadFromResource(String resourcePath) throws IOException {
        String trimmed = (resourcePath == null) ? "" : (resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath);
        InputStream in = null;

        // 1) Standard classpath lookups
        in = ResourceLevelLoader.class.getResourceAsStream("/" + trimmed);
        if (in == null) in = Thread.currentThread().getContextClassLoader().getResourceAsStream(trimmed);
        if (in == null) in = ClassLoader.getSystemResourceAsStream(trimmed);

        // 2) Try path that matches your current Java-package layout:
        //    vn/uet/oop/arkanoid/resources/levels/<file>
        if (in == null && trimmed.contains("/")) {
            String fileName = trimmed.substring(trimmed.lastIndexOf('/') + 1);
            String pkgPath = "vn/uet/oop/arkanoid/resources/levels/" + fileName;
            in = ResourceLevelLoader.class.getResourceAsStream("/" + pkgPath);
            if (in == null) in = Thread.currentThread().getContextClassLoader().getResourceAsStream(pkgPath);

            // 3) Filesystem fallback into src/main/java (only for development)
            if (in == null) {
                Path devPath = Paths.get("src", "main", "java", "vn", "uet", "oop", "arkanoid", "resources", "levels", fileName);
                if (Files.exists(devPath)) {
                    in = Files.newInputStream(devPath);
                }
            }
        }

        // 4) If still not found, log and return empty list to avoid throwing at runtime
        if (in == null) {
            System.err.println("ResourceLevelLoader: resource not found: /" + trimmed);
            System.err.println("Place the file at `src/main/resources/" + trimmed + "` (recommended) and rebuild.");
            return new ArrayList<>();
        }

        try (InputStream input = in) {
            Path temp = Files.createTempFile("level-", ".txt");
            temp.toFile().deleteOnExit();
            Files.copy(input, temp, StandardCopyOption.REPLACE_EXISTING);
            return TxtLevelLoader.load(temp);
        }
    }
}
