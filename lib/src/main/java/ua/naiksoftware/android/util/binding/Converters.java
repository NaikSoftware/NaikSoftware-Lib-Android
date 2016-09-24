package ua.naiksoftware.android.util.binding;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by naik on 3/14/16.
 */
public class Converters {

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }

    @BindingConversion
    public static int convertBooleanToInt(Boolean value) {
        return (value != null && value) ? View.VISIBLE : View.GONE;
    }

    @BindingAdapter("adapter")
    public static <T extends ListAdapter & Filterable> void setAutoCompleteAdapter(final AutoCompleteTextView view, final T adapter) {
        view.setAdapter(adapter);
    }

    @BindingAdapter({"listConfig"})
    public static void configRecyclerView(RecyclerView recyclerView, ListConfig config) {
        config.applyConfig(recyclerView);
    }

    @BindingAdapter(value = {"delayMillis", "longTouchListener"}, requireAll = false)
    public static void setMultipleClicksListener(final View view, Integer delayMillis, final View.OnClickListener listener) {
        if (listener == null) return;

        if (delayMillis == null) delayMillis = 5000;

        final AtomicLong delay = new AtomicLong(delayMillis);
        final AtomicLong start = new AtomicLong(Long.MAX_VALUE);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        start.set(System.currentTimeMillis());
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - start.get() >= delay.get()) {
                            listener.onClick(view);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    default:
                        start.set(Long.MAX_VALUE);
                }
                return false;
            }
        });
    }
}
