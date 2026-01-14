package io.github.nkolosnjaji.webp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

import static io.github.nkolosnjaji.webp.TestUtils.getGeneratedPath;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImageReaderTests {

    private static final Color EXPECTED_COLOR_RGB = new Color(50, 150, 250);

    private static final Color EXPECTED_COLOR_RGBA = new Color(50, 150, 250, 200);

    private final ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

    @Test
    void imageReaderIsRegistered() {
        assertNotNull(reader);
        Assertions.assertInstanceOf(WebPReaderParam.class, reader.getDefaultReadParam());
    }


    @ParameterizedTest
    @ValueSource(strings = {"reader_rgb", "reader_rgba"})
    void decodeDefault(String imageName) {
        Path input = getGeneratedPath(imageName);

        BufferedImage image = readImage(input);

        assertEqualColors(image);
        assertEquals(640, image.getWidth());
        assertEquals(480, image.getHeight());
    }

    @ParameterizedTest
    @ValueSource(strings = {"reader_rgb", "reader_rgba"})
    void decodeCrop(String imageName) {
        Crop crop = new Crop(100, 100, 50, 75);
        WebPReaderParam param = new WebPReaderParam();
        param.setCrop(crop);

        Path input = getGeneratedPath(imageName);
        BufferedImage image = readImage(input, param);

        assertEqualColors(image);
        assertEquals(50, image.getWidth());
        assertEquals(75, image.getHeight());

    }

    @ParameterizedTest
    @ValueSource(strings = {"reader_rgb", "reader_rgba"})
    void decodeResize(String imageName) {
        Resize resize = new Resize(25, 50);
        WebPReaderParam param = new WebPReaderParam();
        param.setResize(resize);

        Path input = getGeneratedPath(imageName);
        BufferedImage image = readImage(input, param);

        assertEqualColors(image);
        assertEquals(25, image.getWidth());
        assertEquals(50, image.getHeight());

    }

    private BufferedImage readImage(Path input) {
        return readImage(input, null);
    }

    private BufferedImage readImage(Path input, WebPReaderParam param) {
        assertDoesNotThrow(() -> reader.setInput(new FileImageInputStream(input.toFile())));
        return assertDoesNotThrow(() -> reader.read(0, param));
    }

    private void assertEqualColors(BufferedImage image) {
        final var hasAlpha = image.getColorModel().hasAlpha();
        final var expectedColor = hasAlpha ? EXPECTED_COLOR_RGBA : EXPECTED_COLOR_RGB;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                var color = new Color(image.getRGB(x, y), hasAlpha);
                assertEquals(expectedColor, color, "RGB colors not equal on x=%d,y=%d".formatted(x, y));
            }
        }
    }

}
