package EncryptTest;

import encrypt.Encryption;
import encrypt.RC4;
import encrypt.RC5;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EncryptionTest {

    Encryption e;

    @Test
    public void RC4EncryptionDecryptionQuickTest() {
        byte[] keyBytes = {1, 2, 3, 4, 5, 6, 7};
        this.e = new RC4(7, keyBytes);
        long testData = 0l;
        testData = e.encrypt(testData);
        this.e = new RC4(7, keyBytes);
        assertEquals(0l, e.decrypt(testData));
    }

    @Test
    public void RC5EncryptionDecryptionQuickTest() {
        byte[] keyBytes = new byte[32];
        this.e = new RC5(32, 12, keyBytes);
        long testData = 0l;
        testData = e.encrypt(testData);
        assertEquals(0l, e.decrypt(testData));
    }

}
