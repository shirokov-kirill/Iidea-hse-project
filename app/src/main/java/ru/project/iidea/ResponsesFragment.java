package ru.project.iidea;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
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
        float scale = getResources().getDisplayMetrics().density;
        profileListOfSubs.setPadding((int) (8 * scale + 0.5f), (int) (8 * scale + 0.5f),
                (int) (8 * scale + 0.5f), (int) (8 * scale + 0.5f));
        List<Long> projectIDs = (List<Long>) this.getArguments().getSerializable("userProjectIDs");
        IideaBackendService server = IideaBackend.getInstance().getService();
        if(!projectIDs.isEmpty()){
            if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
                for (long projectID : projectIDs) {
                    server.responsesTo(projectID).enqueue(new Callback<List<Long>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Long>> call, @NonNull Response<List<Long>> response) {
                            if(response.isSuccessful() && response.body() != null){
                                List<Long> responseIDs = response.body();
                                if(responseIDs.isEmpty()){
                                    return;
                                }
                                for (long responseID : responseIDs){
                                    server.response(responseID).enqueue(new Callback<BackendResponse>() {
                                        @Override
                                        public void onResponse(@NonNull Call<BackendResponse> call, @NonNull Response<BackendResponse> response) {
                                            if(response.isSuccessful() && response.body() != null){
                                                BackendResponse backendResponse = response.body();
                                                TextView textView = new TextView(getContext());
                                                ru.project.iidea.Response response1 = new ru.project.iidea.Response(backendResponse.getId(), backendResponse.getToProject(), backendResponse.getFromUser());
                                                textView.setText(response1.toString());
                                                textView.setTextSize(21);
                                                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                                textView.setClickable(true);
                                                textView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        activity.openCurrentResponse(response1);
                                                    }
                                                });
                                                profileListOfSubs.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                View separator = new View(getContext());
                                                separator.setBackgroundColor(Color.parseColor("#808080"));
                                                LinearLayout.LayoutParams separatorLayoutParams = new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT, (int) (scale + 0.5f));
                                                separatorLayoutParams.setMargins(0, (int) (10 * scale + 0.5f), 0, (int) (10 * scale + 0.5f));
                                                separator.setLayoutParams(separatorLayoutParams);
                                                profileListOfSubs.addView(separator);
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<BackendResponse> call, @NonNull Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            } else {
                                onFailure(call, new IOException("DB connection error."));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Long>> call, @NonNull Throwable t) {
                            activity.showToast("Error while uploading responses.");
                        }
                    });
                }
            } else {
                activity.showToast("No Internet connection");
            }
        } else {
            TextView textView = new TextView(this.getContext());
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            textView.setText(R.string.NoResponses);
            textView.setTextSize(30);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            profileListOfSubs.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
