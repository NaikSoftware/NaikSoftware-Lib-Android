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
    private final Map<Class<? extends BaseModel>, Map<Field, Annotation>> mFieldsMapping = new HashMap<>();

    public ModelDelegate(@NonNull Class<? extends BaseModel> modelClass, @LayoutRes int itemLayoutResId) {
        this(itemLayoutResId, new SimpleViewTypeClause(modelClass));
    }

    public ModelDelegate(@LayoutRes int itemLayoutResId, ViewTypeClause viewTypeClause) {
        this(null, null, viewTypeClause, itemLayoutResId);
    }

    /**
     * If you use support-library you must pass Activity as context
     */
    public ModelDelegate(Context context, ActionClickListener actionHandler, @NonNull Class<? extends BaseModel> modelClass, @LayoutRes int itemLayoutResId) {
        this(context, actionHandler, new SimpleViewTypeClause(modelClass), itemLayoutResId);
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
     * If you use support-library you must pass Activity as context
     *
     * @param itemTypeTag type to react to {@link SimpleItem} with this type in data set
     * @param itemLayoutResId layout for item
     */
    public ModelDelegate(Context context, ActionClickListener actionHandler, int itemTypeTag, @LayoutRes int itemLayoutResId) {
        this(context, actionHandler, new SimpleItemViewTypeClause(SimpleItem.class, itemTypeTag), itemLayoutResId);
    }

    public ModelDelegate(Context context, ActionClickListener actionHandler, ViewTypeClause viewTypeClause, @LayoutRes int itemLayoutResId) {
        super(actionHandler, context);
        mItemLayoutResId = itemLayoutResId;
        mViewTypeClause = viewTypeClause;
    }

    @Override
    public boolean isForViewType(@NonNull List<BaseModel> items, int position) {
        return mViewTypeClause.isForViewType(items, position);
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
                Class<? extends Annotation> fieldType = annotation.getClass();
                if (fieldType == Text.class) {
                    TextView view = holder.getView(((Text) annotation).value());
                    view.setText((CharSequence) field.get(model));
                } else if (fieldType == DrawableId.class) {
                    ImageView view = holder.getView(((DrawableId) annotation).value());
                    view.setImageResource((int) field.get(model));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
