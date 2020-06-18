package edu.illinois.library.poppler;

import edu.illinois.library.poppler.test.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PopplerPageTest {

    private static final double DELTA = 0.00000001;

    private PopplerDocument doc;
    private PopplerPage page;

    @AfterEach
    void tearDown() {
        if (doc != null) {
            doc.nativeDestroy();
        }
        if (page != null) {
            page.nativeDestroy();
        }
    }

    /* getLabel() */

    @Test
    void getLabel() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        doc = PopplerDocument.load(fixture.toString());
        page = doc.getPage(0);
        assertEquals("1", page.getLabel());
    }

    /* getOrientation() */

    @Test
    void getOrientationOfPortrait() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        doc = PopplerDocument.load(fixture.toString());
        page = doc.getPage(0);
        assertEquals(PopplerPage.Orientation.PORTRAIT, page.getOrientation());
    }

    @Test
    void getOrientationOfLandscape() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-orientation-landscape.pdf");
        doc = PopplerDocument.load(fixture.toString());
        page = doc.getPage(0);
        assertEquals(PopplerPage.Orientation.LANDSCAPE, page.getOrientation());
    }

    @Test
    void getOrientationOfSeascape() {
        // TODO: write this
    }

    @Test
    void getOrientationOfUpsideDown() {
        // TODO: write this
    }

    /* getPageRect() */

    @Test
    void getPageRect() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        doc = PopplerDocument.load(fixture.toString());
        page = doc.getPage(0);
        Dimension rect = page.getPageRect();
        assertEquals(612, rect.width(), DELTA);
        assertEquals(792, rect.height(), DELTA);
    }

}