package ua.naiksoftware.android.auth.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.riversoft.eventssion.core.auth.AuthProvider;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class VkAuthProvider extends AuthProvider implements VKCallback<VKAccessToken> {


    public VkAuthProvider(Activity activity, Bundle arg) {
        super(activity, arg);
    }

    @Override
    protected void init(Bundle arg) {
    }

    @Override
    public void login() {
        VKSdk.login(mActivity, VKScope.EMAIL);
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
        if (mAuthCallback != null) {
            mAuthCallback.onSuccess(res.accessToken, res.email);
        }
    }

    @Override
    public void onError(VKError error) {
        if (mAuthCallback != null) {
            if (error.errorCode == VKError.VK_CANCELED) {
                mAuthCallback.onCancel();
            } else {
                mAuthCallback.onError(error.errorMessage);
            }
        }
    }
}
