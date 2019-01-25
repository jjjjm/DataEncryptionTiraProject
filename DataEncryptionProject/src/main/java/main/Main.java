package main;

import check.ParityCheck;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        byte test = 4;
        test = (byte) (test >> 1);
        System.out.println(test);
    }

}
