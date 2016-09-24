package ua.naiksoftware.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.riversoft.eventssion.core.model.MyUser;
import com.riversoft.eventssion.core.network.CoreRestClient;
import com.riversoft.eventssion.core.util.LogHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NativeAuthProvider extends AuthProvider {

    public static final String ARG_LOGIN = "ARG_LOGIN";
    public static final String ARG_PASSWORD = "ARG_PASSWORD";
    private static final String LOG_TAG = LogHelper.makeLogTag(NativeAuthProvider.class);
    private String mEmail;
    private String mPassword;

    public NativeAuthProvider(Activity activity, Bundle arg) {
        super(activity, arg);
    }

    @Override
    protected void init(Bundle arg) {
        if (arg == null || !arg.containsKey(ARG_LOGIN) || !arg.containsKey(ARG_PASSWORD)) return;
        mEmail = arg.getString(ARG_LOGIN);
        mPassword = arg.getString(ARG_PASSWORD);
    }

    @Override
    public void login() {
        makeLoginRequest(mEmail, mPassword);
    }

    @Override
    public void logout() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    private void sendSuccess(MyUser user) {
        if (mAuthCallback != null) {
            mAuthCallback.onSuccess(user);
        }
    }

    private void sendError(String error) {
        if (mAuthCallback != null) {
            mAuthCallback.onError(error);
        }
    }


    private void makeLoginRequest(String userName, String pass) {
        LogHelper.LOGD(LOG_TAG, "Login request");
        CoreRestClient.getInstance().getMyUserRepository().login(userName, pass)
                .enqueue(new Callback<MyUser>() {
            @Override
            public void onResponse(Call<MyUser> call, Response<MyUser> response) {
                if (response.isSuccessful()) {
                    sendSuccess(response.body());
                } else {
                    sendError(response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<MyUser> call, Throwable t) {
                sendError(t.getMessage());
            }
        });
    }
}
