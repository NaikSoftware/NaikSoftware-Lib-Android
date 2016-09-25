package ua.naiksoftware.android.adapter.actionhandler.action;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import ua.naiksoftware.android.util.ProgressBarController;

/**
 * Base action for implementing call a network request
 *
 * @param <RM> The type of network response
 * @param <M>  The type of model which can be handled
 */
public abstract class RequestAction<RM, M> extends DialogAction<M> {
    protected boolean mShowProgressEnabled;
    protected boolean mShowDialogEnabled;
    protected String mProgressTag;

    public RequestAction() {
    }

    /**
     * Base action for implementing call a network request
     *
     * @param showProgressEnabled Set true to show progress dialog while request
     * @param showDialogEnabled          Set true to show dialog before action fired
     */
    public RequestAction(boolean showProgressEnabled, boolean showDialogEnabled) {
        mShowProgressEnabled = showProgressEnabled;
        mShowDialogEnabled = showDialogEnabled;
        if (mShowProgressEnabled) mProgressTag = getClass().getSimpleName();
    }

    /**
     * Set progress dialog enabled
     *
     * @param showProgressEnabled Set true to show progress dialog while request
     */
    public void setShowProgressEnabled(boolean showProgressEnabled) {
        mShowProgressEnabled = showProgressEnabled;
        if (mProgressTag == null && mShowProgressEnabled) mProgressTag = getClass().getSimpleName();
    }

    /**
     * Set dialog before action fired enabled
     *
     * @param showDialogEnabled Set true to show dialog before action fired
     */
    public void setShowDialogEnabled(boolean showDialogEnabled) {
        mShowDialogEnabled = showDialogEnabled;
    }

    @Override
    public void onFireAction(Context context, @Nullable View view, String actionType, @Nullable M model) {
        if (mShowDialogEnabled) {
            super.onFireAction(context, view, actionType, model);
        } else {
            makeRequest(context, view, actionType, model);
        }
    }

    @Override
    protected void onDialogActionFire(Context context, View view, String actionType, M model) {
        makeRequest(context, view, actionType, model);
    }

    /**
     * Prepare and call a network request
     *
     * @param context    The Context, which generally get from view by {@link View#getContext()}
     * @param view       The view, which can be used for prepare any visual effect (like animation),
     *                   Generally it is that view which was clicked and initiated action to fire
     * @param actionType Type of the action which was executed. Can be null.
     * @param model      The model which should be handled by the action. Can be null.
     */
    public void makeRequest(Context context, View view, String actionType, final M model) {
        onRequestStarted(context, view, actionType, model);
        onMakeRequest(context, view, actionType, model);
    }

    /**
     * Called on request started. Shows progress dialog if enabled.
     *
     * @param context    The Context, which generally get from view by {@link View#getContext()}
     * @param view       The view, which can be used for prepare any visual effect (like animation),
     *                   Generally it is that view which was clicked and initiated action to fire
     * @param actionType Type of the action which was executed. Can be null.
     * @param model      The model which should be handled by the action. Can be null.
     */
    protected void onRequestStarted(Context context, View view, String actionType, M model) {
        if (mShowProgressEnabled) showProgressDialog(context, view, actionType, model);
    }

    /**
     * Provides message for progress dialog
     *
     * @param context    The Context, which generally get from view by {@link View#getContext()}
     * @param view       The view, which can be used for prepare any visual effect (like animation),
     *                   Generally it is that view which was clicked and initiated action to fire
     * @param actionType Type of the action which was executed. Can be null.
     * @param model      The model which should be handled by the action. Can be null.
     * @return message for progress dialog
     */
    protected String getProgressDialogMessage(Context context, View view, String actionType, M model) {
        return null;
    }

    /**
     * Called on request has been fired successfully.
     * Hides progress dialog if enabled and call {@link #notifyOnActionFired}
     * Should be called manually in request callback.
     *
     * @param context    The Context, which generally get from view by {@link View#getContext()}
     * @param view       The view, which can be used for prepare any visual effect (like animation),
     *                   Generally it is that view which was clicked and initiated action to fire
     * @param actionType Type of the action which was executed.
     * @param oldModel   The model which was used in request.
     * @param response   network response
     */
    protected void onResponseSuccess(Context context, View view, String actionType, M oldModel, RM response) {
        if (mShowProgressEnabled) hideProgressDialog();
        notifyOnActionFired(view, actionType, oldModel);
    }

    /**
     * Call for show progress dialog
     *
     * @param context    The Context, which generally get from view by {@link View#getContext()}
     * @param view       The view, which can be used for prepare any visual effect (like animation),
     *                   Generally it is that view which was clicked and initiated action to fire
     * @param actionType Type of the action which was executed. Can be null.
     * @param model      The model which should be handled by the action. Can be null.
     */
    public void showProgressDialog(Context context, View view, String actionType, M model) {
        ProgressBarController.showProgressDialog(context, mProgressTag, getProgressDialogMessage(context, view, actionType, model));
    }

    /**
     * Call for hide progress dialog
     */
    public void hideProgressDialog() {
        ProgressBarController.hideProgressDialog(mProgressTag);
    }

    /**
     * Called if request returns error.
     * Hides progress dialog if enabled
     * Should be called manually in request callback.
     *
     * @param context    The Context, which generally get from view by {@link View#getContext()}
     * @param view       The view, which can be used for prepare any visual effect (like animation),
     *                   Generally it is that view which was clicked and initiated action to fire
     * @param actionType Type of the action which was executed.
     * @param oldModel   The model which was used in request.
     * @param e          The Error
     */
    protected void onResponseError(Context context, View view, String actionType, M oldModel, Throwable e) {
        if (mShowProgressEnabled) hideProgressDialog();
    }

    /**
     * Implement network request there.
     * Note: You should call {@link #onResponseSuccess(Context, View, String, Object, Object)} if request finished successfully
     * and {@link #onResponseError(Context, View, String, Object, Throwable)} if it is failed.
     *
     * @param context    The Context, which generally get from view by {@link View#getContext()}
     * @param view       The view, which can be used for prepare any visual effect (like animation),
     *                   Generally it is that view which was clicked and initiated action to fire
     * @param actionType Type of the action which was executed.
     * @param model      The model which was used in request.
     */
    protected abstract void onMakeRequest(Context context, View view, String actionType, final M model);

}
