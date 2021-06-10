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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;
import ru.project.iidea.network.NetworkConnectionChecker;

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
        final Response response1 = (Response) this.getArguments().get("response");
        if(NetworkConnectionChecker.isNetworkAvailable(getContext())){
            IideaBackendService server = IideaBackend.getInstance().getService();
            server.user(response1.getFromUserId()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                    if(response.isSuccessful() && response.body() != null){
                        String projectName = getArguments().getString("projectName");
                        TextView textView = view.findViewById(R.id.responseViewProjectName);
                        TextView textView1 = view.findViewById(R.id.responseViewToUserPage);
                        textView.setText(projectName);
                        textView1.setText(response.body().getSurname() + " " + response.body().getName());
                        textView1.setClickable(true);
                        textView1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.onUserIdClicked(response.body());
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
                                activity.onRejectResponseButtonClicked(response1.id);
                            }
                        });
                    } else {
                        onFailure(call, new IOException());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    activity.showToast("Something went wrong.");
                    activity.onBackButtonPressed();
                }
            });
        } else {
            activity.showToast("No Internet connection.");
            activity.onBackButtonPressed();
        }
    }
}
