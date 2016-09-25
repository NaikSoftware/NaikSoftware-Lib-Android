package ua.naiksoftware.android.adapter.actionhandler.action;

/**
 * Defines an interface for actions that can (or need to) be cancelled. For example, if they
 * are not used any longer or if ActionHandler will be recreated and all old actions has to be cancelled.
 * For actions collected by {@link ActionHandler} this method can be called by {@link ActionHandler#cancelAll()}
 */
public interface Cancelable {

    /**
     * Cancel any action once this method is called.
     * For actions collected by {@link ActionHandler} this method can be called by {@link ActionHandler#cancelAll()}
     */
    void cancel();
}
