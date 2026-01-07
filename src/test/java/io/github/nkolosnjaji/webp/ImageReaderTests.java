package io.github.nkolosnjaji.webp;

import io.github.nkolosnjaji.webp.Crop;
import io.github.nkolosnjaji.webp.Resize;
import io.github.nkolosnjaji.webp.WebPReaderParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

import static io.github.nkolosnjaji.webp.TestUtils.assertEqualColors;
import static io.github.nkolosnjaji.webp.TestUtils.getGeneratedPath;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
class ImageReaderTests {

    private static final Color EXPECTED_COLOR = new Color(50,150,250, 200);

    private final ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

    @Test
    void imageReaderIsRegistered() {
        assertNotNull(reader);
        Assertions.assertInstanceOf(WebPReaderParam.class, reader.getDefaultReadParam());
    }


    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void decodeDefault(String imageName) {
        Path input = getGeneratedPath(imageName);

        BufferedImage image = readImage(input);

        assertEqualColors(image, EXPECTED_COLOR);
        assertEquals(500, image.getHeight());
        assertEquals(500, image.getWidth());
    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void decodeCrop(String imageName) {
        Crop crop = new Crop(100, 100, 50, 75);
        WebPReaderParam param = new WebPReaderParam();
        param.setCrop(crop);

        Path input = getGeneratedPath(imageName);
        BufferedImage image = readImage(input, param);

        assertEqualColors(image, EXPECTED_COLOR);
        assertEquals(50, image.getWidth());
        assertEquals(75, image.getHeight());

    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void decodeResize(String imageName) {
        Resize resize = new Resize(25, 50);
        WebPReaderParam param = new WebPReaderParam();
        param.setResize(resize);

        Path input = getGeneratedPath(imageName);
        BufferedImage image = readImage(input, param);

        assertEqualColors(image, EXPECTED_COLOR);
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

}
