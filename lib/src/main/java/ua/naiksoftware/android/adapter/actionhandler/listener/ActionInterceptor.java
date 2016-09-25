package ua.naiksoftware.android.adapter.actionhandler.listener;

import android.content.Context;
import android.view.View;

/**
 * Interface definition for a callback to be invoked after a view with an action is clicked
 * and before action handling started. If {@link #onInterceptAction(Context, View, String, Object)} return true
 * then this action will not be handled.
 */
public interface ActionInterceptor {
    /**
     * Called after a view with an action is clicked
     * and before action handling started. If return true then this action will not be handled.
     *
     *
     * @param context
     * @param view       The view that was clicked.
     * @param actionType The action type, which appointed to the view
     * @param model      The model, which  appointed to the view and should be handled
     * @return true for intercept the action, false to handle the action in normal way.
     */
    boolean onInterceptAction(Context context, final View view, final String actionType, final Object model);
}
