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

public class ProfileFragmentEditing extends Fragment {

    private LinearLayout profileListOfSubs;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profile_editing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        User myUser = getUser();//TODO
        TextView headLineName = view.findViewById(R.id.profileHeadLineName);
        String fullName = myUser.getSurname() + ' ' + myUser.getName();
        headLineName.setText(fullName);//отсюда
        TextView dateOfBirth = view.findViewById(R.id.profileDateOfBirth);
        dateOfBirth.setText(myUser.getDateOfBirth());
        TextView state = view.findViewById(R.id.profileUserStatus);
        state.setText(myUser.getState().toString());
        TextView email = view.findViewById(R.id.profileEmail);
        email.setText(myUser.getEmail());
        TextView phoneNumber = view.findViewById(R.id.profilePhoneNumber);
        phoneNumber.setText(myUser.getPhoneNumber());
        profileListOfSubs = view.findViewById(R.id.profileListOfSubs);
        List<String> subscribes = getSubscrides(myUser); //TODO
        if(!subscribes.isEmpty()){
            int i = 0;
            for (String str : subscribes) {
                i++;
                TextView textView = new TextView(this.getContext());
                textView.setText(str);
                textView.setTextSize(21);
                textView.setId(i);
                profileListOfSubs.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }
}
