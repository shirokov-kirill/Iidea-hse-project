package ru.project.iidea.network;

import retrofit2.Retrofit;

public class IideaBackend {

    private static IideaBackendService instance;
    private static Retrofit retrofit;
    public static synchronized IideaBackendService getInstance() {
        if(instance == null) {
            retrofit = new Retrofit.Builder().baseUrl("https://uadaf.tech/iidea").build();
            instance = retrofit.create(IideaBackendService.class);
        }
        return instance;
    }
}
