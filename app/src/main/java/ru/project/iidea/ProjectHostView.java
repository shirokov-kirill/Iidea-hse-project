package ru.project.iidea;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class ProjectHostView extends Fragment {

    ProjectHostViewInterface activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_host_view, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ProjectHostViewInterface){
            activity = (ProjectHostViewInterface) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        final Project project = (Project) bundle.get("project");
        ScrollView scrollView = view.findViewById(R.id.project_host_view_scroll_view);
        final LinearLayout projectBlock = new LinearLayout(this.getContext());
        projectBlock.setOrientation(LinearLayout.VERTICAL);
        TextView projectName = new TextView(this.getContext());
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView descriptionLabel = new TextView(this.getContext());
        TextView projectDescription = new TextView(this.getContext());
        TextView projectType = new TextView(this.getContext());
        TextView projectState = new TextView(this.getContext());

        descriptionLabel.setText(R.string.Description);
        descriptionLabel.setTextSize(21);
        descriptionLabel.setTextColor(getResources().getColor(R.color.black, null));
        linearLayout.addView(descriptionLabel);

        projectName.setTextSize(21);
        projectName.setTextColor(getResources().getColor(R.color.black, null));

        projectDescription.setTextSize(21);
        projectDescription.setTextColor(getResources().getColor(R.color.black, null));

        projectType.setTextSize(21);
        projectType.setTextColor(getResources().getColor(R.color.black, null));

        projectState.setTextSize(21);
        projectState.setTextColor(getResources().getColor(R.color.black, null));

        projectName.setText(getString(R.string.projectName, project.getName()));
        projectDescription.setText(project.getDescription());
        linearLayout.addView(projectDescription);
        projectType.setText(getString(R.string.projectType, project.getType().toString()));
        projectState.setText(getString(R.string.projectState, project.getStatus().toString()));

        projectBlock.addView(projectName);
        projectBlock.addView(projectState);
        projectBlock.addView(projectType);
        projectBlock.addView(linearLayout);

        scrollView.addView(projectBlock);

        ImageButton imageButton = view.findViewById(R.id.projectHostViewHeadLineBackButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackButtonPressed();
            }
        });
    }
}
