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
import androidx.fragment.app.Fragment;

public class ResponseViewFragment extends Fragment {

    ResponseViewFragmentInterface activity;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_response_view, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ResponseViewFragmentInterface){
            activity = (ResponseViewFragmentInterface) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Response response = (Response) this.getArguments().get("response");
        String projectName = this.getArguments().getString("projectName");
        TextView textView = view.findViewById(R.id.responseViewProjectName);
        TextView textView1 = view.findViewById(R.id.responseViewToUserPage);
        textView.setText(projectName);
        textView1.setText(String.valueOf(response.getFromUserId()));
        textView1.setClickable(true);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onUserIdClicked(response.getFromUserId());
            }
        });
        ImageButton imageButton = view.findViewById(R.id.ResponseViewHeadLineBackButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackButtonPressed();
            }
        });
        Button button = view.findViewById(R.id.rejectResponseButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
    }
}
