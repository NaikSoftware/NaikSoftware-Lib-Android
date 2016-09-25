package ua.naiksoftware.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.naiksoftware.android.auth.provider.FacebookAuthProvider;
import ua.naiksoftware.android.auth.provider.GoogleAuthProvider;
import ua.naiksoftware.android.auth.provider.VkAuthProvider;
import ua.naiksoftware.android.util.LogHelper;

/**
 * Just call init and callbacks from activity lifecycle and receive user
 *
 * @param <T> user model
 */
public class AuthManager<T> {

    private static final String TAG = LogHelper.makeLogTag(AuthManager.class);

    public static final String ARG_LOGIN = "arg_login";
    public static final String ARG_PASSWORD = "arg_password";

    private static AuthManager INSTANCE;

    private AuthManager() {
    }

    public static AuthManager getInstance() {
        if (INSTANCE == null) INSTANCE = new AuthManager();
        return INSTANCE;
    }

    private AuthProvider<T> mAuthProvider;
    private LoginCallback<T> mLoginCallback;
    private boolean mIsProcessing = false;
    private GetMyUserRequestProvider<T> mGetMyUserRequestProvider;

    public interface GetMyUserRequestProvider<T> {

        Observable<T> getGetMyUserRequest(String username, String password, String socialAuthToken, AuthType authType);
    }

    public interface LoginCallback<T> {

        /**
         * Called when user received from you server
         */
        void onSuccess(T user);

        void onFailed(Throwable message);

        void onCancel();

        /**
         * Called when user fetched from other service (Google, Facebook, Vk, etc)
         */
        void onThirdPartyUserReceived(String email, String username, String accessToken);
    }

    /**
     * Call in onDestroy
     */
    public void onDestroy() {
        if (mAuthProvider != null) mAuthProvider.cancel();
    }

    public boolean isProcessing() {
        return mIsProcessing;
    }

    /**
     * Use this for built-in AuthProvider's
     */
    public void init(final Activity activity, final AuthType authType, Bundle args, final LoginCallback<T> loginCallback, GetMyUserRequestProvider<T> myUserRequestProvider) throws AuthException {
        if (mIsProcessing) return;
        switch (authType) {
            case FACEBOOK:
                mAuthProvider = new FacebookAuthProvider<>(activity, args);
                break;
            case GOOGLE:
                mAuthProvider = new GoogleAuthProvider<>(activity, args);
                break;
            case VKONTAKTE:
                mAuthProvider = new VkAuthProvider<>(activity, args);
                break;
        }
        init(mAuthProvider, args, loginCallback, myUserRequestProvider);
    }

    /**
     * Use for custom AuthProvider's
     *
     * @param authProvider if null use NATIVE provider (just call passed login request)
     */
    public void init(@Nullable AuthProvider<T> authProvider, Bundle args, final LoginCallback<T> loginCallback, GetMyUserRequestProvider<T> myUserRequestProvider) throws AuthException {
        if (mIsProcessing) return;
        mLoginCallback = loginCallback;
        mGetMyUserRequestProvider = myUserRequestProvider;
        mAuthProvider = authProvider;
        if (authProvider == null) {
            mIsProcessing = true;
            String login = args.getString(ARG_LOGIN);
            String pass = args.getString(ARG_PASSWORD);
            if (login == null || pass == null)
                throw new AuthException("Login or password not found in bundle");
            callMyUserRequest(login, pass, null, AuthType.NATIVE);
        } else {
            final AuthType authType = authProvider.getType();
            authProvider.setAuthCallback(new AuthProvider.AuthCallback<T>() {

                @Override
                public void onSuccess(String email, String user, String accessToken) {
                    LogHelper.LOGD(TAG, "Social Access Token received: " + accessToken);
                    loginCallback.onThirdPartyUserReceived(email, user, accessToken);
                    callMyUserRequest(email, null, accessToken, authType);
                }

                @Override
                public void onCancel() {
                    callbackCancel();
                }

                @Override
                public void onError(Throwable message) {
                    callbackError(message);
                }
            });
        }
    }

    public void startLogin() {
        if (mAuthProvider != null && !mIsProcessing) {
            mIsProcessing = true;
            mAuthProvider.login();
        }
    }

    /**
     * Call in onActivityResult
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mAuthProvider != null) {
            mAuthProvider.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode != Activity.RESULT_OK) {
            callbackCancel();
        }
    }

    private void callbackSuccess(T user) {
        mIsProcessing = false;
        if (mLoginCallback != null) {
            mLoginCallback.onSuccess(user);
        }
    }

    private void callbackError(Throwable error) {
        mIsProcessing = false;
        LogHelper.LOGD("LoginActivity", "Login onError: " + error.getMessage());
        if (mLoginCallback != null) {
            mLoginCallback.onFailed(error);
        }
    }

    private void callbackCancel() {
        mIsProcessing = false;
        LogHelper.LOGD(TAG, "Login onCancel");
        if (mLoginCallback != null) {
            mLoginCallback.onCancel();
        }
    }

    private void callMyUserRequest(final String login, final String password, final String socialAuthToken, final AuthType authType) {
        LogHelper.LOGD(TAG, "Start call to server...");
        mGetMyUserRequestProvider.getGetMyUserRequest(login, password, socialAuthToken, authType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {

                    private boolean onNextCalled = false;

                    @Override
                    public void onCompleted() {
                        if (!onNextCalled) callbackCancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callbackError(e);
                    }

                    @Override
                    public void onNext(T myUser) {
                        onNextCalled = true;
                        if (myUser == null) {
                            callbackError(new AuthException("Something went wrong, user from server is null"));
                        } else {
                            LogHelper.LOGD(TAG, "MyUser info received: " + myUser.toString());
                            callbackSuccess(myUser);
                        }
                    }
                });
    }
}
