package ua.naiksoftware.android.adapter.delegate.viewtype;

import android.support.annotation.NonNull;

import java.util.List;

public class ClassViewTypeCondition implements ViewTypeCondition {

    private final Class<?> mClass;

    public ClassViewTypeCondition(@NonNull Class<?> aClass) {
        mClass = aClass;
    }

    @Override
    public boolean isForViewType(List<?> items, int position) {
        return mClass.isAssignableFrom(items.get(position).getClass());
    }

}