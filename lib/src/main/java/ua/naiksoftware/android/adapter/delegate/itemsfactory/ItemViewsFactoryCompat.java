package ua.naiksoftware.android.adapter.delegate.itemsfactory;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ua.naiksoftware.android.R;
import ua.naiksoftware.android.adapter.actionhandler.listener.ActionClickListener;

//import static ua.naiksoftware.android.util.LogHelper.*;

/**
 * LayoutInflater factory with support for support-library widgets.
 * If you are not using support-library widgets use {@link ItemViewsFactory}
 * instead this
 */
public class ItemViewsFactoryCompat extends ItemViewsFactoryAbs implements LayoutInflaterFactory {

//    private static final String TAG = makeLogTag(ItemViewsFactoryCompat.class);

    private static final Class<?>[] CONSTRUCTOR_SIGNATURE = new Class[] {
            Context.class, AttributeSet.class };

    private final ActionClickListener mActionHandler;
    private AppCompatDelegate mAppCompatDelegate;
    private Set<String> mActionViewTags = new HashSet<>();

    public ItemViewsFactoryCompat(Activity activity, ActionClickListener actionHandler) {
        mAppCompatDelegate = AppCompatDelegate.create(activity, null);
        mActionHandler = actionHandler;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        // Custom inflation code here
        TypedArray ta = parent.getContext().obtainStyledAttributes(attrs, R.styleable.ActionHandlerAttrs);
        final String actionType = ta.getString(R.styleable.ActionHandlerAttrs_actionType);
        final String actionTypeLongClick = ta.getString(R.styleable.ActionHandlerAttrs_actionTypeLongClick);
        ta.recycle();

        // Get themed views from AppCompat
        View view = mAppCompatDelegate.createView(parent, name, context, attrs);

        if (view == null) {
            try {
                Constructor<? extends View> constructor = null;
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(CONSTRUCTOR_SIGNATURE);
                constructor.setAccessible(true);
                view = constructor.newInstance(context, attrs);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

//        // Set listeners
//        LOGD(TAG, "View: " + view);
        if (mActionHandler != null && view != null) {
            if (actionType != null) {
//                LOGD(TAG, "RESOLVED ATTR actionType: " + actionType);
                mActionViewTags.add(actionType);
                view.setTag(actionType);
                final View finalView = view;
                finalView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mActionHandler.onActionClick(finalView, actionType, v.getTag(R.integer.tag_action_model));
                    }
                });
            }

            if (actionTypeLongClick != null) {
//                LOGD(TAG, "RESOLVED ATTR actionTypeLongClick: " + actionTypeLongClick);
                mActionViewTags.add(actionTypeLongClick);
                view.setTag(actionTypeLongClick);
                final View finalView = view;
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        mActionHandler.onActionClick(finalView, actionTypeLongClick, v.getTag(R.integer.tag_action_model));
                        return true;
                    }
                });
            }
        }

        return view;
    }

    @Override
    public void setActionModel(View itemView, Object actionModel) {
        Iterator<String> iterator = mActionViewTags.iterator();
        while (iterator.hasNext()) {
            View actionView = itemView.findViewWithTag(iterator.next());
            if (actionView != null) {
                actionView.setTag(R.integer.tag_action_model, actionModel);
            }
        }
    }
}