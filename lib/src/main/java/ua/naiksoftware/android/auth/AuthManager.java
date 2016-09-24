package ua.naiksoftware.android.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.riversoft.eventssion.core.R;
import com.riversoft.eventssion.core.auth.provider.FacebookAuthProvider;
import com.riversoft.eventssion.core.auth.provider.GoogleAuthProvider;
import com.riversoft.eventssion.core.auth.provider.VkAuthProvider;
import com.riversoft.eventssion.core.model.MyUser;
import com.riversoft.eventssion.core.network.util.RetrofitExceptionHelper;
import com.riversoft.eventssion.core.util.LogHelper;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

// TODO: Refactor to using new instance each time we need this class
public class AuthManager {
    private static final String LOG_TAG = LogHelper.makeLogTag(AuthManager.class);

    private static final String STATE_KEY_AUTH_PROVIDER = "STATE_KEY_AUTH_PROVIDER";
    private static final String STATE_KEY_IS_PROCESSED = "STATE_KEY_IS_PROCESSED";
    private static final String STATE_KEY_ARGS = "STATE_KEY_ARGS";

    private Context mContext;
    private AuthProvider mAuthProvider;
    private LoginCallback mLoginCallback;
    private AuthType mAuthType;
    private boolean mIsProcessed = false;
    private GetMyUserRequestProvider mGetMyUserRequestProvider;

    public interface GetMyUserRequestProvider {
        Observable<MyUser> getGetMyUserRequest(String socialAuthToken, String email, AuthType authType);
    }

    public interface LoginCallback {
        void onSuccess(final MyUser user);

        void onFailed(final String message);

        void onCancel();
    }

    private AuthManager() {
    }

    private static class SAMHolder {
        private static final AuthManager INSTANCE = new AuthManager();
    }

    /**
     * Use this to release all static fields
     */
    public static void release() {
        SAMHolder.INSTANCE.mContext = null;
        SAMHolder.INSTANCE.mAuthProvider = null;
        SAMHolder.INSTANCE.mLoginCallback = null;
        SAMHolder.INSTANCE.mAuthType = null;
        SAMHolder.INSTANCE.mIsProcessed = false;
    }


    public static void onSaveInstanceState(Bundle outState) {
        if (null != SAMHolder.INSTANCE.mAuthType) {
            outState.putString(STATE_KEY_AUTH_PROVIDER, SAMHolder.INSTANCE.mAuthType.name());
            outState.putBoolean(STATE_KEY_IS_PROCESSED, SAMHolder.INSTANCE.mIsProcessed);
        }
    }

    public static void onActivityCreate(final Activity loginActivity, Bundle savedInstanceState, LoginCallback loginCallback, GetMyUserRequestProvider myUserRequestProvider) {
        if (savedInstanceState != null) {
            AuthType authType = AuthType.valueOf(savedInstanceState.getString(STATE_KEY_AUTH_PROVIDER, AuthType.NONE.name()));
            boolean isProcessed = savedInstanceState.getBoolean(STATE_KEY_IS_PROCESSED, false);
            if (authType != AuthType.NONE && isProcessed) {
                final Bundle arg = savedInstanceState.getBundle(STATE_KEY_ARGS);
                init(loginActivity, authType, arg, loginCallback, myUserRequestProvider);
                SAMHolder.INSTANCE.mIsProcessed = true;
            }
        }
    }

    public static boolean isProcessed() {
        return SAMHolder.INSTANCE.mIsProcessed;
    }

    public static AuthManager init(final Activity loginActivity, final AuthType authType, Bundle arg, LoginCallback loginCallback, GetMyUserRequestProvider myUserRequestProvider) {
        if (!SAMHolder.INSTANCE.mIsProcessed) {
            switch (authType) {
                case FACEBOOK:
                    SAMHolder.INSTANCE.mAuthProvider = new FacebookAuthProvider(loginActivity, arg);
                    break;
                case GOOGLE:
                    SAMHolder.INSTANCE.mAuthProvider = new GoogleAuthProvider(loginActivity, arg);
                    break;
                case VKONTAKTE:
                    SAMHolder.INSTANCE.mAuthProvider = new VkAuthProvider(loginActivity, arg);
                    break;
                case NATIVE:
                    SAMHolder.INSTANCE.mAuthProvider = new NativeAuthProvider(loginActivity, arg);
                    break;
            }
            SAMHolder.INSTANCE.mAuthProvider.setAuthCallback(sAuthCallback);
            SAMHolder.INSTANCE.mLoginCallback = loginCallback;
            SAMHolder.INSTANCE.mContext = loginActivity.getApplicationContext();
            SAMHolder.INSTANCE.mAuthType = authType;
            SAMHolder.INSTANCE.mGetMyUserRequestProvider = myUserRequestProvider;
        }
        return SAMHolder.INSTANCE;
    }

