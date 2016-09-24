package ua.naiksoftware.android.adapter.util;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.riversoft.core.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for simple configuring Adapter for RecyclerView
 */
public class ListConfig {

    private final RecyclerView.Adapter mAdapter;
    private final LayoutManagerProvider mLayoutManagerProvider;
    private final List<RecyclerView.ItemDecoration> mItemDecorations;
    private final List<RecyclerView.OnScrollListener> mScrollListeners;
    private final ItemTouchHelper mItemTouchHelper;
    private final boolean mHasFixedSize;

    public ListConfig(RecyclerView.Adapter _adapter, LayoutManagerProvider _layoutManagerProvider, List<RecyclerView.ItemDecoration> _itemDecorations, List<RecyclerView.OnScrollListener> _scrollListeners, ItemTouchHelper itemTouchHelper, boolean _hasFixedSize) {
        mAdapter = _adapter;
        mLayoutManagerProvider = _layoutManagerProvider;
        mItemDecorations = _itemDecorations != null
                ? _itemDecorations : Collections.<RecyclerView.ItemDecoration>emptyList();
        mScrollListeners = _scrollListeners != null
                ? _scrollListeners : Collections.<RecyclerView.OnScrollListener>emptyList();
        mItemTouchHelper = itemTouchHelper;
        mHasFixedSize = _hasFixedSize;
    }

    public void applyConfig(RecyclerView recyclerView) {
        final RecyclerView.LayoutManager layoutManager;
        final Context context = recyclerView.getContext();
        if (mAdapter == null || mLayoutManagerProvider == null || (layoutManager = mLayoutManagerProvider.get(context)) == null) return;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(mHasFixedSize);
        recyclerView.setAdapter(mAdapter);
        for (RecyclerView.ItemDecoration itemDecoration : mItemDecorations) {
            recyclerView.addItemDecoration(itemDecoration);
        }
        for (RecyclerView.OnScrollListener scrollListener : mScrollListeners) {
            if (scrollListener instanceof EndlessOnScrollListener)
                ((EndlessOnScrollListener) scrollListener).setRecyclerView(recyclerView);
            recyclerView.addOnScrollListener(scrollListener);
        }
        if (mItemTouchHelper != null) {
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    public static class Builder {
        private final RecyclerView.Adapter mAdapter;
        private LayoutManagerProvider mLayoutManagerProvider;
        private List<RecyclerView.ItemDecoration> mItemDecorations;
        private List<RecyclerView.OnScrollListener> mOnScrollListeners;
        private ItemTouchHelper mItemTouchHelper;
        private boolean mHasFixedSize;
        private int mDefaultDividerOffset = -1;

        public Builder(RecyclerView.Adapter _adapter) {
            mAdapter = _adapter;
        }

        public Builder setLayoutManagerProvider(LayoutManagerProvider layoutManagerProvider) {
            mLayoutManagerProvider = layoutManagerProvider;
            return this;
        }

        public Builder addItemDecoration(RecyclerView.ItemDecoration _itemDecoration) {
            if (mItemDecorations == null) {
                mItemDecorations = new ArrayList<>();
            }
            mItemDecorations.add(_itemDecoration);
            return this;
        }

        public Builder addOnScrollListener(RecyclerView.OnScrollListener _onScrollListener) {
            if (mOnScrollListeners == null) {
                mOnScrollListeners = new ArrayList<>();
            }
            mOnScrollListeners.add(_onScrollListener);
            return this;
        }

        public Builder setHasFixedSize(boolean _isFixedSize) {
            mHasFixedSize = _isFixedSize;
            return this;
        }

        public Builder setDefaultDividerEnabled(boolean _isEnabled) {
            mDefaultDividerOffset = _isEnabled ? 0 : -1;
            return this;
        }

        public Builder setDefaultDividerOffset(int _offset) {
            mDefaultDividerOffset = _offset;
            return this;
        }

        public Builder setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
            mItemTouchHelper = itemTouchHelper;
            return this;
        }

        public ListConfig build(Context context) {
            if (mLayoutManagerProvider == null) mLayoutManagerProvider = new SimpleLinearLayoutManagerProvider();
            if (mDefaultDividerOffset >= 0) {
                if (mDefaultDividerOffset == 0) mDefaultDividerOffset = context.getResources()
                        .getDimensionPixelSize(R.dimen.loading_list_divider_size);
                addItemDecoration(new DividerItemDecoration(mDefaultDividerOffset));
            }
            return new ListConfig(
                    mAdapter,
                    mLayoutManagerProvider,
                    mItemDecorations,
                    mOnScrollListeners,
                    mItemTouchHelper, mHasFixedSize);
        }
    }

    public interface LayoutManagerProvider {
        RecyclerView.LayoutManager get(Context context);
    }


    public static class SimpleLinearLayoutManagerProvider implements LayoutManagerProvider {
        @Override
        public RecyclerView.LayoutManager get(Context context) {
            return new LinearLayoutManager(context);
        }
    }

    public static class SimpleGridLayoutManagerProvider implements LayoutManagerProvider {
        private final int mSpanCount;
        private GridLayoutManager.SpanSizeLookup mSpanSizeLookup;

        public SimpleGridLayoutManagerProvider(@IntRange(from = 1) int mSpanCount) {
            this.mSpanCount = mSpanCount;
        }

        public SimpleGridLayoutManagerProvider(int spanCount, GridLayoutManager.SpanSizeLookup spanSizeLookup) {
            mSpanCount = spanCount;
            mSpanSizeLookup = spanSizeLookup;
        }

        @Override
        public RecyclerView.LayoutManager get(Context context) {
            GridLayoutManager layoutManager = new GridLayoutManager(context, mSpanCount);
            if (mSpanSizeLookup != null) layoutManager.setSpanSizeLookup(mSpanSizeLookup);
            return layoutManager;
        }
    }

    public static class SimpleStaggeredGridLayoutManagerProvider implements LayoutManagerProvider {
        private final int mSpanCount;

        public SimpleStaggeredGridLayoutManagerProvider(@IntRange(from = 1) int mSpanCount) {
            this.mSpanCount = mSpanCount;
        }

        @Override
        public RecyclerView.LayoutManager get(Context context) {
            return new StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.VERTICAL);
        }
    }
}

