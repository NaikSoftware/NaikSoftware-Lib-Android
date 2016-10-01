package ua.naiksoftware.android.adapter;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.android.adapter.delegate.AdapterDelegate;

/**
 * RecyclerView Adapter for using with lists
 */
public class DelegatesAdapter<T> extends BaseDelegatesAdapter<List<T>> {


    public DelegatesAdapter() {
        super(new ArrayList<T>());
    }

    public DelegatesAdapter(AdapterDelegate... delegates) {
        super(new ArrayList<T>(), delegates);
    }

    public DelegatesAdapter(List<AdapterDelegate> delegates) {
        super(new ArrayList<T>(), delegates);
    }

    public DelegatesAdapter(List<T> mDataSet, List<AdapterDelegate> delegates) {
        super(mDataSet, delegates);
    }

    public DelegatesAdapter(List<T> mDataSet, AdapterDelegate... delegates) {
        super(mDataSet, delegates);
    }

    @Override
    public int getItemCount() {
        List<T> dataSet = getDataSet();
        return dataSet == null ? 0 : dataSet.size();
    }
}
