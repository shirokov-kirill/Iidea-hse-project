package ru.project.iidea;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;

import static java.lang.Math.min;

public class FeedFragment extends Fragment {

    FeedFragmentInterface activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FeedFragmentInterface){
            activity = (FeedFragmentInterface) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        long myUserID = bundle.getLong("userID");
        IideaBackendService server = IideaBackend.getInstance().getService();
        ScrollView projectList = view.findViewById(R.id.feed_scroll_view);
        server.feed(myUserID)
                .enqueue(new Callback<List<Long>>() {
            @Override
            public void onResponse(@NonNull Call<List<Long>> call, @NonNull Response<List<Long>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Long> projectIDs = response.body();
                    final TableLayout tLayout = new TableLayout(getContext());
                    for(Long projectID : projectIDs){
                        server.project(projectID).enqueue(new Callback<Project>() {
                            @Override
                            public void onResponse(@NonNull Call<Project> call, @NonNull Response<Project> response) {
                                if(response.isSuccessful() && response.body() != null){
                                    Project project = response.body();
                                    final TableLayout projectBlock = new TableLayout(getContext());
                                    projectBlock.setStretchAllColumns(true);
                                    projectBlock.setClickable(true);
                                    projectBlock.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            activity.onProjectBlockClicked(project);
                                        }
                                    });
                                    TextView projectName = new TextView(getContext());
                                    projectName.setText(project.getName());
                                    projectName.setTextSize(24);

                                    TextView projectDescription = new TextView(getContext());
                                    projectDescription.setTextSize(21);
                                    String description = project.getDescription();
                                    String inputDescription;
                                    if (description.length() != 0) {
                                        inputDescription = description.substring(0, min(description.length(), 40)) + "...";
                                    } else {
                                        inputDescription = "";
                                    }
                                    projectDescription.setText(inputDescription);

                                    TableRow row1 = new TableRow(getContext());
                                    row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    row1.addView(projectName);


                                    TextView projectType = new TextView(getContext());
                                    projectType.setText(project.getType().toString());
                                    projectType.setTextSize(21);

                                    TextView projectStatus = new TextView(getContext());
                                    projectStatus.setText(project.getStatus().toString());
                                    projectStatus.setTextSize(21);

                                    row1.addView(projectType);
                                    row1.addView(projectStatus);

                                    TableRow row2 = new TableRow(getContext());
                                    row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    row2.addView(projectDescription);


                                    projectBlock.addView(row1);
                                    projectBlock.addView(row2);
                                    tLayout.addView(projectBlock);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Project> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                    projectList.addView(tLayout);
                } else {
                    onFailure(call, new IOException("Error receiving data."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Long>> call, @NonNull Throwable t) {
                activity.showToast("Something went wrong while download. Please check your internet connection.");
            }
        });
    }

}
