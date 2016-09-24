package ua.naiksoftware.android.adapter.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import ua.naiksoftware.android.util.ViewUtils;

/**
 * Created by naik on 22.04.16.
 */
public class DownTopScrollListener extends RecyclerView.OnScrollListener {

    public static final String TAG = makeLogTag(DownTopScrollListener.class);

    private static final int DEFAULT_THRESHOLD = 3;

    private final EndlessOnScrollListener.OnLoadMoreListener mMoreListener;
    private final int mThreshold;

    public DownTopScrollListener(@NonNull EndlessOnScrollListener.OnLoadMoreListener listener) {
        this(listener, DEFAULT_THRESHOLD);
    }

    public DownTopScrollListener(@NonNull EndlessOnScrollListener.OnLoadMoreListener listener, int threshold) {
        mMoreListener = listener;
        mThreshold = threshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy <= 0) {

            if (!mMoreListener.isLoading() && ViewUtils.findFirstVisiblePosition(recyclerView) <= mThreshold) {

                LOGD(TAG, "Detected loadmore in DownTopScrollListener");
                mMoreListener.onLoadMore();
            }
        }
    }

}
