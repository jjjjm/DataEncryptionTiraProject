package main;

import encrypt.*;
import IO.FileHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Main {

    static long mostMem;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String file = args[0];
        String encryption = args[1];
        String key = args[2];
        String encyptDecrypt = args[3];
        boolean isEncrypted = (encyptDecrypt.charAt(0) == 'e');
        System.out.println("");
        FileHandler fh = new FileHandler(file, encryption, isEncrypted);
        Encryption encrypt = null;
        byte[] keyBytes = key.getBytes("UTF-8");
        long millis = System.currentTimeMillis();
        mostMem = 0;
        if (encryption.equals("RC5")) {
            encrypt = new RC5(key.length(), 12, keyBytes);
            while (!fh.isFileEnd()) {
                memTest();
                long nextBytes = 0l;
                for (int i = 0; i < 3; i++) {
                    if (!fh.isFileEnd()) {
                        nextBytes = nextBytes | fh.nextFileByte();
                        nextBytes = nextBytes << 8;
                    }
                }
                if (!fh.isFileEnd()) {
                    nextBytes = nextBytes | fh.nextFileByte();
                }
                memTest();
                if (!isEncrypted) {
                    long encryptedBytes = encrypt.encrypt(nextBytes);
                    memTest();
                    for (int i = 0; i < 3; i++) {
                        byte asByte = (byte) (encryptedBytes & 0xFF);
                        encryptedBytes = encryptedBytes >> 8;
                        fh.writeByte(asByte);
                    }
                    fh.writeByte((byte) (encryptedBytes & 0xFF));
                } else {
                    long decryptedBytes = encrypt.decrypt(nextBytes);
                    memTest();
                    for (int i = 0; i < 3; i++) {
                        byte asByte = (byte) (decryptedBytes & 0xFF);
                        decryptedBytes = decryptedBytes >> 8;
                        fh.writeByte(asByte);
                    }
                }
            }
        } else if (encryption.equals(
                "RC4")) {
            encrypt = new RC4(keyBytes.length, keyBytes);
            while (!fh.isFileEnd()) {
                if (!isEncrypted) {
                    memTest();
                    long encryptedAsLong = encrypt.encrypt((long) fh.nextFileByte());
                    byte asByte = (byte) (encryptedAsLong & 0xFF);
                    fh.writeByte(asByte);
                } else {
                    memTest();
                    long decryptedAsLong = encrypt.decrypt((long) fh.nextFileByte());
                    byte asByte = (byte) (decryptedAsLong & 0xFF);
                    fh.writeByte(asByte);
                }
            }
        } else {
            throw new Error("Please give valid encryption scheme");
        }

        System.out.println(
                "Time taken: " + (System.currentTimeMillis() - millis) + "ms");
        System.out.println("Highest memory usage peak was: " + (mostMem / 1024l) + " Kb");
    }

    static void memTest() {
        long currentMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        if (mostMem < currentMem) {
            mostMem = currentMem;
        }
    }

}
