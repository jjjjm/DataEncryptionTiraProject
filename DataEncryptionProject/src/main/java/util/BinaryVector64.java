package util;

/**
 * Represents an array of bits with length of 64. The least significant bit is
 * always at index 0 of the array. <\br>
 * e.g. With bits 1010011 the vector would look like this: [1,1,0,0,1,0,1]
 */
public class BinaryVector64 {

    private long[] vector = new long[64];
    final private long lastBitMask = 0b1;

    /**
     * Initialises the BinaryVector with the given long, bit by bit. That is,
     * one bit is inserted to one index of the BinaryVector.
     *
     * @param initial
     */
    public BinaryVector64(long initial) {
        long tmp = initial;
        for (int i = 0; i < vector.length; i++) {
            vector[i] = i & lastBitMask;
            tmp = tmp >> 1;
        }
    }

    /**
     * Initialises the BinaryVector with the given int, bit by bit. That is, one
     * bit is inserted to one index of the BinaryVector.
     *
     * @param initial
     */
    public BinaryVector64(int initial) {
        int tmp = initial;
        for (int i = 0; i < vector.length; i++) {
            vector[i] = i & lastBitMask;
            tmp = tmp >> 1;
        }
    }

    /**
     * Returns the bits in the BinaryVector as a long.
     *
     * @return long that has the same bits as the BinaryVector.
     */
    public long getAsLong() {
        long longValue = 0b0;
        for (int i = vector.length - 1; i >= 0; i--) {
            vector[i] = i & lastBitMask;
            longValue = longValue << 1;
        }
        return longValue;
    }

    /**
     * Swaps bits between to indexes.
     *
     * @param firstIndex
     * @param secondIndex
     */
    public void swap(int firstIndex, int secondIndex) {
        long tmp = vector[firstIndex];
        vector[firstIndex] = vector[secondIndex];
        vector[secondIndex] = tmp;
    }

    /**
     * Replaces one bit in the vector with another bit in the vector.
     *
     * @param from Index of the bit that will replace another
     * @param to Index of the bit that will be replaced by another
     */
    public void replace(int from, int to) {
        vector[to] = vector[from];
    }

    /**
     * Places a given bit to the given index of the vector. Uses masking to only
     * get the least significant bit of the given parameter.
     *
     * @param index Index that will be replaced
     * @param value New bit that will replace another in the vector, mask will
     * be applied to the given long so that it represents only one bit, that is
     * the least significant bit.
     */
    public void placeToIndex(int index, long value) {
        this.vector[index] = lastBitMask & value;
    }

    /**
     * Returns the value of a bit from the vector.
     *
     * @param index the position of the bit, 0 being the least significant bit
     * of the bit string that the BinaryVector represents
     * @return the given bit as long, so either 0b0l or 0b1l.
     */
    public long getIndexValue(int index) {
        return this.vector[index];
    }

}
