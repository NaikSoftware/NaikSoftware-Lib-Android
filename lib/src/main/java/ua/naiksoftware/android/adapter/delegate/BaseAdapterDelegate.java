package ua.naiksoftware.android.adapter.delegate;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;
import ua.naiksoftware.android.adapter.util.SimpleViewHolder;
import ua.naiksoftware.android.util.ObjectUtils;

/**
 * AdapterDelegate without using data binding
 */
public abstract class BaseAdapterDelegate<T> implements AdapterDelegate<List<T>> {

    private ItemViewsFactoryAbs mItemViewsFactory;
    private LayoutInflater mLayoutInflater;

    public BaseAdapterDelegate() {
    }

    /**
     * Use this constructor if you are use ActionHandler and Android support library.
     * <b>WARNING:</b> pass Activity as context for inflate support library widgets
     */
    public BaseAdapterDelegate(ActionClickListener actionHandler, Context context) {
        if (actionHandler == null || context == null) return;
        mLayoutInflater = LayoutInflater.from(context).cloneInContext(context);

        if (ObjectUtils.Classpath.APP_COMPAT) {
            if (context instanceof Activity) {
                ItemViewsFactoryCompat itemViewsFactory = new ItemViewsFactoryCompat((Activity) context, actionHandler);
                mItemViewsFactory = itemViewsFactory;
                LayoutInflaterCompat.setFactory(mLayoutInflater, itemViewsFactory);
            } else {
                throw new IllegalArgumentException("Pass Activity as context for inflate " +
                        "support-library widgets or remove support library from dependencies!");
            }
        } else {
            ItemViewsFactory itemViewsFactory = new ItemViewsFactory(actionHandler);
            mItemViewsFactory = itemViewsFactory;
            mLayoutInflater.setFactory(itemViewsFactory);
        }
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        return createHolder(mLayoutInflater, parent);
    }

    @NonNull
    public abstract SimpleViewHolder createHolder(LayoutInflater inflater, ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull List<T> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        if (mItemViewsFactory != null) {
            mItemViewsFactory.setActionModel(holder.itemView, items.get(position));
        }
        bindHolder(items, position, (SimpleViewHolder) holder);
    }

    public abstract void bindHolder(@NonNull List<T> items, int position, @NonNull SimpleViewHolder holder);
}
