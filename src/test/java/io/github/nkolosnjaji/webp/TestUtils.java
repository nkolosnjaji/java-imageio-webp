package io.github.nkolosnjaji.webp;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Adler32;

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

        Adler32 sourceChecksum = new Adler32();
        sourceChecksum.update(sourceBytes);
        final long sourceValue = sourceChecksum.getValue();

        Adler32 generatedChecksum = new Adler32();
        generatedChecksum.update(generatedBytes);
        final long generatedValue = generatedChecksum.getValue();

        assertEquals(sourceValue, generatedValue, "Checksums of source and generated pictures are not equal");
    }

    public static void assertEqualColors(BufferedImage image, Color expectedColor) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                assertEquals(expectedColor, new Color(image.getRGB(x, y)), "RGB colors not equal on x=%d,y=%d".formatted(x, y));
            }
        }
    }

    static void main() throws IOException {
        final ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/png").next();
        File outputFile = new File("/Users/n.kolosnjaji/playground/oss/java-imageio-webp/rgba.png");

        // Image dimensions
        int width = 1280;
        int height = 720;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Color colorWithAlpha = new Color(50, 150, 250, 200);

        // Fill the entire image efficiently using Graphics2D
        java.awt.Graphics2D g2d = image.createGraphics();
        g2d.setColor(colorWithAlpha);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        ImageIO.write(image, "png", outputFile);
        System.out.println(outputFile.isFile());
        System.out.println(outputFile.length() > 0);

    }
}
