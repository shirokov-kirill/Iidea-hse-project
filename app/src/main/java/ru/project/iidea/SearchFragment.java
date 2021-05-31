package ru.project.iidea;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;
import ru.project.iidea.network.NetworkConnectionChecker;

public class SearchFragment extends Fragment {

    SearchFragmentInterface activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
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
        final EditText userText = view.findViewById(R.id.searchExpressionEditView);
        ImageButton searchButton = view.findViewById(R.id.clickableSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userText.getText().toString().equals("")){
                    activity.onSearchButtonClicked(new ArrayList<>());
                }
                Bundle bundle = getArguments();
                long userID = bundle.getLong("userID");
                IideaBackendService server = IideaBackend.getInstance().getService();
                if (NetworkConnectionChecker.isNetworkAvailable(getContext())) {
                    server.user(userID).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                            ProjectType[] projectTypes = ProjectType.values();
                            List<ProjectType> res = new ArrayList<>();
                            List<ProjectType> userProjectTypes = response.body().getSubscriptions();
                            for (ProjectType projectType : projectTypes) {
                                if (projectType.toString().startsWith(userText.getText().toString()) && !userProjectTypes.contains(projectType)) {
                                    res.add(projectType);
                                }
                            }
                            activity.onSearchButtonClicked(res);
                        }

                        @Override
                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                        }
                    });
                }
            }
        });
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
                if (str.isEmpty()) {
                    LinearLayout searchRecs = view.findViewById(R.id.layoutForSearchRecs);
                    searchRecs.removeAllViews();
                    return;
                }
                List<String> projectTypeNames = new ArrayList<>();
                Bundle bundle = getArguments();
                long userID = bundle.getLong("userID");
                IideaBackendService server = IideaBackend.getInstance().getService();
                if (NetworkConnectionChecker.isNetworkAvailable(getContext())) {
                    server.user(userID).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ProjectType[] projectTypes = ProjectType.values();
                                List<ProjectType> userProjectTypes = response.body().getSubscriptions();
                                for (ProjectType projectType : projectTypes) {
                                    String projectTypeUpperCase = projectType.toString().toUpperCase();
                                    String strUpperCase = str.toUpperCase();
                                    if (projectTypeUpperCase.startsWith(strUpperCase) && !userProjectTypes.contains(projectType)) {
                                        projectTypeNames.add(projectType.toString());
                                    }
                                }
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
                        }
                        @Override
                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                        }
                    });
                }
            }
        });
    }
}
