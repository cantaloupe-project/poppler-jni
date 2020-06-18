package edu.illinois.library.poppler;

import java.util.Arrays;

/**
 * <p>Equivalent to a
 * <a href="https://poppler.freedesktop.org/api/cpp/classpoppler_1_1page__renderer.html">
 * poppler::page_renderer</a>.</p>
 *
 * <p><strong>N.B.: instances must be {@link #nativeDestroy() destroyed} when
 * they are no longer needed.</strong></p>
 */
public final class PopplerPageRenderer {

    public enum RenderHint {
        ANTIALIASING(1),
        TEXT_ANTIALIASING(2),
        TEXT_HINTING(3);

        private final int id;

        RenderHint(int id) {
            this.id = id;
        }
    }

    @SuppressWarnings("unused") private long nativePtr;

    static {
        System.loadLibrary(Constants.LIBRARY_NAME);
    }

    public PopplerPageRenderer() {
        initialize();
    }

    private native void initialize();
    public native void nativeDestroy();
    private native int nativeGetImageFormat();
    private native void nativeSetImageFormat(int formatID);
    private native void nativeSetRenderHint(int hintID);

    /**
     * <p>Rendering capability test.</p>
     *
     * <p>Rendering is only possible if a render backend ("Splash") is compiled
     * in Poppler.</p>
     */
    public native boolean canRender();

    /**
     * @return The image format used when rendering ({@link
     *         PopplerImage.Format#ARGB32} by default).
     */
    public PopplerImage.Format getImageFormat() {
        return Arrays.stream(PopplerImage.Format.values())
                .filter(f -> f.id == nativeGetImageFormat())
                .findFirst()
                .orElse(PopplerImage.Format.INVALID);
    }

    public native PopplerImage renderPage(PopplerPage page,
                                          double xRes,
                                          double yRes,
                                          int x,
                                          int y,
                                          int width,
                                          int height);

    public void setImageFormat(PopplerImage.Format format) {
        this.nativeSetImageFormat(format.id);
    }

    public void setRenderHint(RenderHint hint) {
        this.nativeSetRenderHint(hint.id);
    }



}
