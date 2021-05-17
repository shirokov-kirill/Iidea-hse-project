package ru.project.iidea;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;
import ru.project.iidea.network.NetworkConnectionChecker;

import static java.lang.Math.min;

public class MyProjectsFragment extends Fragment {

    MyProjectsFragmentInterface activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_projects, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if(context instanceof MyProjectsFragmentInterface){
            activity = (MyProjectsFragmentInterface) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        long userID =  bundle.getLong("userID");
        IideaBackendService server = IideaBackend.getInstance().getService();
        ScrollView projectList = view.findViewById(R.id.myProjects_projects_scroll_view);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
            server.user(userID).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful() && response.body() != null){
                        List<Long> projectIDs = response.body().getProjects();
                        for(long projectID : projectIDs){
                            server.project(projectID).enqueue(new Callback<Project>() {
                                @Override
                                public void onResponse(Call<Project> call, Response<Project> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        final LinearLayout projectBlock = new LinearLayout(getContext());
                                        projectBlock.setOrientation(LinearLayout.VERTICAL);
                                        projectBlock.setClickable(true);
                                        projectBlock.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                activity.onProjectBlockInMyProjectsClicked(response.body());
                                            }
                                        });
                                        TextView projectName = new TextView(getContext());
                                        projectName.setText(response.body().getName());
                                        projectName.setTextSize(24);
                                        TextView projectDescription = new TextView(getContext());
                                        projectDescription.setTextSize(21);
                                        String description = response.body().getDescription();
                                        String inputDescription;
                                        if(description.length() != 0){
                                            inputDescription = description.substring(0, min(description.length(), 40)) + "...";
                                        } else {
                                            inputDescription = "";
                                        }
                                        projectDescription.setText(inputDescription);
                                        projectBlock.addView(projectName);
                                        projectBlock.addView(projectDescription);
                                        linearLayout.addView(projectBlock);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Project> call, Throwable t) {

                                }
                            });
                        }
                        projectList.addView(linearLayout);
                    } else {
                        onFailure(call, new IOException("Error reading from server."));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    activity.showToast("Something is wrong, please try again.");
                }
            });
        } else {
            activity.showToast("No Internet connection.");
        }
    }
}
