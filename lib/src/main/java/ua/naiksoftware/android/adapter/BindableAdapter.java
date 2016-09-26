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
        super(new ArrayList<T>());
    }

    public BindableAdapter(List<AdapterDelegate> delegates) {
        super(new ArrayList<T>(), delegates);
    }

    public BindableAdapter(List<T> mDataSet, List<AdapterDelegate> delegates) {
        super(mDataSet, delegates);
    }

    @Override
    public int getItemCount() {
        List<T> dataSet = getDataSet();
        return dataSet == null ? 0 : dataSet.size();
    }
}
