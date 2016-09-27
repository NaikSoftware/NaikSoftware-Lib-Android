package ua.naiksoftware.android.adapter.delegate;

import android.databinding.ViewDataBinding;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.util.List;

import ua.naiksoftware.android.BR;
import ua.naiksoftware.android.adapter.util.BindingHolder;
import ua.naiksoftware.android.model.BaseModel;
import ua.naiksoftware.android.model.SimpleItem;

/**
 * Delegate for simple items
 */
public class ModelItemDelegate extends BaseBindableAdapterDelegate<List<BaseModel>, ViewDataBinding> {

    private final int mModelId;
    private final int mItemLayoutResId;
    private final ViewTypeClause mViewTypeClause;

    public ModelItemDelegate(@NonNull Class<? extends BaseModel> modelClass, @LayoutRes int itemLayoutResId) {
        this(itemLayoutResId, 0, new SimpleViewTypeClause(modelClass));
    }

    public ModelItemDelegate(@NonNull Class<? extends BaseModel> modelClass, @LayoutRes int itemLayoutResId, @IdRes int modelId) {
        this(itemLayoutResId, modelId, new SimpleViewTypeClause(modelClass));
    }

    /**
     * Delegate for using with SimpleItem model
     *
     * @param itemTypeTag type to react to model with this type
     * @param itemLayoutResId layout for item
     */
    public ModelItemDelegate(int itemLayoutResId, int itemTypeTag) {
        this(itemLayoutResId, 0, new SimpleItemViewTypeClause(SimpleItem.class, itemTypeTag));
    }

    public ModelItemDelegate(@LayoutRes int itemLayoutResId, int modelId, ViewTypeClause viewTypeClause) {
        mItemLayoutResId = itemLayoutResId;
        mViewTypeClause = viewTypeClause;
        mModelId = modelId != 0 ? modelId : BR.model;
    }

    @Override
    public boolean isForViewType(@NonNull List<BaseModel> items, int position) {
        return mViewTypeClause.isForViewType(items, position);
    }

    @NonNull
    @Override
    public BindingHolder<ViewDataBinding> onCreateViewHolder(ViewGroup parent) {
        return BindingHolder.newInstance(mItemLayoutResId, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull List<BaseModel> items, int position, @NonNull BindingHolder<ViewDataBinding> holder) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(mModelId, items.get(position));
        binding.executePendingBindings();
    }

    @Override
    public long getItemId(List<BaseModel> dataSource, int position) {
        return dataSource.get(position).getId();
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
