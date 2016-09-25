package ua.naiksoftware.android.util;

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

import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;
import ua.naiksoftware.android.adapter.util.ListConfig;

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

    /**
     * Binding adapter to assign an action to a view using android data binding approach.
     * Sample:
     * <pre>
     * &lt;Button
     *     android:layout_width="wrap_content"
     *     android:layout_height="wrap_content"
     *
     *     android:actionHandler="@{someActionHandler}"
     *     android:actionType='@{"send_message"}'
     *     android:actionTypeLongClick='@{"show_menu"}'
     *     android:model="@{user}"
     *
     *     android:text="@string/my_button_text"/&gt;
     * </pre>
     *
     * @param view                The View to bind an action
     * @param actionHandler       The action handler which will handle an action
     * @param actionType          The action type, which will be handled on view clicked
     * @param actionTypeLongClick The action type, which will be handled on view long clicked
     * @param model               The model which will be handled
     */
    @BindingAdapter(
            value = {"actionHandler", "actionType", "actionTypeLongClick", "model", "modelLongClick"},
            requireAll = false
    )
    public static void setActionHandler(final View view, final ActionClickListener actionHandler, final String actionType, final String actionTypeLongClick, final Object model, final Object modelLongClick) {
        if (actionHandler != null) {
            if (actionType != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        actionHandler.onActionClick(view, actionType, model);
                    }
                });
            }

            if (actionTypeLongClick != null) {
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        actionHandler.onActionClick(view, actionTypeLongClick, modelLongClick != null ? modelLongClick : model);
                        return true;
                    }
                });
            }
        }
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
