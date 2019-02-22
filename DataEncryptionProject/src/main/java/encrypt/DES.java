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

    private long key;
    private long[] roundScheduledKeys;
    private final byte lastBitMask = 0x1;
    private final long rightHandMask = 0xFFFFFFFF;
    private final int[] rotationTable = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
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
    private final int[] keyCompressionTable
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
     * @param key The key used in encryption.
     */
    public DES(long key) {
        this.key = key;
        this.roundScheduledKeys = roundKeyGenerators(key);
    }

    @Override
    public long encrypt(long input) {
        long cipher = input;
        for (int round = 0; round < 16; round++) {
            cipher = (feistelFunction(cipher, roundScheduledKeys[round]));
        }
        return cipher;
    }

    @Override
    public long decrypt(long input) {
        long plain = input;
        for (int round = 15; round >= 0; round--) {
            plain = (feistelFunction(plain, roundScheduledKeys[round]));
        }
        return plain;
    }

    //Should be 64-bit (word) input and the 48-bit round. Outputs the round cipher for that word
    private long feistelFunction(long inputBits, long roundKey) {
        long rightOriginal = inputBits & this.rightHandMask;
        long newRight = rightOriginal;
        long leftOriginal = (inputBits >> 32) & rightHandMask;
        newRight = this.expansionFunction(newRight) ^ roundKey; //Broken up for readability
        newRight = this.permutationFunction(newRight);
        newRight = newRight ^ leftOriginal;
        return newRight | (rightOriginal << 32); // New left-side is old right-side as is and new left-side is calculated with functions
    }

    //Generates keys for 16 rounds needed for the encryption, each entry represents the 48-bit round key for current round.
    private long[] roundKeyGenerators(long key) {
        long[] roundKeys = new long[16];
        long permutatedKey = permutedChoiceOne(key);
        BinaryVector64 binVec = new BinaryVector64(permutatedKey);
        for (int i = 0; i < roundKeys.length; i++) {
            //Shifts the bits, some extra work is needed to compensate for the long (64-bit) usage as a container for 24-bits
            for (int shift = 0; shift < this.rotationTable[i]; shift++) {
                binVec.leftShift();
                binVec.swap(0, 28);
                binVec.swap(28, 56);
            }
            roundKeys[i] = permutedChoiceTwo(binVec.getAsLong());
        }
        return roundKeys;
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

    //Helper function for the subtitution boxing of the round feistel function, should take 48-bits as input and output 32-bits
    private long substitutionHelper(long input, int round) {
        long substituted = 0b0;
        long sixBitMask = 0b111111;
        for (int i = 0; i < 8; i++) {
            substituted = substituted | calculateSBox(i + 1, (input >> i) & sixBitMask);
            substituted = substituted << 4;
        }
        return substituted;
    }

    //Replaces the given 6 bits with 4 using the given substitution box
    private long calculateSBox(int sBox, long bits) {
        long innerBits = (bits & 0b011110) >> 1;
        long outerBits = lastBitMask & bits;
        long tmp = lastBitMask & (bits >> 5);
        tmp = tmp << 1;
        outerBits = outerBits | tmp;
        return SubstitutionBox.getSboxValues(sBox, outerBits, innerBits);
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

    //Round key scheduling permutations (returns 48bit subkey from the 56 key schedule state)
    private long permutedChoiceTwo(long initial) {
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        for (int i = this.keyCompressionTable.length - 1; i >= 0; i--) {
            finalVector.placeToIndex(i, initialVector.getIndexValue(this.permutationTable[i]));
        }
        return finalVector.getAsLong();
    }
}
