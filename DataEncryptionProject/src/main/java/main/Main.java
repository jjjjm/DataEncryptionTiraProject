package main;

import java.util.Scanner;
import util.BinaryMatrix64;

public class Main {

    public static void main(String[] args) {
        int x = 0;
        for (int i = 0; i < 64; i++) {
            System.out.print(" X_" + i);
            x++;
            if (x == 6) {
                System.out.print("|||");
                x = 0;
            }
        }
    }

}
