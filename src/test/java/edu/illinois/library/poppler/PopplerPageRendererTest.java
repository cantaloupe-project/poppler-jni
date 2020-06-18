package edu.illinois.library.poppler;

import edu.illinois.library.poppler.test.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class PopplerPageRendererTest {

    private PopplerPageRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new PopplerPageRenderer();
        renderer.setRenderHint(PopplerPageRenderer.RenderHint.ANTIALIASING);
        renderer.setRenderHint(PopplerPageRenderer.RenderHint.TEXT_ANTIALIASING);
        renderer.setRenderHint(PopplerPageRenderer.RenderHint.TEXT_HINTING);
    }

    @AfterEach
    void tearDown() {
        if (renderer != null) {
            renderer.nativeDestroy();
        }
    }

    @Test
    void canRender() {
        assertTrue(renderer.canRender());
    }

    @Test
    void getImageFormat() {
        assertEquals(PopplerImage.Format.ARGB32, renderer.getImageFormat());
    }

    @Test
    void renderPage() throws Exception {
        final Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        PopplerDocument doc = null;
        PopplerPage page    = null;
        try {
            doc                 = PopplerDocument.load(fixture.toString());
            page                = doc.getPage(0);
            final int dpi       = 72;
            final double scale  = dpi / 72.0;
            double width        = page.getPageRect().width() * scale;
            double height       = page.getPageRect().height() * scale;
            switch (page.getOrientation()) {
                case LANDSCAPE:
                case SEASCAPE:
                    double tmp = width;
                    width      = height;
                    height     = tmp;
                    break;
            }
            final int intWidth  = (int) Math.round(width);
            final int intHeight = (int) Math.round(height);

            PopplerImage image = renderer.renderPage(
                    page, dpi, dpi, 0, 0, intWidth, intHeight);
            assertTrue(image.data().length > 35000);
            assertEquals(PopplerImage.Format.ARGB32, image.getFormat());
            assertEquals(2448, image.getBytesPerRow());
            assertEquals(612, image.getWidth());
            assertEquals(792, image.getHeight());
        } finally {
            if (doc != null) {
                doc.nativeDestroy();
            }
            if (page != null) {
                page.nativeDestroy();
            }
        }
    }

    /**
     * This basically just verifies that the library is suited for use in a
     * multi-threaded environment with different threads rendering different
     * pages.
     */
    @Test
    void renderPageThreadSafety() throws Exception {
        final int threadCount      = 100;
        final Path fixture         = TestUtils.getFixture("NEXTSTEP.pdf");
        final CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                final PopplerPageRenderer renderer = new PopplerPageRenderer();
                PopplerDocument doc = null;
                PopplerPage page = null;
                try {
                    doc = PopplerDocument.load(fixture.toString());
                    page = doc.getPage(0);
                    final int dpi = 150;
                    final double scale = dpi / 72.0;
                    double width = page.getPageRect().width() * scale;
                    double height = page.getPageRect().height() * scale;
                    switch (page.getOrientation()) {
                        case LANDSCAPE:
                        case SEASCAPE:
                            double tmp = width;
                            width = height;
                            height = tmp;
                            break;
                    }
                    renderer.renderPage(page, dpi, dpi, 0, 0,
                            (int) Math.round(width),
                            (int) Math.round(height));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                } finally {
                    renderer.nativeDestroy();
                    if (doc != null) {
                        doc.nativeDestroy();
                    }
                    if (page != null) {
                        page.nativeDestroy();
                    }
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
    }

    @Disabled // The format doesn't stick after being set. Is this a bug in poppler?
    @Test
    void setImageFormat() {
        renderer.setImageFormat(PopplerImage.Format.GRAY8);
        assertEquals(PopplerImage.Format.GRAY8, renderer.getImageFormat());
    }

    @Disabled // TODO: this is just a test, get rid of it
    @Test
    void writeBufferedImage() throws Exception {
        final Path fixture = TestUtils.getFixture("NEXTSTEP.pdf");
        PopplerDocument doc = null;
        PopplerPage page    = null;
        try {
            doc  = PopplerDocument.load(fixture.toString());
            page = doc.getPage(0);
            final int dpi = 150;
            double scale = dpi / 72.0;
            double width = page.getPageRect().width() * scale;
            double height = page.getPageRect().height() * scale;
            switch (page.getOrientation()) {
                case LANDSCAPE:
                case SEASCAPE:
                    double tmp = width;
                    width = height;
                    height = tmp;
                    break;
            }
            final int intWidth = (int) Math.round(width);
            final int intHeight = (int) Math.round(height);

            PopplerImage image = renderer.renderPage(
                    page, dpi, dpi, 0, 0, intWidth, intHeight);
            BufferedImage bImage = newBufferedImage(image);
            ImageIO.write(bImage, "PNG", new File("/Users/alexd/Desktop/out.png"));
        } finally {
            if (doc != null) {
                doc.nativeDestroy();
            }
            if (page != null) {
                page.nativeDestroy();
            }
        }
    }

    BufferedImage newBufferedImage(PopplerImage popplerImage) {
        int imageType;
        switch (popplerImage.getFormat()) {
            case MONO:
                imageType = BufferedImage.TYPE_BYTE_BINARY;
                break;
            case RGB24:
                imageType = BufferedImage.TYPE_INT_RGB;
                break;
            case ARGB32:
                imageType = BufferedImage.TYPE_INT_ARGB;
                break;
            case GRAY8:
                imageType = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case BGR24:
                imageType = BufferedImage.TYPE_INT_BGR;
                break;
            default:
                throw new IllegalArgumentException("Unknown image type");
        }

        final byte[] byteData = popplerImage.data();
        final int[] intData   = new int[byteData.length];
        switch (imageType) {
            case BufferedImage.TYPE_BYTE_BINARY:
            case BufferedImage.TYPE_BYTE_GRAY:  // TODO: verify this
                for (int i = 0; i < byteData.length - 1; i++) {
                    intData[i] = byteData[i];
                }
                break;
            case BufferedImage.TYPE_INT_RGB:  // TODO: verify this
                for (int i = 0; i < byteData.length - 3; i += 3) {
                    intData[i]     = byteData[i + 2]; // R
                    intData[i + 1] = byteData[i + 1]; // G
                    intData[i + 2] = byteData[i];     // B
                }
                break;
            case BufferedImage.TYPE_INT_ARGB:
                for (int i = 0; i < byteData.length - 4; i += 4) {
                    intData[i]     = byteData[i + 2]; // R
                    intData[i + 1] = byteData[i + 1]; // G
                    intData[i + 2] = byteData[i];     // B
                    intData[i + 3] = byteData[i + 3]; // A
                }
                break;
            case BufferedImage.TYPE_INT_BGR: // TODO: verify this
                for (int i = 0; i < byteData.length - 3; i += 3) {
                    intData[i]     = byteData[i];     // B
                    intData[i + 1] = byteData[i + 1]; // G
                    intData[i + 2] = byteData[i + 2]; // R
                }
                break;
        }

        final BufferedImage bufImage = new BufferedImage(
                popplerImage.getWidth(),
                popplerImage.getHeight(), imageType);
        bufImage.getRaster().setPixels(
                0, 0, bufImage.getWidth(), bufImage.getHeight(), intData);
        return bufImage;
    }

}