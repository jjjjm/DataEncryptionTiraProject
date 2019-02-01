package util;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryMatrix64Test {

    BinaryMatrix64 binMat;

    @Test
    public void asLongPrimitiveReturnsOriginal() {
        long expResult = 1234567l;
        binMat = new BinaryMatrix64(expResult);
        assertEquals(binMat.getAsLongPrimitive(), expResult);
    }

    @Test
    public void transposeTest() {
        long original = 2l;
        long expected = 256l;
        binMat = new BinaryMatrix64(original);
        binMat.transpose();
        assertEquals(expected, binMat.getAsLongPrimitive());
    }

    @Test
    public void testSwapRowAndColumn() {
        long original = 2l;
        long expected = 512l;
        binMat = new BinaryMatrix64(original);
        binMat.swapRowAndColumn(0, 1);
        assertEquals(expected, binMat.getAsLongPrimitive());
    }

    @Test
    public void testSwapRow() {
        long original = 1l;
        long expected = 256l;
        binMat = new BinaryMatrix64(original);
        binMat.swapRow(0, 1);
        assertEquals(expected, binMat.getAsLongPrimitive());
    }

    @Test
    public void testSwapColumn() {
        long original = 1l;
        long expected = 2l;
        binMat = new BinaryMatrix64(original);
        binMat.swapRow(1, 2);
        assertEquals(expected, binMat.getAsLongPrimitive());
    }

}
