package ru.project.iidea;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

public class ProjectHostEdit extends Fragment {

    ProjectHostEditInterface activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_host_edit, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ProjectHostEditInterface){
            activity = (ProjectHostEditInterface) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        final Project project = (Project) bundle.get("project");
        ScrollView scrollView = view.findViewById(R.id.project_host_view_scroll_view);
        final LinearLayout projectBlock = new LinearLayout(this.getContext());
        projectBlock.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        projectBlock.setOrientation(LinearLayout.VERTICAL);
        TextView projectName = new TextView(this.getContext());
        EditText projectDescription = new EditText(this.getContext());
        Button projectType = new Button(this.getContext());
        Button projectState = new Button(this.getContext());

        projectName.setTextSize(30);
        projectName.setTypeface(null, Typeface.BOLD);
        projectName.setTextColor(getResources().getColor(R.color.black, null));

        projectDescription.setTextSize(21);
        projectDescription.setTextColor(getResources().getColor(R.color.black, null));

        projectType.setTextSize(21);
        projectType.setTextColor(getResources().getColor(R.color.black, null));
        projectType.setOnClickListener(view1 -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(), projectType);
            popupMenu.getMenuInflater().inflate(R.menu.menu_category, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.category_it:
                        projectType.setText(R.string.category_it);
                        return true;
                    case R.id.category_culture:
                        projectType.setText(R.string.category_culture);
                        return true;
                    case R.id.category_business:
                        projectType.setText(R.string.category_business);
                        return true;
                    case R.id.category_history:
                        projectType.setText(R.string.category_history);
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });

        projectState.setTextSize(21);
        projectState.setTextColor(getResources().getColor(R.color.black, null));
        projectState.setOnClickListener(view2 -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(), projectState);
            popupMenu.getMenuInflater().inflate(R.menu.menu_project_status, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.project_status_new:
                        projectState.setText(R.string.project_status_new);
                        return true;
                    case R.id.project_status_search:
                        projectState.setText(R.string.project_status_search);
                        return true;
                    case R.id.project_status_in_progress:
                        projectState.setText(R.string.project_status_in_progress);
                        return true;
                    case R.id.project_status_finished:
                        projectState.setText(R.string.project_status_finished);
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        });

        projectName.setText(project.getName());
        projectDescription.setText(project.getDescription());
        projectType.setText(project.getType().toString());
        projectState.setText(project.getStatus().toString());

        projectBlock.addView(projectName);
        projectBlock.addView(projectState);
        projectBlock.addView(projectType);
        projectBlock.addView(projectDescription);

        scrollView.addView(projectBlock);

        ImageButton imageButton = view.findViewById(R.id.projectHostEditHeadLineBackButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackButtonPressed();
            }
        });
    }
}
