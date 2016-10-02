package ua.naiksoftware.android.adapter.delegate.autobind;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Make view id usable in ModelBinder method ({@link ua.naiksoftware.android.adapter.util.SimpleViewHolder#getView(int)})
 *
 * Created by naik on 02.10.16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindingField {

    @IdRes int value();
}
