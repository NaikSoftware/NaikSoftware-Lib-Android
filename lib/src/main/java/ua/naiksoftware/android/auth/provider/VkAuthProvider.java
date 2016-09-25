package ua.naiksoftware.android.auth.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import ua.naiksoftware.android.auth.AuthException;
import ua.naiksoftware.android.auth.AuthProvider;
import ua.naiksoftware.android.auth.AuthType;

public class VkAuthProvider<T> extends AuthProvider<T> implements VKCallback<VKAccessToken> {

    public VkAuthProvider(Activity activity, Bundle arg) {
        super(activity, arg);
    }

    @Override
    protected AuthType getType() {
        return AuthType.VKONTAKTE;
    }

    @Override
    public void login() {
        VKSdk.login(getActivity(), VKScope.EMAIL);
    }

    @Override
    public void logout() {
        VKSdk.logout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKSdk.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onResult(VKAccessToken res) {
        if (getAuthCallback() != null) {
            getAuthCallback().onSuccess(res.email, res.userId, res.accessToken);
        }
    }

    @Override
    public void onError(VKError error) {
        AuthCallback<T> authCallback = getAuthCallback();
        if (authCallback != null) {
            if (error.errorCode == VKError.VK_CANCELED) {
                authCallback.onCancel();
            } else {
                authCallback.onError(new AuthException(error.httpError));
            }
        }
    }

    @Override
    protected void cancel() {
    }
}
