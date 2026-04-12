package org.fdu;

public class buggyClass {
    public static String status = "active"; // PMD: public static field (DataClass)

    public void riskyMethod() {
        try {
            int[] arr = new int[5];
            arr[10] = 1;
        } catch (Exception e) { // SpotBugs: caught exception ignored
        }
    }
    public void anotherMethod(){} // Checkstyle: no space before '{'
}