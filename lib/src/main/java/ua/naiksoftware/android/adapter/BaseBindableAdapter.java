package ua.naiksoftware.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ua.naiksoftware.android.adapter.delegate.AdapterDelegate;
import ua.naiksoftware.android.adapter.util.AdapterDelegatesManager;

/**
 * RecyclerView Adapter for using with data binding
 * Created by roman.donchenko on 05.04.2016
 */
public abstract class BaseBindableAdapter<T> extends RecyclerView.Adapter {


    protected T mDataSet;
    protected AdapterDelegatesManager<T> mDelegatesManager;

    public BaseBindableAdapter() {
    }

    public BaseBindableAdapter(AdapterDelegatesManager<T> mDelegatesManager) {
        this.mDelegatesManager = mDelegatesManager;
    }

    public BaseBindableAdapter(List<AdapterDelegate> delegates) {
        mDelegatesManager = new AdapterDelegatesManager<>();
        mDelegatesManager.addDelegates(delegates);
    }

    public BaseBindableAdapter(T mDataSet, AdapterDelegatesManager<T> delegatesManager) {
        this(delegatesManager);
        this.mDataSet = mDataSet;
    }

    public BaseBindableAdapter(T mDataSet, List<AdapterDelegate> delegates) {
        this(delegates);
        this.mDataSet = mDataSet;
    }

    /**
     * Update list
     */
    public void setDataSet(T mDataSet) {
        this.mDataSet = mDataSet;
        dataChanged();
    }

    protected void dataChanged() {

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
