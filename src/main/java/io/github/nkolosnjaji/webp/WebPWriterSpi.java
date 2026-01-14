package io.github.nkolosnjaji.webp;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

public final class WebPWriterSpi extends ImageWriterSpi {

    public WebPWriterSpi() {
        super("Nikola Kološnjaji",
                "1.0",
                new String[]{"WebP", "webp"},
                new String[]{"webp"},
                new String[]{"image/webp", "webp"},
                WebPWriter.class.getName(),
                new Class[]{Path.class},
                new String[]{WebPReaderSpi.class.getName()},
                false,
                null,
                null,
                null,
                null,
                false,
                null,
                null,
                null,
                null);
    }

    @Override
    public boolean canEncodeImage(ImageTypeSpecifier type) {
        return type.getSampleModel().getDataType() == DataBuffer.TYPE_BYTE;
    }

    @Override
    public ImageWriter createWriterInstance(Object extension) throws IOException {
        return new WebPWriter(this);
    }

    @Override
    public String getDescription(Locale locale) {
        return "WebP JAVA encoder using Java Foreign Function by Nikola Kološnjaji";
    }

}
