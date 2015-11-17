package ua.naiksoftware.advancedviews;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public abstract class RecyclerAdapterFooterHeader<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    public static final String TAG = RecyclerAdapterFooterHeader.class.getSimpleName();

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_BASIC = 3;

    private boolean useHeader, useFooter;

    public void setLayoutManager(final RecyclerView.LayoutManager manager) {
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = (GridLayoutManager) manager;
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case TYPE_BASIC:
                            return 1;
                        case TYPE_FOOTER:
                        case TYPE_HEADER:
                            return gridManager.getSpanCount();
                        default:
                            return -1;
                    }
                }
            });
        }
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, viewType);
        } else if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        }
        return onCreateBasicItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (position == 0 && holder.getItemViewType() == TYPE_HEADER) {
            onBindHeaderView(holder, position);
        } else if (position == getBasicItemCount() && holder.getItemViewType() == TYPE_FOOTER) {
            onBindFooterView(holder, position);
        } else {
            onBindBasicItemView(holder, position - (useHeader ? 1 : 0));
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = getBasicItemCount();
        if (useHeader) {
            itemCount += 1;
        }
        if (useFooter) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && useHeader) {
            return TYPE_HEADER;
        }
        if (position == getBasicItemCount() && useFooter) {
            return TYPE_FOOTER;
        }
        return TYPE_BASIC;
    }

    public void useHeaderAsync(final boolean use) {
        if (use != useHeader) {
            int headerPosition = 0;
            Message msg = new Message();
            msg.arg1 = TYPE_HEADER;
            msg.arg2 = use ? 1 : 0;
            msg.what = headerPosition;
            handler.sendMessage(msg);

        }
    }

    public void useFooterAsync(final boolean use) {
        if (use != useFooter) {
            final int footerPosition = getItemCount();
            Message msg = new Message();
            msg.arg1 = TYPE_FOOTER;
            msg.arg2 = use ? 1 : 0;
            msg.what = footerPosition;
            handler.sendMessage(msg);
        }
    }

    public void useHeader(final boolean use) {
        if (use != useHeader) {
            useHeader = use;
            int headerPosition = 0;
            if (use) notifyItemInserted(headerPosition);
            else notifyItemRemoved(headerPosition);
        }
    }

    public void useFooter(boolean use) {
        if (use != useFooter) {
            useFooter = use;
            int footerPosition = getItemCount();
            if (use) footerPosition -= 1;
            if (use) notifyItemInserted(footerPosition);
            else notifyItemRemoved(footerPosition);
        }
    }

    public boolean isUseFooter() {
        return useFooter;
    }

    public boolean isUseHeader() {
        return useHeader;
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos = msg.what;
            if (msg.arg2 == 0) { // footer or header was removed
                if (msg.arg1 == TYPE_FOOTER) useFooter = false;
                else if (msg.arg1 == TYPE_HEADER) useHeader = false;
                if (getItemViewType(pos) != TYPE_BASIC) notifyItemChanged(pos);
            } else { // footer or header was inserted
                if (msg.arg1 == TYPE_FOOTER) useFooter = true;
                else if (msg.arg1 == TYPE_HEADER) useHeader = true;
                if (getItemViewType(pos) != TYPE_BASIC) notifyItemInserted(pos);
            }
            removeMessages(pos);
        }
    };

    public abstract T onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderView(T holder, int position);

    public abstract T onCreateFooterViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindFooterView(T holder, int position);

    public abstract T onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBasicItemView(T holder, int position);

    public abstract int getBasicItemCount();

}
