package ru.project.iidea;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

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
        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}