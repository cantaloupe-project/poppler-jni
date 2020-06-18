package edu.illinois.library.poppler;

import java.util.Arrays;

/**
 * <p>Equivalent to a
 * <a href="https://poppler.freedesktop.org/api/cpp/classpoppler_1_1image.html">
 * poppler::image</a>.</p>
 *
 * <p>Instances are created on the native stack and there is no need to
 * manually free them.</p>
 */
public final class PopplerImage {

    public enum Format {
        INVALID(0), MONO(1), RGB24(2), ARGB32(3), GRAY8(4), BGR24(5);

        final int id;

        Format(int id) {
            this.id = id;
        }
    }

    @SuppressWarnings("unused") private byte[] data;
    @SuppressWarnings("unused") private int format, width, height, bytesPerRow;

    public int getBytesPerRow() {
        return bytesPerRow;
    }

    /**
     * @return Raw raster data. Use {@link #getFormat()} to find out what kind.
     */
    public byte[] data() {
        return data;
    }

    public Format getFormat() {
        return Arrays.stream(Format.values())
                .filter(f -> f.id == format)
                .findFirst()
                .orElse(Format.INVALID);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}
