package encrypt;

import util.*;

/**
 * Class implements the DES encryption algorithm, both enciphering and
 * deciphering <\br>
 * All functions can be seen in graphical form for reference at
 * https://en.wikipedia.org/wiki/DES_supplementary_material
 * 
 * !!NOTE!!
 * Doesn't actually work
 */
public class DES implements Encryption {

    private long key;
    private long[] roundScheduledKeys;
    private final long lastBitMask = 0b1l;
    private long rightHandMask;
    private final int[] rotationTable = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private final int[] permutationTable
            = {15, 6, 19, 20, 28, 11, 27, 16,
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
    private final int[] initPermutation = {
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7,
        56, 48, 40, 32, 24, 16, 8, 0,
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 7
    };

    private final int[] finalPermutation = {
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25,
        32, 0, 40, 8, 48, 16, 56, 24
    };

    private final int[] expansionTable = {
        31, 0, 1, 2, 3, 4,
        3, 4, 5, 6, 7, 8,
        7, 8, 9, 10, 11, 12,
        11, 12, 13, 14, 15, 16,
        15, 16, 17, 18, 19, 20,
        19, 20, 21, 22, 23, 24,
        23, 24, 25, 26, 27, 28,
        27, 28, 29, 30, 31, 0
    };

    /**
     * Creates a new DES-class
     *
     * @param key The key used in encryption.
     */
    public DES(long key) {
        this.key = key;
        this.roundScheduledKeys = roundKeyGenerators(key);
        this.rightHandMask = this.make64BitMask(0, 31);
    }

    @Override
    public long encrypt(long input) {
        long cipher = input;
        cipher = initialPermutationFuction(cipher);
        for (int round = 0; round < 16; round++) {
            cipher = (feistelFunction(cipher, roundScheduledKeys[round]));
        }
        cipher = finalPermutationFunction(cipher);
        return cipher;
    }

    @Override
    public long decrypt(long input) {
        long plain = input;
        plain = initialPermutationFuction(plain);
        for (int round = 15; round >= 0; round--) {
            plain = (feistelFunction(plain, roundScheduledKeys[round]));
        }
        plain = finalPermutationFunction(plain);
        return plain;
    }

    //Should be 64-bit (word) input and the 48-bit round. Outputs the round cipher for that word
    private long feistelFunction(long inputBits, long roundKey) {
        long rightOriginal = inputBits & this.rightHandMask;
        long newRight = rightOriginal;
        long leftOriginal = (inputBits >> 32l) & rightHandMask;
        newRight = this.expansionFunction(newRight) ^ roundKey; //Broken up for readability
//        newRight = this.substitutionHelper(newRight);
//        newRight = this.permutationFunction(newRight);
        newRight = newRight ^ leftOriginal;
        return newRight | (rightOriginal << 32l); // New left-side is old right-side as is and new left-side is calculated with functions
    }

    //Generates keys for 16 rounds needed for the encryption, each entry represents the 48-bit round key for current round.
    private long[] roundKeyGenerators(long key) {
        long[] roundKeys = new long[16];
        long permutatedKey = permutedChoiceOne(key);
        BinaryVector64 binVec = new BinaryVector64(permutatedKey);
        for (int i = 0; i < roundKeys.length; i++) {
            //Shifts the bits, some extra work is needed to compensate for the long (64-bit) usage as a container for 2 x 28-bits
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
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        for (int i = 0; i < this.initPermutation.length; i++) {
            finalVector.placeToIndex(i, initialVector.getIndexValue(this.initPermutation[i]));
        }
        return finalVector.getAsLong();
    }

    //Inversed initialPermutationFunction
    private long finalPermutationFunction(long initial) {
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        for (int i = 0; i < this.finalPermutation.length; i++) {
            finalVector.placeToIndex(i, initialVector.getIndexValue(this.finalPermutation[i]));
        }
        return finalVector.getAsLong();
    }

    //Expansion (E) function for DES
    private long expansionFunction(long initial) {
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        for (int i = 0; i < this.expansionTable.length; i++) {
            finalVector.placeToIndex(i, initialVector.getIndexValue(this.expansionTable[i]));
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

    //Helper function for the subtitution boxing of the round feistel function, should take 48-bits as input and outputs 32-bits
    private long substitutionHelper(long input) {
        long substituted = 0b0l;
        long[] bits = new long[8];
        long sixBitMask = this.make64BitMask(0l, 5l);
        for (int i = 0; i < 8; i++) {
            bits[7 - i] = input >> (i * 6) & sixBitMask;
        }
        for (int i = 0; i < 7; i++) {
            substituted = substituted | calculateSBox(i + 1, bits[i]);
            substituted = substituted << 4;
        }
        substituted = substituted | calculateSBox(8, bits[7]);
        return substituted;
    }

    //Replaces the given 6 bits with 4 using the given substitution box
    private long calculateSBox(int sBox, long bits) {
        long innerBits = (bits & this.make64BitMask(1l, 4l)) >> 1;
        long outerBits = lastBitMask & bits;
        long tmp = lastBitMask & (bits >> 5l);
        tmp = tmp << 1;
        outerBits = outerBits | tmp;
        return SubstitutionBox.getSboxValues(sBox, outerBits, innerBits);
    }

    //Key scheduling functions follows
    //Permuted choice 1 function (PC-1)
    private long permutedChoiceOne(long initial) {
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        for (int i = 0; i < permutedChoice1Table.length; i++) {
            finalVector.placeToIndex(i, initialVector.getIndexValue(this.permutedChoice1Table[i]));
        }
        return finalVector.getAsLong();
    }

    //Round key scheduling permutations (returns 48bit subkey from the 56 key schedule state)
    private long permutedChoiceTwo(long initial) {
        BinaryVector64 initialVector = new BinaryVector64(initial);
        BinaryVector64 finalVector = new BinaryVector64(0b0l);
        for (int i = 0; i < keyCompressionTable[i]; i++) {
            finalVector.placeToIndex(i, initialVector.getIndexValue(this.keyCompressionTable[i]));
        }
        return finalVector.getAsLong();
    }

    private long make64BitMask(long fromBit, long toBit) {
        long mask = 0b0l;
        long maskBit;
        for (long i = 0; i < 64; i++) {
            maskBit = (0b1l << i);
            if (i >= fromBit && i <= toBit) {
                mask = mask | maskBit;
            }
        }
        return mask;
    }
}
