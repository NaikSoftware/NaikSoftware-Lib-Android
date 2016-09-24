package ua.naiksoftware.android.util;

public class ComparingUtils {

    /**
     * Null-safe equals
     */
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
