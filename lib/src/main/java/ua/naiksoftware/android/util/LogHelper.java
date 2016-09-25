package ua.naiksoftware.android.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Helper class for logging
 */
public class LogHelper {

    public static final String DEFAULT_TAG = "default_tag";

    private static boolean LOGGING_ENABLED = true;

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return cls.getSimpleName();
    }

    public static void LOGD(String message) {
        LOGD(DEFAULT_TAG, message);
    }

    public static void LOGD(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.d(tag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.d(tag, message, cause);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.v(tag, message);
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.v(tag, message, cause);
        }
    }

    public static void LOGI(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.i(tag, message);
        }
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.i(tag, message, cause);
        }
    }

    public static void LOGW(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.w(tag, message);
        }
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.w(tag, message, cause);
        }
    }

    public static void LOGE(final String tag, String message) {
        if (LOGGING_ENABLED){
            Log.e(tag, message);
        }
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.e(tag, message, cause);
        }
    }

    public static void notifyMessage(Context context, String message) {
        if(LOGGING_ENABLED) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            LOGI(DEFAULT_TAG, message);
        }
    }

    private LogHelper() {
    }

    public static void enableLog(boolean enabled) {
        LOGGING_ENABLED = enabled;
    }
}