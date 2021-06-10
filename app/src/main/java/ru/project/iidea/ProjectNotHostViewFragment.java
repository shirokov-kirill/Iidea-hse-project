package ru.project.iidea;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;
import ru.project.iidea.network.NetworkConnectionChecker;

public class ProjectNotHostViewFragment extends Fragment {

    ProjectNotHostViewFragmentInterface activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_not_host_view, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ProjectNotHostViewFragmentInterface){
            activity = (ProjectNotHostViewFragmentInterface) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Project project = (Project) getArguments().getSerializable("project");
        if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
            IideaBackendService server = IideaBackend.getInstance().getService();
            server.user(project.getHostId()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful() && response.body() != null){
                        TextView textView = view.findViewById(R.id.projectNotHostView_head_text);
                        textView.setText(project.getName());
                        TextView textView1 = view.findViewById(R.id.projectNotHostViewDescription);
                        textView1.setText(project.getDescription());
                        TextView textView2 = view.findViewById(R.id.projectNotHostViewStatus);
                        textView2.setText(getString(R.string.Status, project.getStatus()));
                        TextView textView3 = view.findViewById(R.id.projectNotHostViewCategory);
                        textView3.setText(getString(R.string.projectType, project.getType()));
                        TextView textView4 = view.findViewById(R.id.projectNotHostViewToHost);
                        textView4.setText(getString(R.string.HostRef,response.body().getSurname() + " " + response.body().getName()));
                        textView4.setClickable(true);
                        textView4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.onUserIdClicked(response.body());
                            }
                        });
                        Button button = view.findViewById(R.id.button3);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.onRespondButtonClicked(project.getId());
                            }
                        });
                        ImageButton imageButton = view.findViewById(R.id.projectNotHostViewHeadLineBackButton);
                        imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.onBackButtonPressed();
                            }
                        });
                    } else {
                        onFailure(call, new IOException());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    activity.showToast("Something went wrong.");
                    activity.onBackButtonPressed();
                }
            });
        } else {
            activity.showToast("No Internet connection.");
            activity.onBackButtonPressed();
        }
    }
}
