package ua.naiksoftware.android.adapter.delegate.viewtype;

import android.support.annotation.NonNull;

import java.util.List;

import ua.naiksoftware.android.model.SimpleItem;

public class SimpleItemViewTypeCondition extends ClassViewTypeCondition {

    private final int itemTypeTag;

    public SimpleItemViewTypeCondition(int itemTypeTag) {
        super(SimpleItem.class);
        this.itemTypeTag = itemTypeTag;
    }

    @Override
    public boolean isForViewType(List<?> items, int position) {
        return super.isForViewType(items, position) && ((SimpleItem) items.get(position)).itemTypeTag == itemTypeTag;
    }

}