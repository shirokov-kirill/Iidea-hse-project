package ru.project.iidea;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText userText = view.findViewById(R.id.searchExpressionEditView);
        userText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                List<String> projectTypeNames = getTagsStartingWithText(str);
                LinearLayout searchRecs = view.findViewById(R.id.layoutForSearchRecs);
                searchRecs.removeAllViews();
                for (String projectTypeName : projectTypeNames){
                    final TextView recomendation = new TextView(getContext());
                    recomendation.setClickable(true);
                    recomendation.setText(projectTypeName);
                    recomendation.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    recomendation.setTextSize(24);
                    final EditText askLine = view.findViewById(R.id.searchExpressionEditView);
                    recomendation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            askLine.setText(recomendation.getText());
                        }
                    });
                    searchRecs.addView(recomendation, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
        });
    }

    private List<String> getTagsStartingWithText(final String string){
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
}
