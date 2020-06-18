package edu.illinois.library.poppler;

import edu.illinois.library.poppler.test.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PopplerDocumentTest {

    private PopplerDocument instance;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        if (instance != null) {
            instance.nativeDestroy();
        }
    }

    /* load(byte[]) */

    @Test
    void loadWithDataWithValidPDF() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        byte[] data = Files.readAllBytes(fixture);
        instance = PopplerDocument.load(data);
        assertNotNull(instance);
    }

    @Test
    void loadWithDataWithInvalidPDF() throws Exception {
        Path fixture = TestUtils.getFixture("jpg.jpg");
        byte[] data = Files.readAllBytes(fixture);
        assertThrows(IllegalArgumentException.class,
                () -> PopplerDocument.load(data));
    }

    /* load(String) */

    @Test
    void loadWithFileWithValidPDF() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertNotNull(instance);
    }

    @Test
    void loadWithFileWithInvalidPDF() {
        Path fixture = TestUtils.getFixture("jpg.jpg");
        assertThrows(IOException.class, () ->
                PopplerDocument.load(fixture.toString()));
    }

    /* getAuthor() */

    @Test
    void getAuthor() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-metadata.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals("Sample Author", instance.getAuthor());
    }

    /* getCreationDate() */

    @Test
    void getCreationDate() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals(Instant.ofEpochSecond(1592684051),
                instance.getCreationDate());
    }

    /* getCreator() */

    @Test
    void getCreator() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-metadata.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals("Pages", instance.getCreator());
    }

    /* getKeywords() */

    @Test
    void getKeywords() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-metadata.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals("Sample Keyword", instance.getKeywords());
    }

    /* getModificationDate() */

    @Test
    void getModificationDate() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals(Instant.ofEpochSecond(1592684156),
                instance.getModificationDate());
    }

    /* getPage() */

    @Test
    void getPage() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertNotNull(instance.getPage(0));
        assertNotNull(instance.getPage(1));
    }

    @Test
    void getPageWithIllegalPage() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertThrows(IndexOutOfBoundsException.class,
                () -> instance.getPage(3));
    }

    /* getProducer() */

    @Test
    void getProducer() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-metadata.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals("macOS Version 10.15.5 (Build 19F101) Quartz PDFContext, AppendMode 1.1",
                instance.getProducer());
    }

    /* getSubject() */

    @Test
    void getSubject() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-metadata.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals("Sample Subject", instance.getSubject());
    }

    /* getTitle() */

    @Test
    void getTitle() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-metadata.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals("Sample Title", instance.getTitle());
    }

    /* isEncrypted() */

    @Test
    @Disabled // TODO: why does this fail?
    void isEncryptedWithEncryptedPDF() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-encrypted.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertTrue(instance.isEncrypted());
    }

    @Test
    void isEncryptedWithUnencryptedPDF() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertFalse(instance.isEncrypted());
    }

    /* isLocked() */

    @Test
    void isLockedWithEncryptedPDF() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-encrypted.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertTrue(instance.isLocked());
    }

    @Test
    void isLockedWithUnencryptedPDF() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertFalse(instance.isLocked());
    }

    /* metadata() */

    @Test
    void metadata() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-xmp.pdf");
        instance = PopplerDocument.load(fixture.toString());
        String metadata = instance.metadata();
        assertTrue(metadata.startsWith("<?xpacket begin"));
        assertTrue(metadata.endsWith("<?xpacket end='w'?>"));
    }

    /* numPages() */

    @Test
    void numPages() throws Exception {
        Path fixture = TestUtils.getFixture("pdf-multipage.pdf");
        instance = PopplerDocument.load(fixture.toString());
        assertEquals(2, instance.numPages());
    }

}