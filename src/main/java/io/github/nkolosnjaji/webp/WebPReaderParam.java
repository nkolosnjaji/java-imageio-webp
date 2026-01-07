package io.github.nkolosnjaji.webp;

import javax.imageio.ImageReadParam;

public final class WebPReaderParam extends ImageReadParam {

    private Crop crop;

    private Resize resize;

    private Boolean flip;

    private Boolean multiThreading;

    public Crop getCrop() {
        return crop;
    }

    /**
     * Set crop parameters with a given rectangle
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
     * @param resize parameter
     */
    public void setResize(Resize resize) {
        this.resize = resize;
    }

    public Boolean getFlip() {
        return flip;
    }

    /**
     * Flip the image vertically
     * @param flip parameter
     */
    public void setFlip(Boolean flip) {
        this.flip = flip;
    }

    public Boolean getMultiThreading() {
        return multiThreading;
    }

    /**
     * enable/disable multi-threading (if available)
     * @param multiThreading boolean value to enable/disable multi-threading
     */
    public void setMultiThreading(Boolean multiThreading) {
        this.multiThreading = multiThreading;
    }
}
