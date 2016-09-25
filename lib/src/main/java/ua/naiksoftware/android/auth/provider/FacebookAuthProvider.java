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

import java.util.Arrays;
import java.util.List;

import ua.naiksoftware.android.auth.AuthException;
import ua.naiksoftware.android.auth.AuthProvider;
import ua.naiksoftware.android.auth.AuthType;

public class FacebookAuthProvider<T> extends AuthProvider<T> {

    private CallbackManager mCallbackManager;
    public static final List<String> SCOPE = Arrays.asList("public_profile", "email");

    public FacebookAuthProvider(Activity activity, Bundle arg) {
        super(activity, arg);
        FacebookSdk.sdkInitialize(getActivity());
        mCallbackManager = CallbackManager.Factory.create();
        final AuthCallback<T> authCallback = getAuthCallback();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final AccessToken accessToken = loginResult.getAccessToken();
                        if (authCallback != null) {
                            authCallback.onSuccess(null, null, accessToken.getToken());
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (authCallback != null) {
                            authCallback.onCancel();
                        }
                    }

                    @Override
                    public void onError(FacebookException e) {
                        if (authCallback != null) {
                            authCallback.onError(new AuthException(e));
                        }
                    }

                });
    }

    @Override
    protected AuthType getType() {
        return AuthType.FACEBOOK;
    }

    @Override
    public void login() {
        LoginManager.getInstance().logInWithReadPermissions(getActivity(), SCOPE);
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void cancel() {
    }
}
