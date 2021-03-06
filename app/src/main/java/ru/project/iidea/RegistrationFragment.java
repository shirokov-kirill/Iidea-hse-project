package ru.project.iidea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.project.iidea.network.IideaBackend;
import ru.project.iidea.network.IideaBackendService;
import ru.project.iidea.network.NetworkConnectionChecker;

public class RegistrationFragment extends Fragment {

    private RegistrationFragmentInterface activity;
    private static final int RC_SIGN_IN = 123;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegistrationFragmentInterface){
            activity = (RegistrationFragmentInterface) context;
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("621982323742-2oselqnv8k1ce2qvrbcqnjjameejaihc.apps.googleusercontent.com")
                .requestServerAuthCode("621982323742-2oselqnv8k1ce2qvrbcqnjjameejaihc.apps.googleusercontent.com")
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/user.phonenumbers.read"))
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);
        SignInButton signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setVisibility(View.VISIBLE);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.sign_in_button) {
                    if (NetworkConnectionChecker.isNetworkAvailable(getContext())) {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    } else {
                        activity.showToast("Sorry. No internet connection.");
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
            IideaBackendService server = IideaBackend.getInstance().getService();
            server.auth(authCode).enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    if(response.isSuccessful() && response.body() != null){
                        IideaBackend server1 = IideaBackend.getInstance();
                        server1.setUserID(response.body());
                        server1.setUserToken(account.getIdToken());
                        activity.onRegistrationClick(response.body());
                    } else {
                        onFailure(call, new IOException("Error registration"));
                    }
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    activity.showToast("Error registering to server.");
                }
            });

        } catch (ApiException e) {
            e.printStackTrace();
            activity.onBackPressed();
        }
    }
}