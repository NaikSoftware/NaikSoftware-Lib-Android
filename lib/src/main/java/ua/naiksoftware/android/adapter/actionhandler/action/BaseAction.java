package ua.naiksoftware.android.adapter.actionhandler.action;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.android.adapter.actionhandler.listener.OnActionFiredListener;

/**
 * Extent from BaseAction all you custom actions.
 * BaseAction contain base logic to notify listeners if action fired.
 *
 * @param <M> base type, which can be handled
 */
public abstract class BaseAction<M> implements Action<M> {

    /**
     * Listeners for action fired events.
     */
    protected List<OnActionFiredListener> mActionFiredListeners = new ArrayList<>(1);

    /**
     * Add a listener that will be called when method {@link #notifyOnActionFired(View, String, Object)}
     * called. Generally if action fired successfully.
     *
     * @param listener The listener that will be called when action fired successfully.
     */
    public void addActionFiredListener(OnActionFiredListener listener) {
        if (listener != null) mActionFiredListeners.add(listener);
    }

    /**
     * Remove a listener for action fired events.
     *
     * @param listener The listener for action fired events.
     */
    public void removeActionFireListener(OnActionFiredListener listener) {
        if (listener != null) mActionFiredListeners.remove(listener);
    }

    /**
     * Remove all listeners for action fired events.
     */
    public void removeAllActionFireListeners() {
        mActionFiredListeners.clear();
    }

    /**
     * Notify any registered listeners that the action has been fired.
     *
     * @param view       The View, which can be used for prepare any visual effect (like animation),
     *                   Generally it is that view which was clicked and initiated action to fire.
     * @param actionType type of the action
     * @param model      model, which was handled
     */
    public void notifyOnActionFired(View view, String actionType, Object model) {
        for (OnActionFiredListener listener : mActionFiredListeners) {
            listener.onActionFired(view, actionType, model);
        }
    }
}