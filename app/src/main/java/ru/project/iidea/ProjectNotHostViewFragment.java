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
        TextView textView = view.findViewById(R.id.projectNotHostView_head_text);
        textView.setText(project.getName());
        TextView textView1 = view.findViewById(R.id.projectNotHostViewDescription);
        textView1.setText(project.getDescription());
        TextView textView2 = view.findViewById(R.id.projectNotHostViewStatus);
        textView2.setText(getString(R.string.Status, project.getStatus()));
        TextView textView3 = view.findViewById(R.id.projectNotHostViewCategory);
        textView3.setText(getString(R.string.projectType, project.getType()));
        TextView textView4 = view.findViewById(R.id.projectNotHostViewToHost);
        textView4.setText(getString(R.string.HostRef,String.valueOf(project.getHostId())));
        textView4.setClickable(true);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onUserIdClicked(project.getHostId());
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
    }
}
