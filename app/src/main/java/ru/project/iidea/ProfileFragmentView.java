package ru.project.iidea;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragmentView extends Fragment {

    ProfileFragmentViewInterface activity;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile_editing, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ProfileFragmentViewInterface){
            activity = (ProfileFragmentViewInterface) context;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        User myUser = (User) this.getArguments().get("user");
        TextView headLineName = view.findViewById(R.id.profileViewHeadLineName);
        String fullName = myUser.getSurname() + ' ' + myUser.getName();
        headLineName.setText(fullName);
        TextView dateOfBirth = view.findViewById(R.id.profileViewDateOfBirthHead);
        dateOfBirth.setText(getString(R.string.Birthday, myUser.getDateOfBirth()));
        TextView state = view.findViewById(R.id.profileViewUserStatusHead);
        state.setText(getString(R.string.Status, myUser.getState().toString()));
        ImageButton backButton = view.findViewById(R.id.profileViewHeadLineBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackButtonPressed();
            }
        });
        TextView description = view.findViewById(R.id.profileViewDescription);
        description.setText(myUser.getDescription());
        LinearLayout profileProjects = view.findViewById(R.id.profileViewProjects);
        List<Project> projects = myUser.getProjects();
        if(!projects.isEmpty()){
            int i = 0;
            for (Project project : projects) {
                i++;
                TextView textView = new TextView(this.getContext());
                textView.setText(project.getName());
                textView.setTextSize(21);
                textView.setId(i);
                profileProjects.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }
}
