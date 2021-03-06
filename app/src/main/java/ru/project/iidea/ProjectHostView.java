package ru.project.iidea;

import android.content.Context;
import android.graphics.Color;
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
        Project project = (Project) bundle.getSerializable("project");
        ScrollView scrollView = view.findViewById(R.id.project_host_view_scroll_view);
        LinearLayout projectBlock = new LinearLayout(this.getContext());
        projectBlock.setOrientation(LinearLayout.VERTICAL);
        TextView projectName = new TextView(this.getContext());
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView descriptionLabel = new TextView(this.getContext());
        TextView projectDescription = new TextView(this.getContext());
        TextView projectType = new TextView(this.getContext());
        TextView projectState = new TextView(this.getContext());
        float scale = getResources().getDisplayMetrics().density;
        View separator1 = new View(getContext());
        separator1.setBackgroundColor(Color.parseColor("#808080"));
        LinearLayout.LayoutParams separatorLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (scale + 0.5f));
        separatorLayoutParams.setMargins(0, (int) (10 * scale + 0.5f), 0, (int) (10 * scale + 0.5f));
        separator1.setLayoutParams(separatorLayoutParams);
        View separator2 = new View(getContext());
        separator2.setBackgroundColor(Color.parseColor("#808080"));
        separator2.setLayoutParams(separatorLayoutParams);
        View separator3 = new View(getContext());
        separator3.setBackgroundColor(Color.parseColor("#808080"));
        separator3.setLayoutParams(separatorLayoutParams);

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
        projectBlock.addView(separator1);
        projectBlock.addView(projectState);
        projectBlock.addView(separator2);
        projectBlock.addView(projectType);
        projectBlock.addView(separator3);
        projectBlock.addView(linearLayout);

        scrollView.addView(projectBlock);

        ImageButton imageButton = view.findViewById(R.id.projectHostViewHeadLineBackButton);
        imageButton.setOnClickListener(view1 -> activity.onBackButtonPressed());

        Button button = view.findViewById(R.id.projectHostViewDeleteProjectButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onDeleteProjectPressed(project.getId());
            }
        });
    }
}
