package ua.naiksoftware.android.adapter.util;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Recycler ViewHolder to use with data binding
 */
public class BindingHolder<VB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private final VB binding;

    public static <VB extends ViewDataBinding> BindingHolder<VB> newInstance(@LayoutRes int layoutId, LayoutInflater inflater, ViewGroup parent) {
        VB vb = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        return new BindingHolder<>(vb);
    }

    public BindingHolder(VB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * Do not use in lists, may cause delay binding
     * @param view view to bind
     */
    public BindingHolder(View view) {
        super(view);
        binding = DataBindingUtil.bind(view);
    }

    public VB getBinding() {
        return binding;
    }

}
