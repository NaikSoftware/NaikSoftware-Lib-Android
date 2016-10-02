package ua.naiksoftware.android.adapter.delegate.autobind;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bind to EditText (setText("field value"))
 *
 * Created by naik on 02.10.16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Text {

    @IdRes int value();
}
