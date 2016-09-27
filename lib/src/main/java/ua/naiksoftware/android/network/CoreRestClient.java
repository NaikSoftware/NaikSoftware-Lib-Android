package ua.naiksoftware.android.network;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.naiksoftware.android.BuildConfig;

import static retrofit2.Retrofit.Builder;

/**
 * Created by naik on 3/14/16.
 */
public class CoreRestClient {

    private static Boolean SHOW_LOG = BuildConfig.DEBUG;
    private volatile static CoreRestClient sInstance;
    private static final Object sLock = new Object();

    private Retrofit mRetrofit;
    private Gson mGson;

    public static void enableLog(Boolean showLog) {
        SHOW_LOG = showLog;
    }

    protected CoreRestClient() {
        final OkHttpClient okClient = createOkClient();
        mRetrofit = buildRetrofit(okClient);
    }

    public static CoreRestClient getInstance() {
        CoreRestClient instance = sInstance;
        if (instance == null) {
            synchronized (sLock) {
                instance = sInstance;
                if (instance == null) {
                    sInstance = instance = new CoreRestClient();
                }
            }
        }
        return instance;
    }

    protected String getServerDateFormat() {
        return null;
    }

    protected FieldNamingPolicy getFieldNamingPolicy() {
        return null;
    }

//    public MyUserRepository getMyUserRepository() {
//        if(mMyUserRepository == null) {
//            mMyUserRepository = mRetrofit.create(MyUserRepository.class);
//        }
//        return mMyUserRepository;
//    }

    protected Retrofit buildRetrofit(OkHttpClient okHttpClient) {
        Gson gson = getGson();

        Builder builder = new Builder()
                .addConverterFactory(GsonConverterFactory.create(gson)) // refactor hardcoded Gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // check classpath
                .client(okHttpClient);
        onBuildRetrofit(builder);
        return builder.build();
    }

    protected void onBuildRetrofit(Builder builder) {}

    protected Retrofit getRetrofit() {
        return mRetrofit;
    }

    @NonNull
    public Gson getGson() {
        if (mGson == null) {
            GsonBuilder builder = new GsonBuilder();

            String serverDateFormat = getServerDateFormat();
            if (serverDateFormat != null) builder.setDateFormat(serverDateFormat);

            FieldNamingPolicy fieldNamingPolicy = getFieldNamingPolicy();
            if (fieldNamingPolicy != null) builder.setFieldNamingPolicy(fieldNamingPolicy);

            onBuildGson(builder);

            mGson = builder.create();
        }
        return mGson;
    }

    protected void onBuildGson(GsonBuilder builder) {
    }

    public OkHttpClient createOkClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Customize the request
                        Request.Builder builder = original.newBuilder()
                                .header("Accept", "application/json")
                                .method(original.method(), original.body());
//                        if (chain.request().header("X-Auth") == null) {
//                            String token = UserStorage.getToken();
//                            if (!TextUtils.isEmpty(token)) {
//                                builder.header("X-Authentication", token);
//                            }
//                        }

                        final okhttp3.Response response = chain.proceed(builder.build());

//                        if (response.code() == 401 && chain.request().url().url().toString().startsWith(apiUrl)) { //Unauthorized
//                            LogHelper.LOGD(response.request().url().toString());
//                            MyApplication.getInstance().restartApplication();
//                        }
                        return response;
                    }
                });

        if (SHOW_LOG /* check classpath*/) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }
}
