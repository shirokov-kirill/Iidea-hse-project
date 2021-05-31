package ru.project.iidea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import ru.project.iidea.network.NetworkConnectionChecker;

public class MainActivity extends BaseActivity implements RegistrationFragmentInterface{

    String currentTag;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegistrationFragment registrationFragment = new RegistrationFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_activity_registration_layout, registrationFragment, "registrationFragment").commit();
        currentTag = "registrationFragment";
    }

    public void onClick(View view) {
        if (NetworkConnectionChecker.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, MainScreenActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
        } else {
            showToast("No internet connection.");
        }
    }
}