package ua.naiksoftware.android.adapter.actionhandler.listener;

import android.view.View;

/**
 * Interface definition for a callback to be invoked when an action is executed successfully.
 */
public interface OnActionFiredListener {

    /**
     * Called after an action is executed successfully.
     *
     * @param view       The View, which can be used for prepare any visual effect (like animation),
     * @param actionType The action type, which appointed to the view
     * @param model      The model, which  appointed to the view and should be handled
     */
    void onActionFired(View view, String actionType, Object model);
}
