package IO;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

public class FileHandlerTest {

    String fileName;
    File testFile;
    FileHandler fh;

    public FileHandlerTest() {
        this.fileName = "testFile.txt";
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        File file = new File("fileName.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        File file = new File("testFile.txt");
        if (!file.exists()) {
            file.delete();
        }
    }

    @Before
    public void setUp() {
        this.testFile = new File(this.fileName);
    }

    @After
    public void tearDown() throws IOException {
        this.testFile.delete();
        this.testFile.createNewFile();
    }

    @Test
    public void testIsFileEnd() throws Exception {
        this.fh = new FileHandler(fileName, "", false);
        assertTrue(fh.isFileEnd());
    }

}
