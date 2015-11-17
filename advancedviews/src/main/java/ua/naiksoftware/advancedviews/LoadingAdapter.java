package ua.naiksoftware.advancedviews;

import android.support.annotation.CallSuper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Savchenko_n
 */
public abstract class LoadingAdapter<T> extends RecyclerAdapterFooterHeader<RecyclerView.ViewHolder> {

    public static final String TAG = LoadingAdapter.class.getSimpleName();

    public static final int COUNT_LOAD_NEW_MIN = 10; // load new items, when end of list reached

    private List<T> list;
    private Loader<T> loader;
    private int totalSize, currentTop, prefOnPageSize;
    private static final int STATE_LOAD_PREV = 1, STATE_LOAD_NEXT = 2;
    private int state;
    private int countLoadNew = COUNT_LOAD_NEW_MIN;
    private int loadingViewId;

    public LoadingAdapter(int prefOnPageSize, Loader<T> loader, int loadingViewId) {
        Log.d(TAG, "Constructor " + " " + this);
        this.prefOnPageSize = prefOnPageSize;
        this.loader = loader;
        this.loadingViewId = loadingViewId;
        list = new ArrayList<>(prefOnPageSize);
        totalSize = Integer.MAX_VALUE;
    }

    public void setLayoutManagerAndLoad(RecyclerView.LayoutManager manager) {
        super.setLayoutManager(manager);
        if (manager instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) manager).getSpanCount();
            countLoadNew += spanCount - countLoadNew % spanCount; // "align" loading items count
            int alignOnPage = prefOnPageSize % spanCount;
            prefOnPageSize += alignOnPage;
            if ( list.size() != 0 && alignOnPage > 0) {
                int diff = totalSize - (currentTop + 1 + list.size());
                if (diff > 0) {
                    loadToEnd(alignOnPage);// else just add elements for fill screen place
                } else {
                    loadToEnd(alignOnPage + diff);
                }
            }
        }
        if (list.size() == 0) {
            loadToEnd(prefOnPageSize); // first loading
        }
    }

    private void loadToEnd(final int count) {
        Log.d(TAG, "LoadToEnd: " + count + " total:" + totalSize + " top: " + currentTop + " " + this);
        if (state != 0) return;
        state = STATE_LOAD_NEXT;
        useFooterAsync(true);

        loader.get(currentTop + list.size(), count, new LoadingCallback<T>() {
            @Override
            public void success(List<T> items, int totalOnServer) {
                useFooter(false);
                totalSize = totalOnServer;
                for (int i = 0; i < items.size(); i++) {
                    list.add(items.get(i));
                    notifyItemInserted(list.size() - 1);
                }
                if (list.size() > prefOnPageSize) {
                    int toRemove = items.size() == countLoadNew ? items.size() : 0;
                    currentTop += toRemove;
                    for (int i = 0; i < toRemove; i++) {
                        list.remove(0);
                        notifyItemRemoved(0);
                    }
                }
                state = 0;
            }

            @Override
            public void fail(Throwable t) {

            }
        });
    }

    private void loadToStart(final int count) {
        Log.d(TAG, "LoadToStart: " + count + " total:" + totalSize + " top: " + currentTop + " " + this);
        if (state != 0) return;
        state = STATE_LOAD_PREV;
        useHeaderAsync(true);

        loader.get(currentTop - count, count, new LoadingCallback<T>() {
            @Override
            public void success(List<T> items, int totalOnServer) {
                useHeader(false);
                totalSize = totalOnServer;
                list.addAll(0, items);
                notifyItemRangeInserted(0, items.size());
                if (list.size() > prefOnPageSize) {
                    int toRemove = items.size();
                    currentTop -= toRemove;
                    for (int i = 0; i < toRemove; i++) {
                        list.remove(list.size() - 1);
                        notifyItemRemoved(list.size() - 1);
                    }
                }
                state = 0;
            }

            @Override
            public void fail(Throwable t) {

            }
        });
    }

    @Override
    public ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(loadingViewId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(loadingViewId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public abstract ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    @CallSuper
    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        if (position == list.size() - 1 && currentTop + position + 1 < totalSize) {
            int upper = currentTop + position + 1;
            int countNew = upper + countLoadNew > totalSize ? totalSize - upper : countLoadNew;
            loadToEnd(countNew);
        } else if (position == 0 && currentTop > 0) {
            int countNew = currentTop < countLoadNew ? currentTop : countLoadNew;
            loadToStart(countNew);
        }
    }

    public List<T> getItemList() {
        return list;
    }

    @Override
    public int getBasicItemCount() {
        return list.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemClickListener<T> listener;

        public ViewHolder(View v) {
            super(v);
        }

        public ViewHolder(View view, ItemClickListener<T> listener) {
            super(view);
            this.listener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (listener != null && pos >= 0 && pos < list.size()) {
                listener.onItemClick(list.get(pos));
            }
        }
    }

    public interface Loader<T> {
        void get(int from, int limit, LoadingCallback<T> callback);
    }
}
