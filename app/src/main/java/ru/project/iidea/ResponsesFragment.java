package ru.project.iidea;

import android.content.Context;
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

public class ResponsesFragment extends Fragment {

    ResponsesFragmentInterface activity;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_responses, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ResponsesFragmentInterface){
            activity = (ResponsesFragmentInterface) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayout profileListOfSubs = view.findViewById(R.id.responsesOnMyProjects);
        List<Response> responses = (ArrayList<Response>) this.getArguments().get("responses");
        if(!responses.isEmpty()){
            for (Response response : responses) {
                TextView textView = new TextView(this.getContext());
                textView.setText(response.toString());
                textView.setTextSize(21);
                textView.setClickable(true);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.openCurrentResponse(response);
                    }
                });
                profileListOfSubs.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } else {
            TextView textView = new TextView(this.getContext());
            textView.setText(R.string.NoResponses);
            textView.setTextSize(30);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            profileListOfSubs.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
