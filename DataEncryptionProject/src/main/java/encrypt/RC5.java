package encrypt;

/**
 * Class used in RC5 encryption.
 */
public class RC5 implements Encryption {

    // Magic numbers for 64-bit blocks, that is the word size.
    final private int P = 0xB7E15163;
    final private int Q = 0x9E3779B9;

    private int roundAmount;
    private int[] keyArray;

    /**
     * Creates a new RC5 which can be use to encrypt / decrypt given data with
     * the RC5 algorithm
     *
     * @param keyLength length of the key in bytes
     * @param roundAmount Amount of round to be used in the program (feistel
     * rounds)
     * @param key the key used in the algoritm, given as a byte array
     */
    public RC5(int keyLength, int roundAmount, byte... key) {
        if (keyLength != key.length) {
            throw new Error("Given length for key doesn't match the actual length");
        }
        this.roundAmount = roundAmount;
        this.keyArray = this.finalizeRoundKeys(this.initializeKeyArray(keyLength, key), this.initializeRoundKeyArray(roundAmount));
    }

    @Override
    public long encrypt(long input) {
        long halfInputMask = 0xFFFFFFFFl;
        int A = (int) ((input >> 32) & halfInputMask);
        int B = (int) (input & halfInputMask);
//        System.out.println(A + "/" + B);
        A = (A + keyArray[0]);
        B = (B + keyArray[1]);
        for (int i = 1; i <= roundAmount; i++) {
            A = (((A ^ B) << B) + keyArray[2 * i]);
            B = (((B ^ A) << A) + keyArray[2 * i + 1]);
        }
//        System.out.println(A + "/" + B);
//        System.out.println(Integer.toHexString(A) + "/" + Integer.toHexString(B));
        long newLong;
        newLong = ((halfInputMask & A) << 32);
        return newLong | (halfInputMask & B);
    }

    @Override
    public long decrypt(long input) {
        long halfInputMask = 0xFFFFFFFFl;
        int A = (int) ((input >> 32) & halfInputMask);
        int B = (int) (input & halfInputMask);
        for (int i = roundAmount; i > 0; i--) {
            B = ((B - keyArray[2 * i + 1]) >> A) ^ A;
            A = ((A - keyArray[2 * i]) >> B) ^ B;
        }
        B = (B - keyArray[1]);
        A = (A - keyArray[0]);
        long newLong;
        newLong = ((halfInputMask & A) << 32);
        return newLong | (halfInputMask & B);
    }

    //This initializes the key array according to the given algorithm and the given word length (32 bits chosen here)
    private int[] initializeKeyArray(int keyLength, byte[] keyBytes) {
        int wordLength = 32 / 8;
        int arrayLength = keyLength / wordLength == 0 ? 1 : keyLength / wordLength;
        int[] L = new int[arrayLength];
        for (int index = keyLength - 1; index >= 0; index--) {
            L[index / wordLength] = (L[index / wordLength] << 8) + keyBytes[index];
        }
        return L;
    }

    // Initializes the final key array
    private int[] initializeRoundKeyArray(int roundAmount) {
        int subKeyAmount = 2 * (roundAmount + 1);
        int[] S = new int[subKeyAmount];
        S[0] = P;
        for (int index = 1; index < subKeyAmount; index++) {
            S[index] = S[index - 1] + Q;
        }
        return S;
    }

    //Aka. Subkey mixer
    private int[] finalizeRoundKeys(int[] keyArray, int[] roundKeyArray) {
        int i = 0, j = 0;
        int A = 0, B = 0;
        int[] roundKeys = roundKeyArray;
        int[] tmpKeys = keyArray;
        long timerLimit = 3 * (keyArray.length < roundKeyArray.length ? roundKeyArray.length : keyArray.length);
        for (int timer = 0; timer < timerLimit; timer++) {
            roundKeys[i] = (roundKeys[i] + A + B) << 3;
            A = roundKeys[i];
            tmpKeys[j] = (tmpKeys[j] + A + B) << 3;
            B = tmpKeys[j];
            i = (i + 1) % roundKeyArray.length;
            j = (j + 1) % tmpKeys.length;
        }
        return roundKeys;
    }

//    private int ROTL(int x, int y) {
//        return (((x) << (y & (32 - 1))) | ((x) >> (32 - (y & (32 - 1)))));
//    }
//
//    private int ROLR(int x, int y) {
//        return (((x) >> (y & (32 - 1))) | ((x) << (32 - (y & (32 - 1)))));
//    }
}
