package ua.naiksoftware.android.adapter.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int SPACE_LEFT = 1;
    public static final int SPACE_RIGHT = 2;
    public static final int SPACE_TOP = 4;
    public static final int SPACE_BOTTOM = 8;
//    public static final int SPACE_GRID = 16;

    protected final int mSpacingConfig;
    protected final boolean mDrawLeft;
    protected final boolean mDrawTop;
    protected final boolean mDrawRight;
    protected final boolean mDrawBottom;
    private final boolean mDisableFirstAndLast;

    protected int mSpacing;

    public DividerItemDecoration(final int spacing) {
        this(spacing, SPACE_TOP);
    }

    public DividerItemDecoration(final int spacing, int spacingConfig) {
        this(spacing, spacingConfig, false);
    }

    public DividerItemDecoration(final int spacing, int spacingConfig, boolean disableFirstAndLast) {
        mSpacing = spacing;
        mSpacingConfig = spacingConfig;

        mDrawLeft = check(SPACE_LEFT);
        mDrawTop = check(SPACE_TOP);
        mDrawRight = check(SPACE_RIGHT);
        mDrawBottom = check(SPACE_BOTTOM);

        mDisableFirstAndLast = disableFirstAndLast;
    }

    @Override
    public void getItemOffsets(Rect outRect, View child, RecyclerView parent, RecyclerView.State state) {
        if (mDrawLeft && needDrawIfFirst(child, parent)) outRect.left = mSpacing;
        if (mDrawTop && needDrawIfFirst(child, parent)) outRect.top = mSpacing;
        if (mDrawRight && needDrawIfLast(child, parent)) outRect.right = mSpacing;
        if (mDrawBottom && needDrawIfLast(child, parent)) outRect.bottom = mSpacing;
    }

    protected boolean check(int value) {
        return (mSpacingConfig & value) == value;
    }

    protected boolean isFirstItem(View child, RecyclerView parent) {
        return parent.getChildAdapterPosition(child) == 0;
    }

    protected boolean isLastItem(View child, RecyclerView parent) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        return adapter != null && parent.getChildAdapterPosition(child) == adapter.getItemCount() - 1;
    }

    protected boolean needDrawIfFirst(View child, RecyclerView parent) {
        return !(mDisableFirstAndLast && isFirstItem(child, parent));
    }

    protected boolean needDrawIfLast(View child, RecyclerView parent) {
        return !(mDisableFirstAndLast && isLastItem(child, parent));
    }

}
