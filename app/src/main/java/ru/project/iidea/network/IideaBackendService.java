package ru.project.iidea.network;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.http.*;
import ru.project.iidea.Project;
import ru.project.iidea.User;

import java.io.Serializable;
import java.util.List;

public interface IideaBackendService extends Serializable {

    @POST("user/auth")
    @Headers("Content-Type: text/plain")
    Call<Long> auth(@Body String token);

    @POST("user")
    Call<Void> updateUser(@Body UserUpdateRequest req);

    default Call<Void> updateUser(String description, String status, String phone) {
        return updateUser(new UserUpdateRequest(description, status, phone));
    }

    @GET("user/{id}")
    Call<User> user(@Path("id") long userId);

    @GET("user/{id}/feed")
    Call<List<Long>> feed(@Path("id") long userId);

    @PUT("user/subscription/{tag}")
    Call<Void> subscribe(@Path("tag") String tag);

    @DELETE("user/subscription/{tag}")
    Call<Void> unsubscribe(@Path("tag") String tag);

    @GET("user/{id}/responses")
    Call<Integer> responsesFrom(@Path("id") long userId);

    @GET("project/{id}")
    Call<Project> project(@Path("id") long projectId);

    @POST("project/{id}")
    Call<Void> updateProject(@Path("id") long id, @Body ProjectUpdateRequest req);

    default Call<Void> updateProject(long id, @Nullable String name, @Nullable String type,
                                        @Nullable String description, @Nullable String status) {
        return updateProject(id, new ProjectUpdateRequest(name, type, description, status));
    }

    @POST("project/search")
    Call<List<Long>> searchProjects(@Body ProjectSearchRequest req);

    @PUT("project")
    Call<Long> createProject(@Body ProjectUpdateRequest req);

    default Call<Long> createProject(@Nullable String name, @Nullable String type,
                                        @Nullable String description, @Nullable String status) {
        return createProject(new ProjectUpdateRequest(name, type, description, status));
    }

    @DELETE("project/{id}")
    Call<Void> deleteProject(@Path("id") long projectId);

    @GET("project/{id}/responses")
    Call<List<Long>> responsesTo(@Path("id") long projectId);

    @PUT("project/{id}/responses")
    Call<Void> respondTo(@Path("id") long projectId);

    @GET("response/{id}")
    Call<BackendResponse> response(@Path("id") long responseId);

    @DELETE("response/{id}")
    Call<Void> deleteResponse(@Path("id") long responseId);

    //TODO Call<Void> modifyUserDescription(userId, String)
}
