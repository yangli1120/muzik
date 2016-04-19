package crazysheep.io.materialmusic.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * network layer, http request api
 *
 * Created by crazysheep on 15/12/17.
 */
public class NetClient {

    private static Retrofit mRetrofit;

    private NetClient() {
    }

    public static Retrofit retrofit() {
        if(mRetrofit == null)
            synchronized (NetClient.class) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .build();

                                return chain.proceed(request);
                            }
                        })
                        .build();

                mRetrofit = new Retrofit.Builder()
                        .baseUrl(NetConstants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
            }

        return mRetrofit;
    }

}
