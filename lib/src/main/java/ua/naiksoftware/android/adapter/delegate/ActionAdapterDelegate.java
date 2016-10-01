package ua.naiksoftware.android.adapter.delegate;

import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import ua.naiksoftware.android.BR;
import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;
import ua.naiksoftware.android.adapter.util.BindingHolder;

public abstract class ActionAdapterDelegate<T, VB extends ViewDataBinding> extends BaseBindableAdapterDelegate<T, VB> {

    protected ActionClickListener mActionHandler;

    public ActionAdapterDelegate(final ActionClickListener actionHandler) {
        mActionHandler = actionHandler;
    }

    public ActionClickListener getActionHandler() {
        return mActionHandler;
    }

    /**
     * Call super for inject actionHandler into layout as binding variable <code>actionHandler</code>
     */
    @CallSuper
    @Override
    public void bindHolder(@NonNull T items, int position, @NonNull BindingHolder<VB> holder) {
        holder.getBinding().setVariable(BR.actionHandler, mActionHandler);
    }
}
