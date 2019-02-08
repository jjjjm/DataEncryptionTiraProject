package encrypt;

import IO.FileHandler;
import util.*;

/**
 * Class implements the DES encryption algorithm, both enciphering and
 * deciphering <\br>
 * All functions can be seen in graphical form for reference at
 * https://en.wikipedia.org/wiki/DES_supplementary_material
 */
public class DES implements Encryption {

    private String key;
    private FileHandler fileHandler;
    private final byte lastBitMask = 0x1;
    private final long leftMask = 0x0000FFFF;
    private final long rightMask = 0xFFFF0000;
    private final int[] permutationTable
            = {15, 7, 19, 20, 28, 11, 27, 17,
                0, 14, 22, 25, 4, 17, 30, 9,
                1, 7, 23, 13, 31, 26, 2, 8,
                18, 12, 29, 5, 21, 10, 3, 24};
    private final int[] permutedChoice1Table
            = {56, 48, 40, 32, 24, 16, 8, //left
                0, 57, 49, 41, 33, 25, 17,
                9, 1, 58, 50, 42, 34, 26,
                18, 10, 2, 59, 51, 43, 35,
                62, 54, 46, 38, 30, 22, 14, //right
                6, 61, 53, 45, 37, 29, 21,
                13, 5, 60, 52, 44, 36, 28,
                20, 12, 4, 27, 19, 11, 3,};
    private final int[] permutedChoice2Table
            = {13, 16, 10, 23, 0, 4,
                2, 27, 14, 5, 20, 9,
                22, 18, 11, 3, 25, 7,
                15, 6, 26, 19, 12, 1,
                40, 51, 30, 36, 46, 54,
                29, 39, 50, 44, 32, 47,
                43, 48, 38, 55, 33, 52,
                45, 41, 49, 35, 28, 31
            };

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

    //Expansion (E) function for DES
    private long expansionFunction(long initial) {
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        finalVector.placeToIndex(47, initialVector.getIndexValue(0));
        finalVector.placeToIndex(0, initialVector.getIndexValue(47));
        int bitPositionDisplacementInput = 0;
        int bitPositionDisplacementOutput = 1;
        for (int round = 0; round < 8; round++) {
            for (int sub = 0; sub < 4; sub++) {
                long currentBit = initialVector.getIndexValue(bitPositionDisplacementInput);
                if (round != 0 && sub == 0) {
                    finalVector.placeToIndex(bitPositionDisplacementOutput, currentBit);
                    finalVector.placeToIndex(bitPositionDisplacementOutput - 2, currentBit);
                } else if (round != 7 && sub == 3) {
                    finalVector.placeToIndex(bitPositionDisplacementOutput, currentBit);
                    finalVector.placeToIndex(bitPositionDisplacementOutput + 2, currentBit);
                    bitPositionDisplacementOutput += 3;
                } else {
                    finalVector.placeToIndex(bitPositionDisplacementOutput, currentBit);
                }
                bitPositionDisplacementInput++;
                bitPositionDisplacementOutput++;
            }
        }
        return finalVector.getAsLong();
    }

    //Permutation (P) function for DES
    private long permutationFunction(long initial) {
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        for (int i = 0; i < this.permutationTable.length; i++) {
            finalVector.placeToIndex(i, initialVector.getIndexValue(this.permutationTable[i]));
        }
        return finalVector.getAsLong();
    }

    //Key scheduling functions follows
    //Permuted choice 1 function (PC-1)
    private long permutedChoiceOne(long initial) {
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        for (int i = this.permutedChoice1Table.length - 1; i >= 0; i--) {
            finalVector.placeToIndex(i, initialVector.getIndexValue(this.permutationTable[i]));
        }
        return finalVector.getAsLong();
    }

    //Replaces the given 6 bits with 4 using the given substitution box
    private long calculateSBox(int sBox, long bits) {
        long innerBits = (bits & 0b011110) >> 1;
        long outerBits = lastBitMask & bits;
        outerBits = outerBits << 1;
        long tmp = lastBitMask & (bits >> 5);
        outerBits = outerBits | tmp;
        return SubstitutionBox.getSboxValues(sBox, outerBits, innerBits);
    }
}
