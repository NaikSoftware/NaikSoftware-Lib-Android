package ua.naiksoftware.android.adapter.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import ua.naiksoftware.android.adapter.util.SimpleViewHolder;

public abstract class BaseAdapterDelegate<T> implements AdapterDelegate<T> {

    @NonNull
    @Override
    public abstract SimpleViewHolder onCreateViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull T items, int position, @NonNull RecyclerView.ViewHolder holder) {
        onBindViewHolder(items, position, (SimpleViewHolder) holder);
    }

    public abstract void onBindViewHolder(@NonNull T items, int position, @NonNull SimpleViewHolder holder);
}
