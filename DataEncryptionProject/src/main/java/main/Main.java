package main;

import java.util.Scanner;
import util.BinaryMatrix64;

public class Main {

    public static void main(String[] args) {
        BinaryMatrix64 bm = new BinaryMatrix64(0b1);
        bm.swapColumn(0, 1);
        System.out.println(Long.toBinaryString(bm.getAsLongPrimitive()));
    }

}
