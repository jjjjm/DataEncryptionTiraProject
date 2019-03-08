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
     * @param extension The extension given to the file
     * @param encrypted Check to determine if the file currently being read is
     * encrypted.
     * @throws java.io.FileNotFoundException
     */
    public FileHandler(String fileName, String extension, boolean encrypted) throws FileNotFoundException {
        this.fileEnd = false;
        this.inputFile = new File(fileName);
        inputStream = new FileInputStream(inputFile);
        if (!encrypted) {
            this.outputFile = new File(fileName + "." + extension + "encrypted");
        } else {
            this.outputFile = new File(fileName.substring(0, fileName.length() - 13));
        }
        outputStream = new FileOutputStream(outputFile);
    }

    /**
     * Used to check if the current file has ended
     *
     * @return true if all the bytes in the file has been read else false;
     * @throws java.io.IOException If the file designated doesn't exist
     */
    public boolean isFileEnd() throws IOException {
        return inputStream.available() <= 0;
    }

    /**
     * Return the next byte of data from the designated file;
     *
     * @return
     * @throws FileNotFoundException If the file designated doesn't exist
     */
    public byte nextFileByte() throws FileNotFoundException {
        try {
            if (inputStream.available() > 0) {
                return (byte) inputStream.read();
            } else {
                this.fileEnd = true;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return 0;
    }

    /**
     * Writes the the given byte to the output file. Also creates the given file
     * if it doesn't exist.
     *
     * @param byteToWrite
     * @throws IOException
     */
    public void writeByte(byte byteToWrite) throws IOException {
        this.outputStream.write(byteToWrite);
    }
    /**
     * Closes the output stream
     * @throws IOException 
     */
    public void closeStreams() throws IOException {
        this.outputStream.close();
    }

}
