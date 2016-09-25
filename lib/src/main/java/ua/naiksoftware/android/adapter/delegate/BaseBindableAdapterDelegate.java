package ua.naiksoftware.android.adapter.delegate;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import ua.naiksoftware.android.adapter.util.BindingHolder;

public abstract class BaseBindableAdapterDelegate<T, VB extends ViewDataBinding> implements AdapterDelegate<T> {

    @NonNull
    @Override
    public abstract BindingHolder<VB> onCreateViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull T items, int position, @NonNull RecyclerView.ViewHolder holder) {
        onBindViewHolder(items, position, (BindingHolder<VB>) holder);
    }

    public abstract void onBindViewHolder(@NonNull T items, int position, @NonNull BindingHolder<VB> holder);
}
