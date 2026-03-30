package org.fdu;

public class SpotBugsViolations {

    // NP_NULL_ON_SOME_PATH - possible null pointer dereference
    public int nullPointer() {
        String s = null;
        return s.length();
    }

    // DM_NUMBER_CTOR - use valueOf instead of new Integer()
    public void unnecessaryConstructor() {
        Integer i = new Integer(42);
    }

    // EQ_COMPARETO_USE_OBJECT_EQUALS - compareTo without equals
    public int compareWithoutEquals(Object o) {
        return this.toString().compareTo(o.toString());
    }

    // RV_RETURN_VALUE_IGNORED - ignoring return value of replace
    public void ignoredReturnValue() {
        String s = "hello";
        s.replace("h", "j"); // result is ignored
    }

    // DM_EXIT - calling System.exit in non-main method
    public void callsExit() {
        System.exit(0);
    }

    // ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD - writing to static field from instance method
    private static int counter = 0;
    public void writeToStatic() {
        counter = 42;
    }
}