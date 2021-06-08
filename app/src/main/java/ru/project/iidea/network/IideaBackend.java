package ru.project.iidea.network;

import com.google.gson.Gson;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class IideaBackend {

    private final IideaBackendService service;
    private final Retrofit retrofit;
    private final OkHttpClient client;
    private Long userID;
    private String userToken;

    private static IideaBackend instance;

    public static synchronized IideaBackend getInstance() {
        if(instance == null) {
            instance = new IideaBackend();
        }
        return instance;
    }

    private IideaBackend() {
        userID = -1L;
        userToken = "";
        client = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    Request req = response.request();
                    if(req.header("Authorization") == null) {
                        return req.newBuilder()
                                .header("Authorization", Credentials.basic(userID.toString(), userToken))
                                .build();
                    }
                    return req;
                }).build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://uadaf.tech/iidea/").build();
        service = retrofit.create(IideaBackendService.class);
    }

    public IideaBackendService getService() {
        return service;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
