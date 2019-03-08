package UtilTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import util.BinaryVector64;

/**
 *
 * @author jermusto
 */
public class BinaryVector64Test {

    BinaryVector64 binVec;

    public BinaryVector64Test() {
    }

    @Before
    public void setUp() {
        binVec = new BinaryVector64(2l);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void binaryVectorConstructionAndReturningWorks() {
        assertTrue(binVec.getAsLong() == 2l);
    }

    @Test
    public void vectorSwapWorks() {
        binVec.swap(1, 0);
        assertTrue(binVec.getAsLong() == 1l);
    }

    @Test
    public void vectorReplacementWorks() {
        binVec.replace(1, 0);
        assertTrue(binVec.getAsLong() == 3l);
    }

    @Test
    public void vectorSettingBitWorks() {
        binVec.placeToIndex(2, 0b1l);
        assertTrue(binVec.getAsLong() == 6l);
    }

    @Test
    public void vectorLeftShiftWorks() {
        binVec.leftShift();
        assertTrue(binVec.getAsLong() == 4l);
    }

}
