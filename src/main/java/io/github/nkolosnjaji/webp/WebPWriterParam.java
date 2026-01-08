package io.github.nkolosnjaji.webp;

import javax.imageio.ImageWriteParam;
import java.util.Set;

public final class WebPWriterParam extends ImageWriteParam {
    /**
     * Compression type Lossy
     */
    public static final String COMPRESSION_LOSSY = "Lossy";

    /**
     * Compression type Lossless
     */
    public static final String COMPRESSION_LOSSLESS = "Lossless";

    private static final float DEFAULT_COMPRESSION_QUALITY = 0.75F;

    /**
     * Supported compression types
     */
    public static final Set<String> compressions = Set.of(COMPRESSION_LOSSLESS, COMPRESSION_LOSSY);

    private WebPWriterMethod method;
    private WebPWriterPreset preset;
    private WebPWriterHint imageHint;

    private Boolean multiThreading;
    private Boolean lowMemory;
    private Crop crop = null;
    private Resize resize = null;


    /**
     * Default constructor with default compression (0.75)
     */
    public WebPWriterParam() {
        this(DEFAULT_COMPRESSION_QUALITY, WebPWriterPreset.DEFAULT);
    }

    /**
     * Constructor with preset
     *
     * @param preset (@see WebPWriterPreset) to use with default quality compression (0.75)
     */
    public WebPWriterParam(WebPWriterPreset preset) {
        this(DEFAULT_COMPRESSION_QUALITY, preset);
    }

    /**
     * Constructor with quality
     *
     * @param compressionQuality quality compression with default preset (@see WebPWriterPreset)
     */
    public WebPWriterParam(float compressionQuality) {
        this(compressionQuality, WebPWriterPreset.DEFAULT);
    }

    /**
     * Constructor with quality compression and preset (@see WebPWriterPreset)
     *
     * @param compressionQuality quality compression
     * @param preset             (@see WebPWriterPreset) predefined preset
     */
    public WebPWriterParam(float compressionQuality, WebPWriterPreset preset) {
        super.canWriteCompressed = true;
        super.compressionTypes = compressions.toArray(String[]::new);
        super.setCompressionMode(MODE_EXPLICIT);
        super.setCompressionType(COMPRESSION_LOSSY);
        super.setCompressionQuality(compressionQuality);
        this.setPreset(preset);
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public WebPWriterMethod getMethod() {
        return method;
    }

    /**
     * Set compression method/speed
     *
     * @param method compression method (@see WebPWriterMethod)
     */
    public void setMethod(WebPWriterMethod method) {
        this.method = method;
    }


    public WebPWriterPreset getPreset() {
        return preset;
    }

    /**
     * Set predefined preset
     *
     * @param preset (@see WebPWriterPreset)
     */
    public void setPreset(WebPWriterPreset preset) {
        this.preset = preset;
    }

    public WebPWriterHint getImageHint() {
        return imageHint;
    }

    /**
     * set image characteristics hint
     *
     * @param imageHint (@see WebPWriterHint) image hint
     */
    public void setImageHint(WebPWriterHint imageHint) {
        this.imageHint = imageHint;
    }

    public Boolean getMultiThreading() {
        return multiThreading;
    }

    /**
     * enable/disable multi-threading (if available)
     *
     * @param multiThreading boolean value to enable/disable multi-threading
     */
    public void setMultiThreading(Boolean multiThreading) {
        this.multiThreading = multiThreading;
    }

    public Resize getResize() {
        return resize;
    }

    public void setResize(Resize resize) {
        this.resize = resize;
    }

    public Boolean getLowMemory() {
        return lowMemory;
    }

    /**
     * reduce memory usage with slower encoding
     *
     * @param lowMemory boolean value to enable/disable low memory usage mode
     */
    public void setLowMemory(Boolean lowMemory) {
        this.lowMemory = lowMemory;
    }

    public enum WebPWriterPreset {
        DEFAULT,
        PHOTO,
        PICTURE,
        DRAWING,
        ICON,
        TEXT
    }

    public enum WebPWriterHint {
        DEFAULT,
        PICTURE,
        PHOTO,
        GRAPH
    }

    public enum WebPWriterMethod {
        FASTEST(0),
        FAST(1),
        MEDIUM(2),
        NORMAL(3),
        DEFAULT(4),
        SLOW(5),
        SLOWEST(6);

        private final int value;

        WebPWriterMethod(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
