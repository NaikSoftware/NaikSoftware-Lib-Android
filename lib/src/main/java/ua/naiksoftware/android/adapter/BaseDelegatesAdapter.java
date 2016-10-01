package ua.naiksoftware.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import ua.naiksoftware.android.adapter.delegate.AdapterDelegate;
import ua.naiksoftware.android.adapter.util.AdapterDelegatesManager;

/**
 * RecyclerView Adapter for using with any data types
 */
public abstract class BaseDelegatesAdapter<T> extends RecyclerView.Adapter {


    private T mDataSet;
    private AdapterDelegatesManager<T> mDelegatesManager;

    public BaseDelegatesAdapter() {
    }

    public BaseDelegatesAdapter(List<AdapterDelegate> delegates) {
        mDelegatesManager = new AdapterDelegatesManager<>();
        mDelegatesManager.addDelegates(delegates);
    }

    public BaseDelegatesAdapter(AdapterDelegate... delegates) {
       this(Arrays.asList(delegates));
    }

    public BaseDelegatesAdapter(T dataSet, List<AdapterDelegate> delegates) {
        this(delegates);
        this.mDataSet = dataSet;
    }

    public BaseDelegatesAdapter(T dataSet, AdapterDelegate... delegates) {
        this(dataSet, Arrays.asList(delegates));
    }
    
    public void setDataSet(T dataSet) {
        mDataSet = dataSet;
    }

    public T getDataSet() {
        return mDataSet;
    }

    @Override
    public long getItemId(int position) {
        return mDelegatesManager.getItemId(mDataSet, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mDelegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mDelegatesManager.onBindViewHolder(mDataSet, position, holder);
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegatesManager.getItemViewType(mDataSet, position);
    }
}
