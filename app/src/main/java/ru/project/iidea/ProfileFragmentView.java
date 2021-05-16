package ru.project.iidea;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;

public class ProfileFragmentView extends Fragment {

    ProfileFragmentViewInterface activity;
    IideaBackendService server;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile_view, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ProfileFragmentViewInterface){
            activity = (ProfileFragmentViewInterface) context;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        long userID = this.getArguments().getLong("userID");
        server = (IideaBackendService) this.getArguments().getSerializable("server");
        server.user(userID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null){
                    User user = response.body();
                    List<Long> projectIDs = user.getProjects();
                    TextView headLineName = view.findViewById(R.id.profileViewHeadLineName);
                    String fullName = user.getSurname() + ' ' + user.getName();
                    headLineName.setText(fullName);
                    TextView dateOfBirth = view.findViewById(R.id.profileViewDateOfBirthHead);
                    dateOfBirth.setText(getString(R.string.Birthday, user.getDateOfBirth()));
                    TextView state = view.findViewById(R.id.profileViewUserStatusHead);
                    state.setText(getString(R.string.Status, user.getState().toString()));
                    TextView description = view.findViewById(R.id.profileViewDescription);
                    description.setText(user.getDescription());
                    LinearLayout profileProjects = view.findViewById(R.id.profileViewProjects);
                    ImageButton backButton = view.findViewById(R.id.profileViewHeadLineBackButton);
                    backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.onBackButtonPressed();
                        }
                    });
                    for(Long projectID : projectIDs){
                            server.project(projectID).enqueue(new Callback<Project>() {
                                @Override
                                public void onResponse(Call<Project> call, Response<Project> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        TextView textView = new TextView(getContext());
                                        textView.setText(response.body().getName());
                                        textView.setTextSize(21);
                                        profileProjects.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Project> call, Throwable t) {
                                    activity.showToast("Incorrect view of Projects. Please reload page.");
                                }
                            });
                    }
                } else {
                    onFailure(call, new IOException("Error uploading user."));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                activity.onBackButtonPressed();
                activity.showToast("Some error happened. Please try again.");
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
