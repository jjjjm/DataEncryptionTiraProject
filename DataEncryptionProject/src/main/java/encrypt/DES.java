package encrypt;

import IO.FileHandler;
import util.BinaryMatrix64;

/**
 * Class implements the DES encryption algorithm, both enciphering and
 * deciphering
 */
public class DES implements Encryption {

    private String key;
    private FileHandler fileHandler;
    private final byte lastBitMask = 0x1;
    private final long leftMask = 0x0000FFFF;
    private final long rightMask = 0xFFFF0000;

    /**
     * Creates a new DES-class
     *
     * @param key The key used in encryption, should be 4 characters long since
     * Java encodes each char as 2 bytes (UTF-16) and the key length should be
     * 64 bits
     */
    public DES(String key) {
        if (key.getBytes().length == 8) {
            this.key = key;
        } else {
            throw new ExceptionInInitializerError("Not a valid key");
        }
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

    // Takes the first 64-bits (8 bytes) as an input and returns the permutated as per Richard Outbridges Initial permutation
    private long initialPermutationFuction(long initial) {
        BinaryMatrix64 binMat = new BinaryMatrix64(initial);
        binMat.transpose();
        for (int x = 0; x < 4; x++) {
            binMat.swapColumn(x, 7 - x);
        }
        for (int round = 0; round < 4; round++) {
            for (int x = 0 + round; x < 8 - round; x += 2) {
                binMat.swapRow(x, x + 1);
            }
        }

        return binMat.getAsLongPrimitive();
    }

    //Inversed initialPermutationFunction
    private long finalPermutationFunction(long initial) {
        BinaryMatrix64 binMat = new BinaryMatrix64(initial);
        binMat.transpose();
        for (int x = 0; x < 4; x++) {
            binMat.swapRow(x, 7 - x);
        }
        for (int round = 0; round < 4; round++) {
            for (int x = 0 + round; x < 4; x += 1) {
                binMat.swapColumn(x, x + (4 - round));
            }
        }

        return binMat.getAsLongPrimitive();
    }

}
