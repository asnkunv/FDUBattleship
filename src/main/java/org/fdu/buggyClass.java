package org.fdu;

import java.io.*;
import java.util.*;
import java.sql.*;

/**
 * This class intentionally contains violations for PMD, Checkstyle, and SpotBugs.
 * DO NOT use this code in production.
 */
public class BuggyClass {

    // ============================================================
    // CHECKSTYLE VIOLATIONS
    // ============================================================

    // [Checkstyle] MagicNumber: raw literals used directly
    // [Checkstyle] VisibilityModifier: fields should be private
    // [Checkstyle] NeedBraces: missing braces on control statements
    // [Checkstyle] WhitespaceAround / LineLength violations below
    public static int MAX=9999;
    public String Name;
    public ArrayList list = new ArrayList();

    // [Checkstyle] MethodName: must start with lowercase
    // [Checkstyle] ParameterNumber: too many parameters
    public void DoSomething(int a,int b,int c,int d,int e,int f,int g,int h) {
        if(a>0) System.out.println("positive"); // missing braces, no space after 'if'
    }

    // [Checkstyle] EmptyBlock
    public void emptyMethod() {}

    // [Checkstyle] LocalVariableName: names must match pattern
    public int calculate() {
        int X = 10;      // uppercase local variable
        int __val = 20;  // illegal name pattern
        return X + __val;
    }

    // ============================================================
    // PMD VIOLATIONS
    // ============================================================

    // [PMD] UnusedLocalVariable
    // [PMD] EmptyCatchBlock
    // [PMD] AvoidCatchingGenericException
    // [PMD] SystemPrintln
    public void pmdViolations() {
        int unused = 42; // never read

        try {
            int result = 10 / 0;
        } catch (Exception e) {
            // swallowed exception, empty catch block
        }

        System.out.println("PMD hates this"); // use a logger!
    }

    // [PMD] CyclomaticComplexity: too many branches
    public String highComplexity(int x) {
        if (x == 1) return "one";
        else if (x == 2) return "two";
        else if (x == 3) return "three";
        else if (x == 4) return "four";
        else if (x == 5) return "five";
        else if (x == 6) return "six";
        else if (x == 7) return "seven";
        else if (x == 8) return "eight";
        else if (x == 9) return "nine";
        else if (x == 10) return "ten";
        else if (x == 11) return "eleven";
        else return "other";
    }

    // [PMD] UseStringBufferForStringAppends: string concat in a loop
    public String buildString(List<String> items) {
        String result = "";
        for (String item : items) {
            result += item; // inefficient, PMD flags this
        }
        return result;
    }

    // [PMD] AvoidInstantiatingObjectsJustToGetClass
    public void unnecessaryInstantiation() {
        Class<?> c = new BuggyClass().getClass(); // should use BuggyClass.class
    }

    // [PMD] ReturnEmptyCollectionRatherThanNull
    public List<String> getItems() {
        return null; // callers must null-check, bad practice
    }

    // ============================================================
    // SPOTBUGS VIOLATIONS
    // ============================================================

    // [SpotBugs] NP_NULL_ON_SOME_PATH: possible NullPointerException
    public int nullDereference(String s) {
        return s.length(); // s could be null, no null check
    }

    // [SpotBugs] OBL_UNSATISFIED_OBLIGATION / RR_NOT_CHECKED:
    // resource opened but never closed (no try-with-resources)
    public void resourceLeak() throws IOException {
        FileInputStream fis = new FileInputStream("data.txt");
        int data = fis.read(); // fis is never closed
        System.out.println(data);
    }

    // [SpotBugs] DM_DEFAULT_ENCODING: uses platform default charset
    public byte[] defaultEncoding(String s) {
        return s.getBytes(); // should specify charset explicitly
    }

    // [SpotBugs] ES_COMPARING_STRINGS_WITH_EQ: == used for String comparison
    public boolean compareStrings(String a, String b) {
        return a == b; // should use a.equals(b)
    }

    // [SpotBugs] PREDICTABLE_RANDOM: Random used for security-sensitive context
    private final Random random = new Random(); // not cryptographically secure

    public int generateToken() {
        return random.nextInt(100000);
    }

    // [SpotBugs] SQL_NONCONSTANT_STRING_TO_EXECUTE:
    // SQL injection vulnerability via string concatenation
    public void sqlInjection(Connection conn, String userInput) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT * FROM users WHERE name = '" + userInput + "'");
    }

    // [SpotBugs] SE_FIELD_NOT_SERIALIZABLE: non-serializable field in Serializable class
    static class BadSerializable implements Serializable {
        private Thread thread = new Thread(); // Thread is not serializable
    }

    // [SpotBugs] RV_RETURN_VALUE_IGNORED: return value of important call ignored
    public void ignoreReturnValue(File f) {
        f.delete();     // result ignored, deletion might have failed
        f.mkdirs();     // same issue
    }

    // ============================================================
    // BONUS: violations caught by all three tools
    // ============================================================

    // Empty main just to make it runnable
    public static void main(String[] args) {
        BuggyClass b = new BuggyClass();
        b.pmdViolations();
        b.highComplexity(5);
    }
}