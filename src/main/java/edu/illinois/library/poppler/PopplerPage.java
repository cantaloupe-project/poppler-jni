package edu.illinois.library.poppler;

import java.util.Arrays;

/**
 * <p>Equivalent of a
 * <a href="https://poppler.freedesktop.org/api/cpp/classpoppler_1_1page.html">
 * poppler::page</a>.</p>
 *
 * <p><strong>N.B.: instances must be {@link #nativeDestroy() destroyed} when
 * they are no longer needed.</strong></p>
 */
public final class PopplerPage {

    public enum Orientation {
        LANDSCAPE(1), PORTRAIT(2), SEASCAPE(3), UPSIDE_DOWN(4);

        private final int id;

        Orientation(int id) {
            this.id = id;
        }
    }

    long nativePtr;

    static {
        System.loadLibrary(Constants.LIBRARY_NAME);
    }

    protected PopplerPage(long ptr) {
        this.nativePtr = ptr;
    }

    public native String getLabel();

    public Orientation getOrientation() {
        return Arrays.stream(Orientation.values())
                .filter(o -> o.id == orientation())
                .findFirst()
                .orElse(Orientation.PORTRAIT);
    }

    public Dimension getPageRect() {
        double[] arr = pageRect();
        return new Dimension(arr[0], arr[1]);
    }

    public native void nativeDestroy();

    private native int orientation();

    /**
     * @return Two-element width/height array.
     */
    private native double[] pageRect();

}
