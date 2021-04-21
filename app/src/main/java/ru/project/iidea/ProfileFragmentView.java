package ru.project.iidea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragmentView extends Fragment {

    private LinearLayout profileProjects;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile_editing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        User myUser = new User(1, "Shirokov", "Kirill", "", "25", "k.s.shirokov", "+7921...", "I'm good", UserState.SEEKING, new ArrayList<ProjectType>(), new ArrayList<Project>());//getUser(host_id);
        TextView headLineName = view.findViewById(R.id.profileViewHeadLineName);
        String fullName = myUser.getSurname() + ' ' + myUser.getName();
        headLineName.setText(fullName);
        TextView dateOfBirth = view.findViewById(R.id.profileViewDateOfBirth);
        dateOfBirth.setText(myUser.getDateOfBirth());
        TextView state = view.findViewById(R.id.profileViewUserStatus);
        state.setText(myUser.getState().toString());
        TextView email = view.findViewById(R.id.profileViewEmail);
        email.setText(myUser.getEmail());
        profileProjects = view.findViewById(R.id.profileViewProjects);
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
