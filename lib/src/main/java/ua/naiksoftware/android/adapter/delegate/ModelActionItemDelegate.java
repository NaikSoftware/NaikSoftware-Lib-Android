package ua.naiksoftware.android.adapter.delegate;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import ua.naiksoftware.android.BR;
import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;
import ua.naiksoftware.android.adapter.util.BindingHolder;
import ua.naiksoftware.android.model.BaseModel;

/**
 * Delegate for simple items with ActionHandler
 */
public class ModelActionItemDelegate extends ModelItemDelegate {

    protected ActionClickListener mActionHandler;

    public ModelActionItemDelegate(ActionClickListener actionHandler, @NonNull Class<? extends BaseModel> modelClass, @LayoutRes int itemLayoutResId) {
        super(modelClass, itemLayoutResId, 0);
        mActionHandler = actionHandler;
    }

    public ModelActionItemDelegate(ActionClickListener actionHandler, @NonNull Class<? extends BaseModel> modelClass, @LayoutRes int itemLayoutResId, int modelId) {
        super(modelClass, itemLayoutResId, modelId);
        mActionHandler = actionHandler;
    }

    public ModelActionItemDelegate(ActionClickListener actionHandler, @LayoutRes int itemLayoutResId, int modelId, ViewTypeClause viewTypeClause) {
        super(itemLayoutResId, modelId, viewTypeClause);
        mActionHandler = actionHandler;
    }

    public ModelActionItemDelegate(ActionClickListener actionHandler, int itemTypeTag, @LayoutRes int itemLayoutResId) {
        super(itemLayoutResId, itemTypeTag);
        mActionHandler = actionHandler;
    }

    @NonNull
    @Override
    public BindingHolder<ViewDataBinding> onCreateViewHolder(ViewGroup parent) {
        BindingHolder<ViewDataBinding> holder = super.onCreateViewHolder(parent);
        final ActionClickListener actionHandler = getActionHandler();
        if (actionHandler != null) {
            holder.getBinding().setVariable(BR.actionHandler, actionHandler);
        }
        return holder;
    }

    public ActionClickListener getActionHandler() {
        return mActionHandler;
    }
}
