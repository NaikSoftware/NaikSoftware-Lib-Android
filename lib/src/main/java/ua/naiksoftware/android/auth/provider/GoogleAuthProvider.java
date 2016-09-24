package ua.naiksoftware.android.auth.provider;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.riversoft.core.network.NetworkHelper;
import com.riversoft.eventssion.core.R;
import com.riversoft.eventssion.core.auth.AuthProvider;

import java.io.IOException;

public class GoogleAuthProvider extends AuthProvider {

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
    private static final int REQUEST_CODE_PLAY_SERVICES_NOT_AVAILABLE_ERROR = 1002;
    private static final String SCOPE = "oauth2:" + Scopes.PROFILE + " " + Scopes.EMAIL;//https://www.googleapis.com/auth/userinfo.profile";

    private String mEmail;

    public GoogleAuthProvider(Activity activity, Bundle arg) {
        super(activity, arg);
    }

    @Override
    protected void init(Bundle arg) {

    }

    @Override
    public void login() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mActivity.getApplicationContext());
        if (resultCode == ConnectionResult.SUCCESS) {
            pickUserAccount();
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(mActivity, resultCode,
                    REQUEST_CODE_PLAY_SERVICES_NOT_AVAILABLE_ERROR, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            if (mAuthCallback != null) {
                                mAuthCallback.onCancel();
                            }
                        }
                    });
            dialog.show();
        }else if (mAuthCallback != null){
            mAuthCallback.onCancel();
        }
    }


    private void pickUserAccount() {
        String[] accountTypes = new String[] {"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        mActivity.startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    public void logout() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        switch (requestCode){
            case REQUEST_CODE_PICK_ACCOUNT:
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                // With the account name acquired, go get the auth token
                getUsername();
                break;
            //case REQUEST_CODE_RECOVER_FROM_AUTH_ERROR:
            case REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR:
                // Receiving a result that follows a GoogleAuthException, try auth again
                getUsername();
                break;
            case REQUEST_CODE_PLAY_SERVICES_NOT_AVAILABLE_ERROR:
                login();
                break;
        }
    }

    /**
     * Attempts to retrieve the username.
     * If the account is not yet known, invoke the picker. Once the account is known,
     * start an instance of the AsyncTask to get the auth token and do work with it.
     */
    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (NetworkHelper.isOnline(mActivity.getApplicationContext())) {
                new GetUserNameTask(mActivity, mEmail, SCOPE).execute();
            } else {
                if (mAuthCallback != null) {
                    mAuthCallback.onError(mActivity.getString(R.string.error_no_internet_connection));
                }
            }
        }
    }

    public class GetUserNameTask extends AsyncTask<Void,Void,String> {
        private Activity mActivity;
        private String mScope;
        private String mEmail;

        GetUserNameTask(Activity activity, String name, String scope) {
            this.mActivity = activity;
            this.mScope = scope;
            this.mEmail = name;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String accessToken = fetchToken();
                if (accessToken != null) {
                    // Use the accessToken to access the user's Google data.
                    return accessToken;
                }
            } catch (final IOException e) {
                // The fetchToken() method handles Google-specific exceptions,
                // so this indicates something went wrong at a higher level.
                // TIP: Check for network connectivity before starting the AsyncTask.
//                LogHelper.LOGD("GoogleAuthProvider","GetUserNameTask: " + e.getMessage());
//                Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                //TODO: Change to Loader!?
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAuthCallback != null) {
                            mAuthCallback.onError(e.getMessage());
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String accessToken) {
            if (!TextUtils.isEmpty(accessToken)){
                if (mAuthCallback != null){
                    mAuthCallback.onSuccess(accessToken, mEmail);
                }
            }
        }

        /**
         * Gets an authentication token from Google and handles any
         * GoogleAuthException that may occur.
         */
        protected String fetchToken() throws IOException {
            try {
                return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
            } catch (UserRecoverableAuthException userRecoverableException) {
                // GooglePlayServices.apk is either old, disabled, or not present
                // so we need to show the user some UI in the activity to recover.
                handleException(userRecoverableException);
            } catch (GoogleAuthException fatalException) {
                // Some other type of unrecoverable exception has occurred.
                // Report and log the error as appropriate for your app.
//                LogHelper.LOGD("GoogleAuthProvider","GetUserNameTask: " + fatalException.getMessage());
//                Toast.makeText(mActivity, fatalException.getMessage(), Toast.LENGTH_SHORT).show();
                if (mAuthCallback != null) {
                    mAuthCallback.onError(fatalException.getMessage());
                }
            }
            return null;
        }


        /**
         * This method is a hook for background threads and async tasks that need to
         * provide the user a response UI when an exception occurs.
         */
        public void handleException(final Exception e) {
            // Because this call comes from the AsyncTask, we must ensure that the following
            // code instead executes on the UI thread.
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (e instanceof GooglePlayServicesAvailabilityException) {
                        // The Google Play services APK is old, disabled, or not present.
                        // Show a dialog created by Google Play services that allows
                        // the user to update the APK
                        int statusCode = ((GooglePlayServicesAvailabilityException) e)
                                .getConnectionStatusCode();
                        Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(mActivity,
                                statusCode,
                                REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                        dialog.show();
                    } else if (e instanceof UserRecoverableAuthException) {
                        // Unable to authenticate, such as when the user has not yet granted
                        // the app access to the account, but the user can fix this.
                        // Forward the user to an activity in Google Play services.
                        Intent intent = ((UserRecoverableAuthException) e).getIntent();
                        mActivity.startActivityForResult(intent,
                                REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    }
                }
            });
        }
    }
}
