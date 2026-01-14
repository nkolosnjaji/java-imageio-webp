package io.github.nkolosnjaji.webp;

import javax.imageio.ImageReadParam;

public final class WebPReaderParam extends ImageReadParam {

    private Crop crop;

    private Resize resize;

    private Integer dither;

    private boolean alphaDither;

    private boolean flip;

    private boolean noFancy;

    private boolean noFilter;

    private boolean noDither;

    private boolean multiThreading;

    public Crop getCrop() {
        return crop;
    }

    /**
     * Set crop parameters with a given rectangle
     *
     * @param crop parameter
     */
    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public Resize getResize() {
        return resize;
    }

    /**
     * Set resize (after any cropping)
     *
     * @param resize parameter
     */
    public void setResize(Resize resize) {
        this.resize = resize;
    }

    /**
     * Flip the image vertically
     *
     * @param flip boolean parameter
     */
    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public boolean isFlip() {
        return flip;
    }

    public boolean isNoFancy() {
        return noFancy;
    }

    /**
     * Don't use the fancy YUV420 upscaler
     *
     * @param noFancy boolean parameter
     */
    public void setNoFancy(boolean noFancy) {
        this.noFancy = noFancy;
    }

    public boolean isNoFilter() {
        return noFilter;
    }

    /**
     * Disable in-loop filtering
     *
     * @param noFilter boolean parameter
     */
    public void setNoFilter(boolean noFilter) {
        this.noFilter = noFilter;
    }

    public boolean isNoDither() {
        return noDither;
    }

    /**
     * Disable dithering
     *
     * @param noDither boolean parameter
     */
    public void setNoDither(boolean noDither) {
        this.noDither = noDither;
    }

    public Integer getDither() {
        return dither;
    }

    /**
     * Dithering strength
     *
     * @param dither strength parameter (0..100)
     */
    public void setDither(Integer dither) {
        if (dither < 0) {
            this.dither = 0;
        }
        else if (dither > 100) {
            this.dither = 100;
        }
        else this.dither = dither;
    }

    public boolean isAlphaDither() {
        return alphaDither;
    }

    /**
     * use alpha-plane dithering if needed
     *
     * @param alphaDither boolean parameter
     */
    public void setAlphaDither(boolean alphaDither) {
        this.alphaDither = alphaDither;
    }

    /**
     * enable/disable multi-threading (if available)
     *
     * @param multiThreading boolean value to enable/disable multi-threading
     */
    public void setMultiThreading(boolean multiThreading) {
        this.multiThreading = multiThreading;
    }
    public boolean isMultiThreading() {
        return multiThreading;
    }

}
