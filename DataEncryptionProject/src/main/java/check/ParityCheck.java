package check;


public class ParityCheck {

    final byte parityMaskLeft = (byte) 0b10000000;
    final byte parityMaskRight = (byte) 0b00000001;

    /**
     * Validates the parity in the given key for DES algorithm
     *
     * @param key The key used is DES algorithm
     * @return Returns true if the given String is parity valid
     */
    public boolean CheckDES(String key) {
        byte[] keyBytes = key.getBytes();
        for (int i = 0; i < keyBytes.length; i++) {
            byte parityByte = findParityByte(keyBytes[i], true);
            if (!checkParity(true, parityByte)) return false;
        }
        return true;
    }

    /*
    Finds the parity bit by masking of the other bits in the byte.
    That is 
     */
    private byte findParityByte(byte b, boolean left) {
        if (left) {
            return (byte) (b & parityMaskLeft);
        } else {
            return (byte) (b & parityMaskRight);
        }
    }

    /* If parity parameter is set to true the parity bit should represent parity otherwise it should represent non-parity*/
    private boolean checkParity(boolean parity, byte parityByte) {
        System.out.println(parityByte);
        if (parity) {
            if (parityByte > 0) {
                return true;
            }
        } else {
            if (!(parityByte > 0)) {
                return true;
            }
        }
        return false;
    }

}
