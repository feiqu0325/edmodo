package com.example.feiqu.edmodo.network;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by feiqu on 8/29/16.
 */
public class Network {
    private static final String BASE_URL = "https://api.edmodo.com/";
    private static final String ACCESS_TOKEN = "12e7eaf1625004b7341b6d681fa3a7c1c551b5300cf7f7f3a02010e99c84695d";
    private NetworkApi mNetworkApi;
    public Network() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalUrl = original.url();
                HttpUrl url = originalUrl.newBuilder()
                        .addQueryParameter("access_token", ACCESS_TOKEN)
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();
        mNetworkApi = retrofit.create(NetworkApi.class);
    }

    public NetworkApi getNetworkApi() {
        return mNetworkApi;
    }

    public interface NetworkApi {
        @GET("assignments")
        Call<List<AssignmentModel>> getAssignments();

        @GET("assignment_submissions")
        Call<List<SubmissionModel>> getSubmissions(@Query("assignment_id") int assignmentId, @Query("assignment_creator_id") int assignmentCreatorId);
    }
}
