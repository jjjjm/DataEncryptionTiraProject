package encrypt;

public class RC4 implements Encryption {

    private long[] key;
    private final long[] S = new long[256];
    private final long[] T = new long[256];
    int i, j = 0; //Key generation pointers, needed here to make stream possible.

    private int keySize;

    /**
     * Creates a new RC4 class
     *
     * @param keySize the size of the key used, given in bytes, should range
     * from 5-256 inclusive
     * @param key The key as long primitives, maximum is 2048 bits so 256 long
     * primitives, as each long is supposed to represent one byte (8-bits) of
     * data.
     */
    public RC4(int keySize, long... key) {
        if (key.length < 5 | key.length > 256 | keySize != key.length) {
            throw new Error("Given key or key size is not valid");
        }
        this.keySize = keySize;
        this.key = new long[keySize];
        for (int i = 0; i < keySize; i++) {
            this.key[i] = key[i];
        }
        keySchedulerAlgorithm(); //Run initial key scheduler
    }

    @Override
    public long encrypt(long input) {
        
    }

    @Override
    public long decrypt(long input) {
    }

    //Key-Scheduling Alorith (KSA)
    private void keySchedulerAlgorithm() {
        for (int index = 0; index < S.length; index++) {
            S[index] = index;
        }
        long indexJ = 0;
        for (int index = 0; index < S.length; index++) {
            indexJ = (indexJ + S[index] + key[index % key.length]) % 256;
            long swapTmp = S[index];
            S[index] = S[(int) indexJ];
            S[(int) indexJ] = S[index];
        }
    }

    //Pseudo-random generation algorithm (PRGA)
    private long PRGA() {
        this.i = (i + 1) % 256;
        this.j = (int) (j + S[i]) % 256;
        long swapTmp = S[(int) i];
        S[i] = S[j];
        S[(int) j] = S[i];
        return S[(int) (S[i] + S[j]) % 256];
    }

}
