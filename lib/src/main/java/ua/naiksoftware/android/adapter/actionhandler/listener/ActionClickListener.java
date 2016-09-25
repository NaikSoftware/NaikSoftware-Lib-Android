package ua.naiksoftware.android.adapter.actionhandler.listener;

import android.view.View;

/**
 * Interface definition for a callback to be invoked when a view with an action is clicked.
 */
public interface ActionClickListener {
    /**
     * Called when a view with an action is clicked.
     *
     * @param view          The view that was clicked.
     * @param actionType    The action type, which appointed to the view
     * @param model         The model, which  appointed to the view and should be handled
     */
    void onActionClick(final View view, final String actionType, final Object model);
}
