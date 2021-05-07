package ru.project.iidea.network;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.http.*;
import ru.project.iidea.Project;
import ru.project.iidea.User;

import java.util.List;

public interface IideaBackendService {

    @GET("user/{id}")
    Call<User> user(@Path("id") int userId);

    @GET("user/{id}/feed")
    Call<List<Integer>> feed(@Path("id") int userId);

    @PUT("user/subscription/{tag}")
    Call<Void> subscribe(String tag);

    @DELETE("user/subscription/{tag}")
    Call<Void> unsubscribe(String tag);

    @GET("user/{id}/responses")
    Call<Integer> responsesFrom(@Path("id") int userId);

    @GET("project/{id}")
    Call<Project> project(@Path("id") int projectId);

    @POST("project/{id}")
    Call<Void> updateProject(@Path("id") int id, @Body ProjectUpdateRequest req);

    default Call<Void> updateProject(int id, @Nullable String name, @Nullable String type,
                                        @Nullable String description, @Nullable String status) {
        return updateProject(id, new ProjectUpdateRequest(name, type, description, status));
    }

    @PUT("project")
    Call<Integer> createProject(@Body ProjectUpdateRequest req);

    default Call<Integer> createProject(@Nullable String name, @Nullable String type,
                                        @Nullable String description, @Nullable String status) {
        return createProject(new ProjectUpdateRequest(name, type, description, status));
    }

    @DELETE("project/{id}")
    Call<Void> deleteProject(@Path("id") int projectId);

    @GET("project/{id}/responses")
    Call<List<Integer>> responsesTo(@Path("id") int projectId);

    @PUT("project/{id}/responses")
    Call<Void> respondTo(@Path("id") int projectId);

    @GET("response/{id}")
    Call<BackendResponse> response(@Path("id") int responseId);

    @DELETE("response/{id}")
    Call<Void> deleteResponse(@Path("id") int responseId);
}