    public void startLogin() {
        if (SAMHolder.INSTANCE.mAuthProvider != null && !SAMHolder.INSTANCE.mIsProcessed) {
            SAMHolder.INSTANCE.mIsProcessed = true;
            SAMHolder.INSTANCE.mAuthProvider.login();
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SAMHolder.INSTANCE.mAuthProvider != null) {
            SAMHolder.INSTANCE.mAuthProvider.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode != Activity.RESULT_OK) {
            callbackCancel();
        }
    }

    private static AuthProvider.AuthCallback sAuthCallback = new AuthProvider.AuthCallback() {

        @Override
        public void onSuccess(final String socialAuthToken, String email) {
            LogHelper.LOGD(LOG_TAG, "Social Access Token received: " + socialAuthToken);
            callMyUserRequest(socialAuthToken, email);
        }

        @Override
        public void onSuccess(MyUser user) {
            LogHelper.LOGD(LOG_TAG, "MyUser info received: " + user.toString());
            callbackSuccess(user);
        }

        @Override
        public void onCancel() {
            callbackCancel();
        }

        @Override
        public void onError(String message) {
            callbackError(message);
        }
    };

    private static void callbackSuccess(MyUser user) {
        SAMHolder.INSTANCE.mIsProcessed = false;
        if (SAMHolder.INSTANCE.mLoginCallback != null) {
            SAMHolder.INSTANCE.mLoginCallback.onSuccess(user);
        }
    }

    private static void callbackError(String message) {
        SAMHolder.INSTANCE.mIsProcessed = false;
        if (message == null) {
            message = "Login error!";
        }
        LogHelper.LOGD("LoginActivity", "Login onError: " + message);
        if (SAMHolder.INSTANCE.mLoginCallback != null) {
            SAMHolder.INSTANCE.mLoginCallback.onFailed(message);
        }
    }

    private static void callbackCancel() {
        SAMHolder.INSTANCE.mIsProcessed = false;
        LogHelper.LOGD(LOG_TAG, "Login onCancel");
        if (SAMHolder.INSTANCE.mLoginCallback != null) {
            SAMHolder.INSTANCE.mLoginCallback.onCancel();
        }
    }

    private static void callMyUserRequest(final String socialAuthToken, String email) {
        LogHelper.LOGD(LOG_TAG, "Start call to main server...");
        SAMHolder.INSTANCE.mGetMyUserRequestProvider.getGetMyUserRequest(socialAuthToken, email, SAMHolder.INSTANCE.mAuthType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyUser>() {
                    public boolean onNextCalled = false;

                    @Override
                    public void onCompleted() {
                        if (!onNextCalled) callbackCancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        RetrofitExceptionHelper.NetworkException networkException = RetrofitExceptionHelper.parseException(e);
                        String message = "Something went wrong";
                        Context context = SAMHolder.INSTANCE.mContext;
                        if (context != null) {
                            switch (networkException) {
                                case CONNECTION_ERROR:
                                    message = context.getString(R.string.error_no_internet_connection);
                                    break;
                                case SOCKET_TIMEOUT:
                                    message = context.getString(R.string.error_something_went_wrong_check_internet);
                                    break;
                                case NOT_FOUND:
                                case SERVER_ERROR:
                                    message = context.getString(R.string.error_something_went_wrong);
                                    break;
                                case UNKNOWN_HOST_EXCEPTION:
                                    message = context.getString(R.string.error_something_went_wrong_check_internet);
                                    break;
                                case SEE_OTHER:
                                    message = context.getString(R.string.error_email_already_in_use);
                                    break;
                                case FORBIDDEN:
                                case CUSTOM:
                                default:
                                    message = e.getLocalizedMessage();
                                    break;
                            }
                        }
                        callbackError(message);
                    }

                    @Override
                    public void onNext(MyUser myUser) {
                        onNextCalled = true;
                        if (myUser == null || SAMHolder.INSTANCE.mAuthType == null) {
                            callbackError("Something went wrong");
                        } else {
                            LogHelper.LOGD(LOG_TAG, "MyUser info received: " + myUser.toString());
                            callbackSuccess(myUser);
                        }
                    }
                });
    }
}
