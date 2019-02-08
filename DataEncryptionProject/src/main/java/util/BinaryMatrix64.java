package util;

/**
 * Utility helper for cryptological algorithms and transformations. It it used
 * to represent 64-bits as a bit matrix. Bits are positioned in a row major
 * format:
 * <br>
 * <br>01 02 03 04 05 06 07 08
 * <br>09 10 11 12 13 14 15 16
 * <br>17 18 19 20 21 22 23 24
 * <br>25 26 27 28 29 30 31 32
 * <br>33 34 35 36 37 38 39 40
 * <br>41 42 43 44 45 46 47 48
 * <br>49 50 51 52 53 54 55 56
 * <br>57 58 59 60 61 62 63 64
 */
public class BinaryMatrix64 {

    final private long[][] matrix = new long[8][8];
    final private long lastBitMask = 0b1;

    /**
     * Creates a new BinaryMatrix and with zeros, so it represents 0 @Long
     */
    public BinaryMatrix64() {

    }

    /**
     * Creates new binary matrix from the existing and initialises it with the
     * given bits
     *
     * @param l Used to initialise the matrix
     */
    public BinaryMatrix64(long l) {
        long tmp = l;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                matrix[x][y] = tmp & lastBitMask; //Mask everything else as zero and set ONLY the least significant bit as to the matrix;
                tmp = tmp >> 1;
            }
        }
    }

    /**
     * Transfers the BinaryMatrix class to long primitive i.e. 64-bit binary
     * number.
     *
     * @return the value of the matrix as long primitive
     */
    public long getAsLongPrimitive() {
        long longValue = 0;
        for (int x = 7; x >= 0; x--) {
            for (int y = 7; y >= 0; y--) {
                longValue = longValue | matrix[x][y];
                if (!(x == 0 && y == 0)) 
                    longValue = longValue << 1;
            }
        }
        return longValue;
    }

    /**
     * Applies a matrix transpose to the current matrix
     */
    public void transpose() {
        for (int x = 0; x < 6; x++) {
            for (int y = x + 1; y < 7; y++) {
                swap(x, y, y, x);
            }
        }
    }

    /**
     * Swaps a row and column with each other, since the matrix is fixed size
     * 64-bit binary number the ranges of both constructors are 0-7 inclusive
     *
     * @param row
     * @param column
     */
    public void swapRowAndColumn(int row, int column) {
        for (int o = 0; o < 8; o++) {
            swap(row, o, o, column);
        }
    }

    /**
     * Swaps the a row with another, ranges for constructors 0,7 inclusive
     *
     * @param row1
     * @param row2
     */
    public void swapRow(int row1, int row2) {
        for (int o = 0; o < 8; o++) {
            swap(row1, o, row2, o);
        }
    }

    /**
     * Swaps a column with another, ranges for constructors 0,7 inclusive
     *
     * @param column1
     * @param column2
     */
    public void swapColumn(int column1, int column2) {
        for (int o = 0; o < 8; o++) {
            swap(o, column1, o, column2);
        }

    }

    /*
    Helper function for swapping methods.
     */
    private void swap(int row1, int column1, int row2, int column2) {
        long tmp = matrix[row1][column1];
        matrix[row1][column1] = matrix[row2][column2];
        matrix[row2][column2] = tmp;
    }

}
