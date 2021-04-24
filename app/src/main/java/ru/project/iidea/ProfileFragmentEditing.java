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

import java.util.List;

public class ProfileFragmentEditing extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile_editing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        User myUser = (User) this.getArguments().get("user");
        TextView headLineName = view.findViewById(R.id.profileHeadLineName);
        String fullName = myUser.getSurname() + ' ' + myUser.getName();
        headLineName.setText(fullName);//отсюда
        TextView dateOfBirth = view.findViewById(R.id.profileDateOfBirth);
        dateOfBirth.setText(getString(R.string.Birthday, myUser.getDateOfBirth()));
        TextView state = view.findViewById(R.id.profileUserStatus);
        state.setText(getString(R.string.Status, myUser.getState().toString()));
        TextView email = view.findViewById(R.id.profileEmail);
        email.setText(getString(R.string.Email, myUser.getEmail()));
        TextView phoneNumber = view.findViewById(R.id.profilePhoneNumber);
        phoneNumber.setText(getString(R.string.PhoneNumber, myUser.getPhoneNumber()));
        LinearLayout profileListOfSubs = view.findViewById(R.id.profileListOfSubs);
        List<ProjectType> subscribes = myUser.getSubscriptions();
        if(!subscribes.isEmpty()){
            int i = 0;
            for (ProjectType projectType : subscribes) {
                i++;
                TextView textView = new TextView(this.getContext());
                textView.setText(projectType.toString());
                textView.setTextSize(21);
                textView.setId(i);
                profileListOfSubs.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        LinearLayout profileProjects = view.findViewById(R.id.profileMyProjects);
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
