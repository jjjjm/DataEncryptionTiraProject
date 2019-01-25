package encrypt;

import IO.FileHandler;
import check.ParityCheck;

/**
 * Class implements the DES encryption algorithm, both enciphering and
 * deciphering
 */
public class DES implements Encryption {

    private String key;
    private FileHandler fileHandler;
    private final ParityCheck parityChecker = new ParityCheck();
    private final byte lastBitMask = 0b00000001;

    /**
     * Creates a new DES-class
     *
     * @param key The key used in encryption, should be 4 characters long since
     * Java encodes each char as 2 bytes (UTF-16) and the key length should be
     * 64 bits
     */
    public DES(String key) {
        if (validateKey(key)) {
            this.key = key;
        } else {
            throw new ExceptionInInitializerError("Not a valid key");
        }
    }

    private boolean validateKey(String key) {
        byte[] keyBytes = key.getBytes();
        return keyBytes.length == 8 && this.parityChecker.CheckDES(key);
    }

    @Override
    public void encrypt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrypt() {
        throw new UnsupportedOperationException();
    }
    
    private byte removeParityBitsFromKey() {
        return 0;
    }

    // Takes the first 64-bits (8 bytes) as an input and does Richard Outbridges Initial permutation function
    private void initialPermutationFuction(Byte[] bytes) {
        
    }

}
