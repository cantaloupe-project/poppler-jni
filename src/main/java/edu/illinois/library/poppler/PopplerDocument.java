package edu.illinois.library.poppler;

import java.io.IOException;
import java.time.Instant;

/**
 * <p>Equivalent to a
 * <a href="https://poppler.freedesktop.org/api/cpp/classpoppler_1_1document.html">
 * poppler::document</a>.</p>
 *
 * <p><strong>N.B.: instances must be {@link #nativeDestroy() destroyed} when
 * they are no longer needed.</strong></p>
 */
public final class PopplerDocument {

    static {
        System.loadLibrary(Constants.LIBRARY_NAME);
    }

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final long nativePtr;

    /**
     * @throws IllegalArgumentException if the given data could not be parsed
     *         as a PDF document.
     */
    public static PopplerDocument load(byte[] data) {
        final long ptr = loadFromData(data);
        if (ptr != 0) {
            return new PopplerDocument(ptr);
        }
        throw new IllegalArgumentException("Failed to parse data");
    }

    public static PopplerDocument load(String pathname) throws IOException {
        final long ptr = loadFromFile(pathname);
        if (ptr != 0) {
            return new PopplerDocument(ptr);
        }
        throw new IOException("Failed to load " + pathname);
    }

    private static native long loadFromData(byte[] data);

    private static native long loadFromFile(String pathname);

    protected PopplerDocument(long ptr) {
        this.nativePtr = ptr;
    }

    private native long createPage(int index);

    public native String getAuthor();

    public Instant getCreationDate() {
        return Instant.ofEpochSecond(getNativeCreationDate());
    }

    private native int getNativeCreationDate();

    public native String getCreator();

    public native String getKeywords();

    public Instant getModificationDate() {
        return Instant.ofEpochSecond(getNativeModificationDate());
    }

    private native int getNativeModificationDate();

    /**
     * @param index Zero-based page index.
     * @throws IndexOutOfBoundsException if a page does not exist at the given
     *         index.
     */
    public PopplerPage getPage(int index) {
        final long ptr = createPage(index);
        if (ptr != 0) {
            return new PopplerPage(ptr);
        }
        throw new IndexOutOfBoundsException(
                "Page does not exist at index " + index);
    }

    public native String getProducer();

    public native String getSubject();

    public native String getTitle();

    public native boolean isEncrypted();

    public native boolean isLocked();

    public native String metadata();

    public native void nativeDestroy();

    public native int numPages();

}
