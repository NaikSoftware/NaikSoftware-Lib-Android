package ua.naiksoftware.android.adapter.delegate;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;
import ua.naiksoftware.android.adapter.delegate.autobind.BindingField;
import ua.naiksoftware.android.adapter.delegate.autobind.DrawableId;
import ua.naiksoftware.android.adapter.delegate.autobind.Text;
import ua.naiksoftware.android.adapter.delegate.viewtype.ClassViewTypeCondition;
import ua.naiksoftware.android.adapter.delegate.viewtype.SimpleItemViewTypeCondition;
import ua.naiksoftware.android.adapter.delegate.viewtype.ViewTypeCondition;
import ua.naiksoftware.android.adapter.util.SimpleViewHolder;
import ua.naiksoftware.android.model.BaseModel;

/**
 * Delegate for simple items (if you are using data binding
 * use {@link SimpleBindableDelegate} instead)
 */
public class SimpleDelegate extends BaseAdapterDelegate<BaseModel> {

    private final int mItemLayoutResId;
    private final ViewTypeCondition mViewTypeCondition;
    private final Map<Class<? extends BaseModel>, Map<Field, Annotation>> mFieldsMapping = new HashMap<>();

    public static class Builder {

        private final int mItemLayoutResId;
        private ViewTypeCondition mViewTypeCondition;
        private Context mContext = null;
        private ActionClickListener mActionHandler = null;

        public Builder(int itemLayoutResId) {
            mItemLayoutResId = itemLayoutResId;
        }

        public SimpleDelegate forClass(Class<? extends BaseModel> modelClass) {
            mViewTypeCondition = new ClassViewTypeCondition(modelClass);
            return build();
        }

        public SimpleDelegate forCondition(ViewTypeCondition viewTypeCondition) {
            mViewTypeCondition = viewTypeCondition;
            return build();
        }

        public SimpleDelegate forSimpleItem(int itemTypeTag) {
            mViewTypeCondition = new SimpleItemViewTypeCondition(itemTypeTag);
            return build();
        }

        /**
         * If you use support-library you must pass Activity as context
         */
        public Builder withActionHandler(Context context, ActionClickListener actionHandler) {
            mContext = context;
            mActionHandler = actionHandler;
            return this;
        }

        public SimpleDelegate build() {
            return new SimpleDelegate(mContext, mActionHandler, mViewTypeCondition, mItemLayoutResId);
        }
    }

    public SimpleDelegate(Context context, ActionClickListener actionHandler, ViewTypeCondition viewTypeCondition, @LayoutRes int itemLayoutResId) {
        super(actionHandler, context);
        mItemLayoutResId = itemLayoutResId;
        mViewTypeCondition = viewTypeCondition;
    }

    @Override
    public boolean isForViewType(@NonNull List<BaseModel> items, int position) {
        return mViewTypeCondition.isForViewType(items, position);
    }

    @Override
    public long getItemId(List<BaseModel> dataSource, int position) {
        return dataSource.get(position).getId();
    }

    @NonNull
    @Override
    public SimpleViewHolder createHolder(LayoutInflater inflater, ViewGroup parent) {
        return new SimpleViewHolder(mItemLayoutResId, parent, inflater);
    }

    @Override
    public void bindHolder(@NonNull List<BaseModel> items, int position, @NonNull SimpleViewHolder holder) {
        BaseModel model = items.get(position);
        Class<? extends BaseModel> modelClass = model.getClass();
        Map<Field, Annotation> fields;
        if (!mFieldsMapping.containsKey(modelClass)) {
            fields = new HashMap<>();
            for (Field field : modelClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Text.class)) {
                    field.setAccessible(true);
                    Text annotation = field.getAnnotation(Text.class);
                    fields.put(field, annotation);
                    holder.findView(annotation.value());
                } else if (field.isAnnotationPresent(DrawableId.class)) {
                    field.setAccessible(true);
                    DrawableId annotation = field.getAnnotation(DrawableId.class);
                    fields.put(field, annotation);
                    holder.findView(annotation.value());
                } else if (field.isAnnotationPresent(BindingField.class)) {
                    // Find view for use in custom binding method
                    holder.findView(field.getAnnotation(BindingField.class).value());
                }
            }
            mFieldsMapping.put(modelClass, fields);
        } else {
            fields = mFieldsMapping.get(modelClass);
        }

        try {
            for (Map.Entry<Field, Annotation> entry : fields.entrySet()) {
                Field field = entry.getKey();
                Annotation annotation = entry.getValue();
                Class<? extends Annotation> fieldType = annotation.annotationType();
                if (fieldType == Text.class) {
                    TextView view = holder.findAndCache(((Text) annotation).value());
                    view.setText((CharSequence) field.get(model));
                } else if (fieldType == DrawableId.class) {
                    ImageView view = holder.findAndCache(((DrawableId) annotation).value());
                    view.setImageResource((Integer) field.get(model));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
