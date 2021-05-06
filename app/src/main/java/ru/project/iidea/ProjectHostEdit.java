package ru.project.iidea;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProjectHostEdit extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_host_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        final Project project = (Project) bundle.get("project");
        ScrollView scrollView = view.findViewById(R.id.project_host_view_scroll_view);
        final LinearLayout projectBlock = new LinearLayout(this.getContext());
        projectBlock.setOrientation(LinearLayout.VERTICAL);
        EditText projectName = new EditText(this.getContext());
        EditText projectDescription = new EditText(this.getContext());
        EditText projectType = new EditText(this.getContext());
        EditText projectState = new EditText(this.getContext());

        projectName.setTextSize(35);
        projectName.setTypeface(null, Typeface.BOLD);

        projectDescription.setTextSize(25);

        projectType.setTextSize(32);
        projectType.setTypeface(null, Typeface.BOLD);

        projectState.setTextSize(30);
        projectState.setTypeface(null, Typeface.BOLD_ITALIC);

        projectName.setText(project.getName());
        projectDescription.setText(project.getDescription());
        projectType.setText(project.getType().toString());
        projectState.setText(project.getStatus().toString());

        projectBlock.addView(projectName);
        projectBlock.addView(projectState);
        projectBlock.addView(projectType);
        projectBlock.addView(projectDescription);

        scrollView.addView(projectBlock);
    }
}
