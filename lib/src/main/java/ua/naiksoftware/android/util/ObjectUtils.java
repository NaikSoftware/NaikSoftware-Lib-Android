package ua.naiksoftware.android.util;

public class ObjectUtils {

    public static class Classpath {

        public static boolean RETROFIT_RX_ADAPTER;
        public static boolean APP_COMPAT;

        static {
            RETROFIT_RX_ADAPTER = ObjectUtils.classExists("retrofit2.adapter.rxjava.Result");
            APP_COMPAT = ObjectUtils.classExists("android.support.v7.app.AppCompatActivity");
        }
    }

    /**
     * Null-safe equals
     */
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static boolean classExists(String className) {
        try {
            Class.forName(className, false, ObjectUtils.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
