package ua.naiksoftware.android.adapter.delegate.viewtype;

import java.util.List;

public interface ViewTypeCondition {

    boolean isForViewType(List<?> items, int position);
}