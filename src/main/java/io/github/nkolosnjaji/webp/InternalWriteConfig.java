package io.github.nkolosnjaji.webp;

import io.github.nkolosnjaji.webp.exceptions.WebPEncodingException;
import io.github.nkolosnjaji.webp.exceptions.WebPWrongVersionException;
import io.github.nkolosnjaji.webp.gen.LibWebP;
import io.github.nkolosnjaji.webp.gen.WebPConfig;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Objects;

import static io.github.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_INVALID_CONFIGURATION;

class InternalWriteConfig {

    private final MemorySegment ms;

    public InternalWriteConfig(Arena arena, WebPWriterParam param) {
        Objects.requireNonNull(arena, "arena must not be null");
        Objects.requireNonNull(param, "param must not be null");
        this.ms = WebPConfig.allocate(arena);
        final float quality = param.getCompressionQuality() * 100;

        if (LibWebP.WebPConfigInitInternal(ms, this.mapWebPPreset(param.getPreset()), quality, LibWebP.WEBP_ENCODER_ABI_VERSION()) != 1) {
            throw new WebPWrongVersionException();
        }

        if (WebPWriterParam.COMPRESSION_LOSSLESS.equals(param.getCompressionType())) {
            WebPConfig.lossless(this.ms, 1);
        }

        if (param.getMethod() != null) {
            WebPConfig.method(this.ms, param.getMethod().getValue());
        }

        if (param.getImageHint() != null) {
            WebPConfig.image_hint(this.ms, this.mapWebPHint(param.getImageHint()));
        }

        if (Boolean.TRUE.equals(param.getMultiThreading())) {
            WebPConfig.thread_level(this.ms, 1);
        }
        if (Boolean.TRUE.equals(param.getLowMemory())) {
            WebPConfig.low_memory(this.ms, 1);
        }

        // validate
        if (LibWebP.WebPValidateConfig(this.ms) != 1) {
            throw new WebPEncodingException(VP8_ENC_ERROR_INVALID_CONFIGURATION());
        }

    }

    public MemorySegment getMemorySegment() {
        return this.ms;
    }

    private int mapWebPPreset(WebPWriterParam.WebPWriterPreset writerPreset) {
        return switch (writerPreset) {
            case PICTURE -> LibWebP.WEBP_PRESET_PICTURE();
            case PHOTO -> LibWebP.WEBP_PRESET_PHOTO();
            case DRAWING -> LibWebP.WEBP_PRESET_DRAWING();
            case ICON -> LibWebP.WEBP_PRESET_ICON();
            case TEXT -> LibWebP.WEBP_PRESET_TEXT();
            case null, default -> LibWebP.WEBP_PRESET_DEFAULT();
        };
    }

    private int mapWebPHint(WebPWriterParam.WebPWriterHint hint) {
        return switch (hint) {
            case PHOTO -> LibWebP.WEBP_HINT_PHOTO();
            case PICTURE -> LibWebP.WEBP_HINT_PICTURE();
            case GRAPH -> LibWebP.WEBP_HINT_GRAPH();
            case null, default -> LibWebP.WEBP_PRESET_DEFAULT();
        };
    }

}
