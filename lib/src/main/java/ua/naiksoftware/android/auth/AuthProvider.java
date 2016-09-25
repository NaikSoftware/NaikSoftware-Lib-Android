package ua.naiksoftware.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public abstract class AuthProvider<T> {

    private final Activity mActivity;
    private AuthCallback<T> mAuthCallback;

     public interface AuthCallback<T> {
        void onSuccess(String email, String user, String socialAuthToken);
        void onCancel();
        void onError(final Throwable message);
    }

    public AuthProvider(final Activity activity, Bundle arg) {
        if (activity == null) throw new IllegalArgumentException("Activity might be non null!");
        mActivity = activity;
    }

    abstract public void login();
    abstract public void logout();
    abstract protected void onActivityResult(int requestCode, int resultCode, Intent data);
    abstract protected void cancel();
    abstract protected AuthType getType();

    AuthProvider setAuthCallback(AuthCallback<T> authCallback) {
        mAuthCallback = authCallback;
        return this;
    }

    public AuthCallback<T> getAuthCallback() {
        return mAuthCallback;
    }

    public Activity getActivity() {
        return mActivity;
    }
}
