package ua.naiksoftware.android.adapter;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.android.adapter.delegate.AdapterDelegate;
import ua.naiksoftware.android.adapter.util.AdapterDelegatesManager;

/**
 * RecyclerView Adapter for using with data binding
 */
public class BindableAdapter<T> extends BaseBindableAdapter<List<T>> {


    public BindableAdapter() {
        mDataSet = new ArrayList<>();
    }

    public BindableAdapter(AdapterDelegatesManager<List<T>> mDelegatesManager) {
        super(mDelegatesManager);
        mDataSet = new ArrayList<>();
    }

    public BindableAdapter(List<AdapterDelegate> delegates) {
        super(delegates);
        mDataSet = new ArrayList<>();
    }

    public BindableAdapter(List<T> mDataSet, AdapterDelegatesManager<List<T>> delegatesManager) {
        super(mDataSet, delegatesManager);
    }

    public BindableAdapter(List<T> mDataSet, List<AdapterDelegate> delegates) {
        super(mDataSet, delegates);
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }
}
