package ua.naiksoftware.android.adapter.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ColorDividerItemDecoration extends DividerItemDecoration {

    private final Paint mPaint;

    public ColorDividerItemDecoration(int color, int spacing, int spacingConfig, boolean disableFirstAndLast) {
        super(spacing, spacingConfig, disableFirstAndLast);
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            drawDivider(c, child, parent);
        }
    }

    private void drawDivider(Canvas c, View child, RecyclerView parent) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        int left, top, right, bottom;

        if (mDrawLeft && needDrawIfFirst(child, parent)) {
            top = child.getTop() - params.topMargin;
            bottom = child.getBottom() + params.bottomMargin;
            right = child.getLeft() - params.leftMargin;
            left = right - mSpacing;
            drawDivider(c, left, top, right, bottom);
        }
        if (mDrawTop && needDrawIfFirst(child, parent)) {
            bottom = child.getTop() - params.topMargin;
            top = bottom - mSpacing;
            left = child.getLeft() - params.leftMargin - (mDrawLeft ? mSpacing : 0);
            right = child.getRight() + params.rightMargin + (mDrawRight ? mSpacing : 0);
            drawDivider(c, left, top, right, bottom);
        }
        if (mDrawRight && needDrawIfLast(child, parent)) {
            top = child.getTop() - params.topMargin;
            bottom = child.getBottom() + params.bottomMargin;
            left = child.getRight() + params.rightMargin;
            right = left + mSpacing;
            drawDivider(c, left, top, right, bottom);
        }
        if (mDrawBottom && needDrawIfLast(child, parent)) {
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mSpacing;
            left = child.getLeft() - params.leftMargin - (mDrawLeft ? mSpacing : 0);
            right = child.getRight() + params.rightMargin + (mDrawRight ? mSpacing : 0);
            drawDivider(c, left, top, right, bottom);
        }

    }

    private void drawDivider(Canvas c, int left, int top, int right, int bottom) {
        c.drawRect(left, top, right, bottom, mPaint);
    }
}
