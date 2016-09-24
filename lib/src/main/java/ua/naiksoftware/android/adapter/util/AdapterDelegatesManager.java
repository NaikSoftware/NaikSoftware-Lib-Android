package ua.naiksoftware.android.adapter.util;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import ua.naiksoftware.android.adapter.delegate.AdapterDelegate;

public class AdapterDelegatesManager<T> extends com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager<T> {

    private List<? extends AdapterDelegate> mDelegateList;

    public void addDelegates(final List<? extends AdapterDelegate> itemDelegates) {
        mDelegateList = itemDelegates;
        for (AdapterDelegate<T> adapter : itemDelegates) {
            super.addDelegate(adapter);
        }
    }

    public long getItemId(final T dataSource, final int position) {
        final AdapterDelegate adapterDelegate = getAdapterDelegate(dataSource, position);
        return adapterDelegate != null ? adapterDelegate.getItemId(dataSource, position) : RecyclerView.NO_ID;
    }

    public AdapterDelegate getAdapterDelegate(final T dataSource, final int position) {
        for (AdapterDelegate itemDelegate : mDelegateList) {
            if (itemDelegate.isForViewType(dataSource, position))
                return itemDelegate;
        }
        return null;
    }
}
