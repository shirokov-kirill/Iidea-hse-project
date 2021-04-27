package ru.project.iidea;

import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class FeedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Bundle bundle = this.getArguments();
//        User myUser = (User) bundle.get("user");
//        List<Project> projects = myUser.getProjects();
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(ProjectType.IT, "First", "Description", 0, ProjectState.New));
        projects.add(new Project(ProjectType.Culture, "Second", "Description2", 0, ProjectState.New));
        ScrollView projectList = view.findViewById(R.id.feed_scroll_view);
        final TableLayout tLayout = new TableLayout(this.getContext());
        for (Project project : projects){
            final TableLayout projectBlock = new TableLayout(this.getContext());
            projectBlock.setStretchAllColumns(true);
            projectBlock.setClickable(true);
            projectBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO
                }
            });
            TextView projectName = new TextView(this.getContext());
            projectName.setText(project.getName());
            projectName.setTextSize(24);

            TextView projectDescription = new TextView(this.getContext());
            projectDescription.setTextSize(21);
            String description = project.getDescription();
            String inputDescription;
            if (description.length() != 0) {
                inputDescription = description.substring(0, min(description.length(), 40)) + "...";
            } else {
                inputDescription = "";
            }
            projectDescription.setText(inputDescription);

            TableRow row1 = new TableRow(this.getContext());
            row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            row1.addView(projectName);


            TextView projectType = new TextView(this.getContext());
            projectType.setText(project.getType().toString());
            projectType.setTextSize(21);

            TextView projectStatus = new TextView(this.getContext());
            projectStatus.setText(project.getStatus().toString());
            projectStatus.setTextSize(21);

            row1.addView(projectType);
            row1.addView(projectStatus);

            TableRow row2 = new TableRow(this.getContext());
            row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            row2.addView(projectDescription);


            projectBlock.addView(row1);
            projectBlock.addView(row2);
            tLayout.addView(projectBlock);
        }
        projectList.addView(tLayout);
    }

}
