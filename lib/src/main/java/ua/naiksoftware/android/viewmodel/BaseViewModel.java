package ua.naiksoftware.android.viewmodel;

/**
 * Created by naik on 3/14/16.
 */

import android.content.Context;
import android.support.annotation.CallSuper;

public abstract class BaseViewModel implements ViewModel {

    private Context mContext;

    public BaseViewModel(Context context) {
        this.mContext = context;
    }

    @CallSuper
    @Override
    public void onDestroy() {
        mContext = null;
    }

    protected Context getContext() {
        return mContext;
    }

    protected String getString(int resId) {
        return mContext == null ? null : mContext.getString(resId);
    }

    protected String getString(int resId, Object... formatArgs) {
        return mContext == null ? null : mContext.getString(resId, formatArgs);
    }
}

