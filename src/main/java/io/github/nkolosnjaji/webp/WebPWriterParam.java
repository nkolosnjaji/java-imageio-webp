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

    private Integer compressionMethod;

    private Integer sharpness;
    private WebPWriterPreset preset;
    private WebPWriterHint imageHint;
    private WebPWriterAlphaFilter alphaFilter;

    private boolean multiThreading;
    private boolean lowMemory;
    private boolean useSharpYuv;
    private boolean autoFilter;

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

    public Integer getCompressionMethod() {
        return compressionMethod;
    }

    /**
     * Set compression method/speed
     *
     * @param compressionMethod (0=fast, 6=slowest), default=4
     */
    public void setCompressionMethod(Integer compressionMethod) {
        this.compressionMethod = compressionMethod;
    }

    public Integer getSharpness() {
        return sharpness;
    }

    /**
     *
     * @param sharpness filter sharpness (0:most .. 7:least sharp), default=0
     */
    public void setSharpness(Integer sharpness) {
        this.sharpness = sharpness;
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

    public boolean isMultiThreading() {
        return multiThreading;
    }

    /**
     * enable/disable multi-threading (if available)
     *
     * @param multiThreading boolean value to enable/disable multi-threading
     */
    public void setMultiThreading(boolean multiThreading) {
        this.multiThreading = multiThreading;
    }

    public Resize getResize() {
        return resize;
    }

    public void setResize(Resize resize) {
        this.resize = resize;
    }

    public boolean isLowMemory() {
        return lowMemory;
    }

    /**
     * reduce memory usage with slower encoding
     *
     * @param lowMemory boolean value to enable/disable low memory usage mode
     */
    public void setLowMemory(boolean lowMemory) {
        this.lowMemory = lowMemory;
    }

    public WebPWriterAlphaFilter getAlphaFilter() {
        return alphaFilter;
    }

    /**
     * Predictive filtering for alpha plane
     *
     * @param alphaFilter one of none, fast, best
     */
    public void setAlphaFilter(WebPWriterAlphaFilter alphaFilter) {
        this.alphaFilter = alphaFilter;
    }

    public boolean isUseSharpYuv() {
        return useSharpYuv;
    }

    /**
     * use sharper (and slower) RGB->YUV conversion
     *
     * @param useSharpYuv boolean value to enable/disable sharp YUV conversion
     */
    public void setUseSharpYuv(boolean useSharpYuv) {
        this.useSharpYuv = useSharpYuv;
    }

    public boolean isAutoFilter() {
        return autoFilter;
    }

    /**
     * Auto-adjust filter strength
     *
     * @param autoFilter boolean parameter
     */
    public void setAutoFilter(boolean autoFilter) {
        this.autoFilter = autoFilter;
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

    public enum WebPWriterAlphaFilter {
        NONE(0),
        FAST(1),
        BEST(2);

        private final int value;

        WebPWriterAlphaFilter(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
