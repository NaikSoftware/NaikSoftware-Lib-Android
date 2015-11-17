package ua.naiksoftware.advancedviews;

import java.util.List;

/**
 * Created by naik on 15.11.15.
 */
public interface LoadingCallback<T> {

    void success(List<T> items, int totalOnServer);
    void fail(Throwable t);
}
