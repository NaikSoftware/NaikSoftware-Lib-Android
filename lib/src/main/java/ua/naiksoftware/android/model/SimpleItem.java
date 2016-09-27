package ua.naiksoftware.android.model;


import android.support.annotation.Nullable;

/**
 * Simple item for using in delegates
 *
 * @author naik, donchenko_r
 */
public class SimpleItem<T> implements BaseModel {

    public static final int DEFAULT_ITEM_TYPE_TAG = -1;

    public final int itemTypeTag;
    public final T value;
    public final long id;

    public SimpleItem(T value) {
        this(DEFAULT_ITEM_TYPE_TAG, value, null);
    }

    public SimpleItem(T value, long id) {
        this(DEFAULT_ITEM_TYPE_TAG, value, id);
    }

    public SimpleItem(int itemTypeTag) {
        this(itemTypeTag, null);
    }

    public SimpleItem(int itemTypeTag, Long id) {
        this(itemTypeTag, null, id);
    }

    public SimpleItem(int itemTypeTag, T value, @Nullable Long id) {
        this.itemTypeTag = itemTypeTag;
        this.value = value;
        if (id != null) this.id = id;
        else {
            long defaultId = hashCode();
            this.id = defaultId > 0 ? -defaultId : defaultId;
        }
    }

    public T getValue() {
        return value;
    }

    @Override
    public Long getId() {
        return id;
    }
}
