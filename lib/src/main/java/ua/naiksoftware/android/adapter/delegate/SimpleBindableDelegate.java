package ua.naiksoftware.android.adapter.delegate;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ua.naiksoftware.android.BR;
import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;
import ua.naiksoftware.android.adapter.delegate.viewtype.ClassViewTypeCondition;
import ua.naiksoftware.android.adapter.delegate.viewtype.SimpleItemViewTypeCondition;
import ua.naiksoftware.android.adapter.delegate.viewtype.ViewTypeCondition;
import ua.naiksoftware.android.adapter.util.BindingHolder;
import ua.naiksoftware.android.model.BaseModel;

/**
 * Delegate for simple items with data-binding
 */
public class SimpleBindableDelegate extends BaseBindableAdapterDelegate<List<BaseModel>, ViewDataBinding> {

    private final int mModelId;
    private final int mItemLayoutResId;
    private final ViewTypeCondition mViewTypeClause;

    public static class Builder {

        private int mItemLayoutResId;
        private int mModelId = BR.model;
        private ViewTypeCondition mViewTypeCondition;
        private ActionClickListener mActionHandler;

        public Builder(int itemLayoutResId) {
            mItemLayoutResId = itemLayoutResId;
        }

        public Builder forClass(Class<? extends BaseModel> modelClass) {
            mViewTypeCondition = new ClassViewTypeCondition(modelClass);
            return this;
        }

        public Builder forCondition(ViewTypeCondition viewTypeCondition) {
            mViewTypeCondition = viewTypeCondition;
            return this;
        }

        public Builder forSimpleItem(int itemTypeTag) {
            mViewTypeCondition = new SimpleItemViewTypeCondition(itemTypeTag);
            return this;
        }

        public Builder withActionHandler(ActionClickListener actionHandler) {
            mActionHandler = actionHandler;
            return this;
        }

        public Builder setModelId(int modelId) {
            mModelId = modelId;
            return this;
        }

        public SimpleBindableDelegate build() {
            return new SimpleBindableDelegate(mItemLayoutResId, mModelId, mActionHandler, mViewTypeCondition);
        }
    }

    public SimpleBindableDelegate(@LayoutRes int itemLayoutResId, int modelId, ActionClickListener actionHandler, ViewTypeCondition viewTypeClause) {
        super(actionHandler);
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
    public BindingHolder<ViewDataBinding> createHolder(LayoutInflater inflater, ViewGroup parent) {
        return BindingHolder.newInstance(mItemLayoutResId, inflater, parent);
    }

    @Override
    public void bindHolder(@NonNull List<BaseModel> items, int position, @NonNull BindingHolder<ViewDataBinding> holder) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(mModelId, items.get(position));
        binding.executePendingBindings();
    }

    @Override
    public long getItemId(List<BaseModel> dataSource, int position) {
        return dataSource.get(position).getId();
    }
}
