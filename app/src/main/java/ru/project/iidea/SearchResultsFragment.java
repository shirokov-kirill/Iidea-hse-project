package ru.project.iidea;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchResultsFragment extends Fragment {
    SearchFragmentInterface activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SearchFragmentInterface){
            activity = (SearchFragmentInterface) context;
        }

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<ProjectType> list = (List<ProjectType>) getArguments().getSerializable("projectTypes");
        LinearLayout layout = view.findViewById(R.id.layoutForSearchResults);
        for (final ProjectType projectType : list){
            LinearLayout linearLayout = new LinearLayout(getActivity());
            TextView textView = new TextView(getActivity());
            textView.setTextSize(24);
            textView.setText(projectType.toString());
            linearLayout.addView(textView);
            Button button = new Button(getActivity());
            button.setText(R.string.Subscribe);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onAddSubscriptionClicked(view, projectType);
                }
            });
            linearLayout.addView(button);
            layout.addView(linearLayout);
        }
        ImageButton backButton = view.findViewById(R.id.searchResultsViewHeadLineBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackButtonPressed();
            }
        });
    }

    private List<String> getTagNamesStartingWithText(final String string){
        if(string.equals("")){
            return new ArrayList<>();
        }
        List<ProjectType> projectTypes = Arrays.asList(ProjectType.values());
        List<String> res = new ArrayList<>();
        for (ProjectType projectType : projectTypes){
            if(projectType.toString().startsWith(string)){
                res.add(projectType.toString());
            }
        }
        return res;
    }

    private List<ProjectType> getTagsStartingWithText(final String string){
        if(string.equals("")){
            return new ArrayList<>();
        }
        List<ProjectType> projectTypes = Arrays.asList(ProjectType.values());
        List<ProjectType> res = new ArrayList<>();
        for (ProjectType projectType : projectTypes){
            if(projectType.toString().startsWith(string)){
                res.add(projectType);
            }
        }
        return res;
    }
}
