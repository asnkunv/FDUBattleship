package org.fdu;

import java.util.*;

public class badCode {
    
    public static int[] data = new int[10];
    private String unusedField = "hello";
    
    public void doStuff(int x, int y, int z, int a, int b, int c) {
        int result = 0;
        if (x > 0) {
            if (y > 0) {
                if (z > 0) {
                    if (a > 0) {
                        if (b > 0) {
                            if (c > 0) {
                                result = x + y + z + a + b + c;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(result);
    }

    public String nullRisk() {
        String s = null;
        return s.toUpperCase(); // SpotBugs: null dereference
    }

    public void resourceLeak() throws Exception {
        java.io.FileInputStream fis = new java.io.FileInputStream("file.txt"); // SpotBugs: resource leak
        fis.read();
    }

    public boolean compareStrings(String s) {
        return s == "hello"; // PMD + SpotBugs: string comparison with ==
    }

    public void emptyCatch() {
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            // PMD: empty catch block
        }
    }
}