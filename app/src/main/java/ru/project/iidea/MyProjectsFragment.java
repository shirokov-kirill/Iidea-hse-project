package ru.project.iidea;

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

import java.util.List;
import static java.lang.Math.min;

public class MyProjectsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_projects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        User myUser = (User) bundle.get("user");
        List<Project> projects = myUser.getProjects();
        ScrollView projectList = view.findViewById(R.id.myProjects_projects_scroll_view);
        for (Project project : projects){
            final LinearLayout projectBlock = new LinearLayout(this.getContext());
            projectBlock.setOrientation(LinearLayout.VERTICAL);
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
            if(description.length() != 0){
                inputDescription = description.substring(0, min(description.length(), 40)) + "...";
            } else {
                inputDescription = "";
            }
            projectDescription.setText(inputDescription);
            projectBlock.addView(projectName);
            projectBlock.addView(projectDescription);
            projectList.addView(projectBlock);
        }
    }
}
