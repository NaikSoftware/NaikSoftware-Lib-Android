package ua.naiksoftware.android.network;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import ua.naiksoftware.android.util.ObjectUtils;

import static ua.naiksoftware.android.network.NetworkErrorCode.*;

public class NetworkHelper {

    public static boolean isOnline(final Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static boolean isConnectionException(Throwable e) {
        return e instanceof ConnectException;
    }

    public static NetworkErrorCode parseException(Throwable e) {
        if (e instanceof ConnectException) return CONNECTION_ERROR;
        if (e instanceof SocketTimeoutException) return SOCKET_TIMEOUT;
        if (e instanceof UnknownHostException) return UNKNOWN_HOST_EXCEPTION;
        if (ObjectUtils.Classpath.RETROFIT_RX_ADAPTER && e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case 303: return SEE_OTHER;
                case 401: return UNAUTHORIZED;
                case 403: return FORBIDDEN;
                case 404: return NOT_FOUND;
                case 409: return CONFLICT;
                case 500: return SERVER_ERROR;
                case 502: return BAD_GATEWAY;

            }
        }

        return CUSTOM;
    }
}
