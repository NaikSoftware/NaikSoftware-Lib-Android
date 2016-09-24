package ua.naiksoftware.android.adapter.delegate;

/**
 * Created by roman.donchenko on 18.01.2016
 */
public interface AdapterDelegate<T> extends com.hannesdorfmann.adapterdelegates2.AdapterDelegate<T> {

    long getItemId(final T dataSource, final int position);
}
