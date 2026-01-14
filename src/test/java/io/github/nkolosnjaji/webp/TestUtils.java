package io.github.nkolosnjaji.webp;

import io.github.nkolosnjaji.webp.gen.LibWebP;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestUtils {

    public static final Path WORKING_PATH = Path.of(System.getProperty("user.dir"));

    public static final String INPUT_DIR = "src/test/resources/images/input";

    public static final String CWEBP_DIR = "src/test/resources/images/cwebp";

    public static final String OUTPUT_DIR = "target/test-classes/images/output";

    private TestUtils() {
    }

    public static Path getInputPath(String name) {
        return WORKING_PATH.resolve(INPUT_DIR, "%s.png".formatted(name));
    }

    public static Path getGeneratedPath(String name) {
        return WORKING_PATH.resolve(CWEBP_DIR, "%s.webp".formatted(name));
    }

    public static Path getOutputPath(String name) {
        return WORKING_PATH.resolve(OUTPUT_DIR, "%s.webp".formatted(name));
    }

    public static Path getOutputPath() {
        return WORKING_PATH.resolve(OUTPUT_DIR);
    }

    public static void assertEqualFiles(Path source, String imageName) {
        Path generated = getGeneratedPath(imageName);
        byte[] sourceBytes = assertDoesNotThrow(() -> Files.readAllBytes(source));
        byte[] generatedBytes = assertDoesNotThrow(() -> Files.readAllBytes(generated));

        CRC32 sourceChecksum = new CRC32();
        sourceChecksum.update(sourceBytes);
        final long sourceValue = sourceChecksum.getValue();

        CRC32 generatedChecksum = new CRC32();
        generatedChecksum.update(generatedBytes);
        final long generatedValue = generatedChecksum.getValue();

        assertEquals(sourceValue, generatedValue, "Checksums of source and generated pictures are not equal");
    }

//    void generate() throws IOException {
//        BufferedImage image = new BufferedImage(LibWebP.WEBP_MAX_DIMENSION(), LibWebP.WEBP_MAX_DIMENSION(), BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = image.createGraphics();
//        g2d.setColor(new Color(50, 150, 250, 200));
//        g2d.fillRect(0, 0, LibWebP.WEBP_MAX_DIMENSION(), LibWebP.WEBP_MAX_DIMENSION());
//        g2d.dispose();
//
//        var rootDir = System.getProperty("user.dir");
//        ImageIO.write(image, "png", new File(rootDir + "/1.png"));
//    }
}
