package edu.illinois.library.poppler.test;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class TestUtils {

    public static File getCurrentWorkingDirectory() {
        try {
            return new File(".").getCanonicalFile();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Path getFixture(String filename) {
        return getFixturePath().resolve(filename);
    }

    /**
     * @return Path of the fixtures directory.
     */
    public static Path getFixturePath() {
        return Paths.get(getCurrentWorkingDirectory().getAbsolutePath(),
                "src", "test", "resources");
    }

    private TestUtils() {}

}
