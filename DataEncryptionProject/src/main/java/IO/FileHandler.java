package IO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class used as an interface between the algorithms classes and the file
 * system.
 */
public class FileHandler {

    private final File inputFile;
    private final File outputFile;
    private FileOutputStream outputStream;
    private FileInputStream inputStream;
    private boolean fileEnd;

    /**
     * Creates a new file handler class and initialises the file objects for the
     * interface
     *
     * @param fileName the name (and location) of the file
     */
    public FileHandler(String fileName) {
        this.fileEnd = false;
        this.inputFile = new File(fileName);
        this.outputFile = new File(fileName + ".encrypted");
    }

    /**
     * Used to check if the current file has ended
     * @return true if all the bytes in the file has been read else false;
     */
    public boolean isFileEnd() {
        return fileEnd;
    }

    /**
     * Return the next byte of data from the designated file;
     *
     * @return
     * @throws FileNotFoundException If the file designated doesn't exist
     */
    public byte nextFileByte() throws FileNotFoundException {
        inputStream = new FileInputStream(inputFile);
        try {
            if (inputStream.available() > 0) {
                return (byte) inputStream.read();
            } else {
                this.fileEnd = true;
                inputStream.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return -1;
    }
}
