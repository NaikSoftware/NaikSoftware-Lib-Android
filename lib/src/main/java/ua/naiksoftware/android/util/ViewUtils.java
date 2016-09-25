package ua.naiksoftware.android.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import ua.naiksoftware.android.R;

/**
 * Created by naik on 07.07.16.
 */
public class ViewUtils {

    public static void setupWindowTranslucentStatus(Activity activity) {
        if (activity.getResources().getIdentifier("windowTranslucentStatus", "attr", "android") != 0) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void clearWindowTranslucentStatus(Activity activity) {
        if (activity.getResources().getIdentifier("windowTranslucentStatus", "attr", "android") != 0) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static int resolveColorAttr(Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }

    public static boolean isLightTheme(final @NonNull Context context) {
        final TypedValue value = new TypedValue();
        final Resources.Theme theme = context.getTheme();
        return theme.resolveAttribute(R.attr.isLightTheme, value, true)  && value.data != 0;
    }

    /**
     * Set StatusBar color or translucent
     *
     * @param activity target activity
     * @param color    color or null for translucent
     */
    public static void setStatusBarColor(Activity activity, Integer color) {
        if (activity == null || activity.isFinishing()) return;
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            if (color == null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else { //even if color == Color.TRANSPARENT
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(color);
            }
        }
    }

    public static int getStatusBarColor(Activity activity) {
        if (activity == null || activity.isFinishing()) return -1;
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            return window.getStatusBarColor();
        }
        return -1;
    }

    public static void setStatusBarColor(Activity activity, View root, Integer color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activity != null) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                activity.getWindow().setStatusBarColor(color);

                // adjust insets to fix status bar transparency issue
                if (root != null) {
                    root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                        @Override
                        public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                            return insets.consumeSystemWindowInsets();
                        }
                    });
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setLightStatusBar(@NonNull View view) {
        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
    }

    /**
     * Call drawable.mutate() before set to view for prevent unpredictable behaviour
     * @param drawable dravable to tint
     * @param color color in integer format
     */
    public static Drawable setTint(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        return drawable.mutate();
    }

    /**
     * @return item offset (Y-axis) or -1 if ViewHolder in position not instantiated yet
     */
    public static int getItemVerticalOffset(RecyclerView recyclerView, int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        return viewHolder == null ? -1 : (int) viewHolder.itemView.getY();
    }

    public static int findFirstVisiblePosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager)
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

        else if (layoutManager instanceof GridLayoutManager)
            return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();

        throw new IllegalArgumentException("RecyclerView use unsupported LayoutManager: " + layoutManager.getClass().getName());
    }

    public static int findFirstCompletelyVisiblePosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager)
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();

        else if (layoutManager instanceof GridLayoutManager)
            return ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();

        throw new IllegalArgumentException("RecyclerView use unsupported LayoutManager: " + layoutManager.getClass().getName());
    }

    public static void snackBar(int stringResId, View parent) {
        Snackbar.make(parent, stringResId, Snackbar.LENGTH_LONG).show();
    }

    public static void snackBar(String message, View parent) {
        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
    }
}
