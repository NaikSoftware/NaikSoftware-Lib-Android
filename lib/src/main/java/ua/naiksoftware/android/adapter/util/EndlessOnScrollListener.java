package ua.naiksoftware.android.adapter.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Use this listener to implement lazy loading
 */
//TODO: Investigate Nikolay's approach to do this
public class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

    private static final int DEFAULT_VISIBLE_THRESOLD = 5;
    public static String TAG = "EndlessOnScrollListener";
    private final OnLoadMoreListener mMoreListener;

    private int visibleThreshold; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public interface OnLoadMoreListener {
        void onLoadMore();
        boolean isLoading();
    }


    public EndlessOnScrollListener(@NonNull OnLoadMoreListener listener) {
        this(listener, DEFAULT_VISIBLE_THRESOLD);
    }

    public EndlessOnScrollListener(@NonNull OnLoadMoreListener listener, int visibleThreshold) {
        mMoreListener = listener;
        this.visibleThreshold = visibleThreshold;
    }

    public void setRecyclerView(final RecyclerView _recyclerView) {
        mRecyclerView = _recyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView == null) recyclerView = mRecyclerView;
        super.onScrolled(recyclerView, dx, dy);

        if (dy < 0) return;

        getLayoutManager(recyclerView);
        if (mLayoutManager == null) return;

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        firstVisibleItem = getFirstVisibleItemPosition();

        if (!mMoreListener.isLoading()
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

            // End has been reached
            mMoreListener.onLoadMore();

        }
    }

    private int getFirstVisibleItemPosition() {
        int pos = 0;
        if (mLayoutManager instanceof LinearLayoutManager) {
            pos = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            pos = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null)[0];
        }
        return pos;
    }

    private void getLayoutManager(RecyclerView _recyclerView) {
        if (mLayoutManager == null && _recyclerView != null) {
            mLayoutManager = _recyclerView.getLayoutManager();
        }
    }
}