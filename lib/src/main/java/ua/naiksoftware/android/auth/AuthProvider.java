package ua.naiksoftware.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.riversoft.eventssion.core.model.MyUser;


public abstract class AuthProvider {

    protected final Activity mActivity;
    protected AuthCallback mAuthCallback;


     public interface AuthCallback{
        void onSuccess(final String socialAuthToken, String email);
        void onSuccess(final MyUser user);
        void onCancel();
        void onError(final String message);
    }

    public AuthProvider(final Activity activity, Bundle arg) {
        assert (null != activity);
        mActivity = activity;
        init(arg);
    }

    abstract protected void init(Bundle arg);
    abstract public void login();
    abstract public void logout();
    abstract protected void onActivityResult(int requestCode, int resultCode, Intent data);

    AuthProvider setAuthCallback(AuthCallback authCallback) {
        mAuthCallback = authCallback;
        return this;
    }

}
