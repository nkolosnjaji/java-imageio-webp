package io.github.nkolosnjaji.webp;

import io.github.nkolosnjaji.webp.exceptions.WebPDecodingException;
import io.github.nkolosnjaji.webp.exceptions.WebPEncodingException;
import io.github.nkolosnjaji.webp.exceptions.WebPFormatException;
import io.github.nkolosnjaji.webp.gen.LibWebP;
import io.github.nkolosnjaji.webp.gen.WebPBitstreamFeatures;
import io.github.nkolosnjaji.webp.gen.WebPDecBuffer;
import io.github.nkolosnjaji.webp.gen.WebPPicture;
import io.github.nkolosnjaji.webp.gen.WebPRGBABuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Objects;

import static io.github.nkolosnjaji.webp.gen.LibWebP.WebPFreeDecBuffer;

final class WebP {

    private static final Logger logger = LoggerFactory.getLogger(WebP.class);

    static {
        Path libSharpYuv = OsUtils.getPathLibSharpYuv();
        System.load(libSharpYuv.toFile().getAbsolutePath());

        Path libWebP = OsUtils.getPathLibWebP();
        System.load(libWebP.toFile().getAbsolutePath());

        logger.debug("WebP library successfully loaded");
    }

    public static void encode(RenderedImage image, ImageWriteParam imageWriteParam, Object out) throws IOException {
        Objects.requireNonNull(image, "Image must not be null");

        if (out instanceof Path path) {
            encodeInternal(image, path, imageWriteParam);
        } else {
            throw new IllegalStateException("out parameter must be %s".formatted(Path.class.getSimpleName()));
        }
    }

    private static void encodeInternal(RenderedImage image, Path path, ImageWriteParam imageWriteParam) throws IOException {
        WebPWriterParam param = switch (imageWriteParam) {
            case WebPWriterParam wwp -> wwp;
            case ImageWriteParam iwp -> new WebPWriterParam(iwp.getCompressionQuality());
            case null -> new WebPWriterParam();
        };

        InternalWriter writer = null;
        InternalPicture picture = null;

        try (Arena arena = Arena.ofConfined()) {
            try {
                picture = new InternalPicture(arena, image);
                picture.cropAndResize(param);

                writer = new InternalWriter(arena, picture, path);

                InternalWriteConfig config = new InternalWriteConfig(arena, param);

                final int result = LibWebP.WebPEncode(config.getMemorySegment(), picture.getMemorySegment());
                if (result != 1) {
                    throw new WebPEncodingException(WebPPicture.error_code(picture.getMemorySegment()));
                }
            } finally {
                if (writer != null) writer.free();
                if (picture != null) picture.free();
            }
        }
    }

    public static Header getHeader(ImageInputStream iis) throws IOException {
        try (Arena arena = Arena.ofConfined()) {
            byte[] bytes;
            bytes = new byte[32];
            iis.readFully(bytes); // read 1st 32 bytes
            MemorySegment data = arena.allocate(bytes.length);

            for (int x = 0; x < bytes.length; x++) {
                data.setAtIndex(ValueLayout.JAVA_BYTE, x, bytes[x]);
            }

            MemorySegment features = WebPBitstreamFeatures.allocate(arena);
            int result = LibWebP.WebPGetFeaturesInternal(data, data.byteSize(), features, LibWebP.WEBP_DECODER_ABI_VERSION());

            if (result == LibWebP.VP8_STATUS_OK()) {
                final int width = WebPBitstreamFeatures.width(features);
                final int height = WebPBitstreamFeatures.height(features);
                final boolean hasAlpha = WebPBitstreamFeatures.has_alpha(features) == 1;
                final boolean hasAnimation = WebPBitstreamFeatures.has_animation(features) == 1;
                final int format = WebPBitstreamFeatures.format(features);
                return new Header(new Dimension(width, height), hasAlpha, hasAnimation, format);
            } else {
                throw new WebPFormatException();
            }
        }
    }

    public static BufferedImage decode(ImageInputStream iis, Header header, WebPReaderParam param) throws IOException {
        try (Arena arena = Arena.ofConfined()) {
            InternalReader reader = null;
            try {
                reader = new InternalReader(arena, header, param);
                MemorySegment rawImage = InternalReader.createRaw(arena, iis);
                int result = LibWebP.WebPDecode(rawImage, rawImage.byteSize(), reader.getMemorySegment());

                if (result != LibWebP.VP8_STATUS_OK()) {
                    throw new WebPDecodingException(result);
                }

                MemorySegment buffer = reader.getBuffer();
                MemorySegment union = WebPDecBuffer.u(buffer);

                final long bufferSize = WebPRGBABuffer.size(union);
                final ByteBuffer byteBuffer = WebPRGBABuffer.rgba(union).asSlice(0, bufferSize).asByteBuffer();

                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);

                Dimension dimension = reader.getImageDimensionAfterDecoding();

                return generateBufferedImage(dimension, bytes, header.hasAlpha());
            } finally {
                if (reader != null) WebPFreeDecBuffer(reader.getBuffer());
            }
        }
    }

    private static BufferedImage generateBufferedImage(Dimension dimension, byte[] bytes, boolean hasAlpha) {
        final int width = dimension.width();
        final int height = dimension.height();

        int channels = hasAlpha ? 4 : 3;
        int[] bandOffsets = hasAlpha ? new int[]{0, 1, 2, 3} : new int[]{0, 1, 2}; // RGBA or RGB order

        PixelInterleavedSampleModel sampleModel = new PixelInterleavedSampleModel(
                DataBuffer.TYPE_BYTE, width, height, channels, width * channels, bandOffsets);

        DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);

        WritableRaster raster = Raster.createWritableRaster(sampleModel, buffer, new Point(0, 0));

        ComponentColorModel colorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_sRGB),
                hasAlpha,
                false,  // not premultiplied
                hasAlpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE,
                DataBuffer.TYPE_BYTE);

        return new BufferedImage(colorModel, raster, false, null);
    }

    public record Header(Dimension dimension, boolean hasAlpha, boolean hasAnimation, int format) {
    }

}
