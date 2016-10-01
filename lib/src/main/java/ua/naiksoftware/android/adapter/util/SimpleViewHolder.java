package ua.naiksoftware.android.adapter.util;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;

/**
 * Simple universal ViewHolder
 *
 * Created by naik on 07.03.16.
 */
public class SimpleViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViewSparseArray = new SparseArray<>();

    public SimpleViewHolder(@LayoutRes int itemLayout, ViewGroup parent, LayoutInflater layoutInflater) {
        super(layoutInflater.inflate(itemLayout, parent, false));
    }

    public SimpleViewHolder useView(@IdRes int viewId) {
        View v = itemView.findViewById(viewId);
        if (v == null) {
            throw new IllegalArgumentException("View with id=" + viewId
											   + " not found in this ViewHolder root view");
        }
        mViewSparseArray.put(viewId, v);
        return this;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        T view = (T) mViewSparseArray.get(viewId);
        if (view == null) {
            throw new IllegalArgumentException("View with id=" + viewId
											   + " not found, try call useView(viewId) before");
        }
        return view;
    }
}
