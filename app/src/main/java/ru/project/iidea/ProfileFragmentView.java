package ru.project.iidea;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;

public class ProfileFragmentView extends Fragment {

    ProfileFragmentViewInterface activity;

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
        IideaBackendService server = IideaBackend.getInstance().getService();
        server.user(userID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful() && response.body() != null){
                    User user = response.body();
                    List<Long> projectIDs = user.getProjects();
                    TextView headLineName = view.findViewById(R.id.profileViewHeadLineName);
                    String fullName = user.getSurname() + ' ' + user.getName();
                    headLineName.setText(fullName);
                    headLineName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    TextView dateOfBirth = view.findViewById(R.id.profileViewDateOfBirthHead);
                    dateOfBirth.setText(getString(R.string.Birthday, user.getDateOfBirth()));
                    dateOfBirth.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    TextView messaging = view.findViewById(R.id.writeMessageTextButton);
                    messaging.setText(Html.fromHtml("<a href=\"mailto:" + user.getEmail() + "\">Отправить e-mail</a>", 0));
                    messaging.setMovementMethod(LinkMovementMethod.getInstance());
                    TextView state = view.findViewById(R.id.profileViewUserStatusHead);
                    state.setText(getString(R.string.Status, user.getState().toString()));
                    state.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    TextView description = view.findViewById(R.id.profileViewDescription);
                    description.setText(user.getDescription());
                    description.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
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
                                public void onResponse(@NonNull Call<Project> call, @NonNull Response<Project> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        TextView textView = new TextView(getContext());
                                        textView.setText(response.body().getName());
                                        textView.setTextSize(21);
                                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                        profileProjects.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<Project> call, @NonNull Throwable t) {
                                    activity.showToast("Incorrect view of Projects. Please reload page.");
                                }
                            });
                    }
                } else {
                    onFailure(call, new IOException("Error uploading user."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                activity.onBackButtonPressed();
                activity.showToast("Some error happened. Please try again.");
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
