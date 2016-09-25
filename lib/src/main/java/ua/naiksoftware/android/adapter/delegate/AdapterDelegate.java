package ua.naiksoftware.android.adapter.delegate;

public interface AdapterDelegate<T> extends com.hannesdorfmann.adapterdelegates2.AdapterDelegate<T> {

    long getItemId(final T dataSource, final int position);
}
