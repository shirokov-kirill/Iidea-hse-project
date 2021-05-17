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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.BackendResponse;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;
import ru.project.iidea.network.NetworkConnectionChecker;

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
        List<Long> projectIDs = (List<Long>) this.getArguments().getSerializable("userProjectIDs");
        IideaBackendService server = IideaBackend.getInstance().getService();
        if(!projectIDs.isEmpty()){
            if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
                for (long projectID : projectIDs) {
                    server.responsesTo(projectID).enqueue(new Callback<List<Long>>() {
                        @Override
                        public void onResponse(Call<List<Long>> call, Response<List<Long>> response) {
                            if(response.isSuccessful() && response.body() != null){
                                List<Long> responseIDs = response.body();
                                for (long responseID : responseIDs){
                                    server.response(responseID).enqueue(new Callback<BackendResponse>() {
                                        @Override
                                        public void onResponse(Call<BackendResponse> call, Response<BackendResponse> response) {
                                            if(response.isSuccessful() && response.body() != null){
                                                BackendResponse backendResponse = response.body();
                                                TextView textView = new TextView(getContext());
                                                ru.project.iidea.Response response1 = new ru.project.iidea.Response(backendResponse.getId(), backendResponse.getToProject(), backendResponse.getFromUser());
                                                textView.setText(response1.toString());
                                                textView.setTextSize(21);
                                                textView.setClickable(true);
                                                textView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        activity.openCurrentResponse(response1);
                                                    }
                                                });
                                                profileListOfSubs.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BackendResponse> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            } else {
                                onFailure(call, new IOException("DB connection error."));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Long>> call, Throwable t) {

                        }
                    });
                }
            } else {
                activity.showToast("No Internet connection");
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
