package ua.naiksoftware.android.auth.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.riversoft.eventssion.core.auth.AuthProvider;

import java.util.Arrays;
import java.util.List;

public class FacebookAuthProvider extends AuthProvider {


    private CallbackManager mCallbackManager;
    public static final List<String> SCOPE = Arrays.asList("public_profile", "email");

    public FacebookAuthProvider(Activity activity, Bundle arg) {
        super(activity, arg);
    }

    @Override
    protected void init(Bundle arg) {
        FacebookSdk.sdkInitialize(mActivity);
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final AccessToken accessToken = loginResult.getAccessToken();
                        if (mAuthCallback != null) {
                            mAuthCallback.onSuccess(accessToken.getToken(), "");
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (mAuthCallback != null) {
                            mAuthCallback.onCancel();
                        }
                    }

                    @Override
                    public void onError(FacebookException e) {
                        if (mAuthCallback != null) {
                            mAuthCallback.onError(e.getMessage());
                        }
                    }

                });
    }

    @Override
    public void login() {
        LoginManager.getInstance().logInWithReadPermissions(mActivity,
                SCOPE);
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
