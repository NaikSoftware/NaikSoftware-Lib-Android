package ua.naiksoftware.android.adapter.delegate;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ua.naiksoftware.android.BR;
import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;
import ua.naiksoftware.android.adapter.util.BindingHolder;

public abstract class BaseBindableAdapterDelegate<T, VB extends ViewDataBinding> implements AdapterDelegate<T> {

    private LayoutInflater mLayoutInflater;
    private ActionClickListener mActionHandler;

    public BaseBindableAdapterDelegate() {
    }

    public BaseBindableAdapterDelegate(ActionClickListener actionHandler) {
        mActionHandler = actionHandler;
    }

    @NonNull
    @Override
    public final BindingHolder<VB> onCreateViewHolder(ViewGroup parent) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        return createHolder(mLayoutInflater, parent);
    }

    protected abstract BindingHolder<VB> createHolder(LayoutInflater inflater, ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull T items, int position, @NonNull RecyclerView.ViewHolder holder) {
        BindingHolder<VB> bindingHolder = (BindingHolder<VB>) holder;
        if (mActionHandler != null) {
            bindingHolder.getBinding().setVariable(BR.actionHandler, mActionHandler);
        }
        bindHolder(items, position, bindingHolder);
    }

    public abstract void bindHolder(@NonNull T items, int position, @NonNull BindingHolder<VB> holder);
}
