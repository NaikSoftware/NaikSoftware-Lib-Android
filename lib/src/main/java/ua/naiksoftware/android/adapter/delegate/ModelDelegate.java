package ua.naiksoftware.android.adapter.delegate;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;
import ua.naiksoftware.android.adapter.util.SimpleViewHolder;
import ua.naiksoftware.android.model.BaseModel;
import ua.naiksoftware.android.model.SimpleItem;

/**
 * Delegate for simple items (if you are using data binding
 * use {@link ModelBindableDelegate} instead)
 */
public class ModelDelegate extends BaseAdapterDelegate<BaseModel> {

    private final int mItemLayoutResId;
    private final ViewTypeClause mViewTypeClause;
    private final ActionClickListener mActionHandler;

    public ModelDelegate(@NonNull Class<? extends BaseModel> modelClass, @LayoutRes int itemLayoutResId) {
        this(itemLayoutResId, new SimpleViewTypeClause(modelClass));
    }

    public ModelDelegate(@LayoutRes int itemLayoutResId, ViewTypeClause viewTypeClause) {
        this(itemLayoutResId, null, viewTypeClause);
    }

    public ModelDelegate(ActionClickListener actionHandler, @NonNull Class<? extends BaseModel> modelClass, @LayoutRes int itemLayoutResId) {
        this(itemLayoutResId, actionHandler, new SimpleViewTypeClause(modelClass));
    }

    /**
     * Delegate for using with SimpleItem model
     *
     * @param itemTypeTag type to react to {@link SimpleItem} with this type in data set
     * @param itemLayoutResId layout for item
     */
    public ModelDelegate(@LayoutRes int itemLayoutResId, int itemTypeTag) {
        this(itemLayoutResId, new SimpleItemViewTypeClause(SimpleItem.class, itemTypeTag));
    }

    /**
     * Delegate for using with SimpleItem model and ActionHandler
     *
     * @param itemTypeTag type to react to {@link SimpleItem} with this type in data set
     * @param itemLayoutResId layout for item
     */
    public ModelDelegate(ActionClickListener actionHandler, int itemTypeTag, @LayoutRes int itemLayoutResId) {
        this(itemLayoutResId, actionHandler, new SimpleItemViewTypeClause(SimpleItem.class, itemTypeTag));
    }

    public ModelDelegate(@LayoutRes int itemLayoutResId, ActionClickListener actionHandler, ViewTypeClause viewTypeClause) {
        mItemLayoutResId = itemLayoutResId;
        mViewTypeClause = viewTypeClause;
        mActionHandler = actionHandler;
    }

    @Override
    public boolean isForViewType(@NonNull List<BaseModel> items, int position) {
        return mViewTypeClause.isForViewType(items, position);
    }

    @Override
    public long getItemId(List<BaseModel> dataSource, int position) {
        return dataSource.get(position).getId();
    }

    protected ActionClickListener getActionHandler() {
        return mActionHandler;
    }

    @NonNull
    @Override
    public SimpleViewHolder createHolder(LayoutInflater inflater, ViewGroup parent) {
        // TODO: 01.10.16 Find view by ids (read from annotations)
        return new SimpleViewHolder(mItemLayoutResId, parent, inflater);
    }

    @Override
    public void bindHolder(@NonNull List<BaseModel> items, int position, @NonNull SimpleViewHolder holder) {
        // TODO: 01.10.16 Bind ids from model (parse annotations)
    }

    public interface ViewTypeClause {
        boolean isForViewType(List<?> items, int position);

    }

    private static class SimpleViewTypeClause implements ViewTypeClause {

        private final Class<?> mClass;

        SimpleViewTypeClause(@NonNull Class<?> aClass) {
            mClass = aClass;
        }
        @Override
        public boolean isForViewType(List<?> items, int position) {
            return mClass.isAssignableFrom(items.get(position).getClass());
        }

    }

    private static class SimpleItemViewTypeClause extends SimpleViewTypeClause {

        private final int itemTypeTag;

        SimpleItemViewTypeClause(@NonNull Class<?> aClass, int itemTypeTag) {
            super(aClass);
            this.itemTypeTag = itemTypeTag;
        }
        @Override
        public boolean isForViewType(List<?> items, int position) {
            return super.isForViewType(items, position) && ((SimpleItem) items.get(position)).itemTypeTag == itemTypeTag;
        }

    }
}
