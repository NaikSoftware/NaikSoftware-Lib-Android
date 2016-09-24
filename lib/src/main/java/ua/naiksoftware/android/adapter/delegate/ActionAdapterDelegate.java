package ua.naiksoftware.android.adapter.delegate;

import android.databinding.ViewDataBinding;

import com.drextended.actionhandler.listener.ActionClickListener;

/**
 * Created by roman.donchenko on 18.01.2016
 */
public abstract class ActionAdapterDelegate<T, VB extends ViewDataBinding> extends BaseBindableAdapterDelegate<T, VB> {

    protected ActionClickListener mActionHandler;

    public ActionAdapterDelegate(final ActionClickListener actionHandler) {
        mActionHandler = actionHandler;
    }

    public ActionClickListener getActionHandler() {
        return mActionHandler;
    }
}
