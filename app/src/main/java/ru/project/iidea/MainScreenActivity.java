package ru.project.iidea;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.util.Objects;

public class MainScreenActivity extends AppCompatActivity {

    private enum FragmentTag{
        PROFILE,
        FEED,
        RESPONCIES,
        PROJECTS,
        SEARCH
    }

    private FragmentTag currentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        ProfileFragmentEditing profileFragmentEditing = new ProfileFragmentEditing();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_screen_activity_fragment_placement, profileFragmentEditing, "profile").commit();
        currentTag = FragmentTag.PROFILE;
        ImageButton imageButton = findViewById(R.id.lower_menu_profile_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile_button_pressed, null));
        imageButton = findViewById(R.id.lower_menu_feed_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.feed_button_notpressed, null));
        imageButton = findViewById(R.id.lower_menu_search_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.search_button_notpressed, null));
        imageButton = findViewById(R.id.lower_menu_responces_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.responces_button_notpressed, null));
        imageButton = findViewById(R.id.lower_menu_myProjects_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.projects_button_notpressed, null));
    }

    private void updateBottomLine(@NonNull FragmentTag previousTag, @NonNull FragmentTag currentTag){
        if(Objects.equals(previousTag, currentTag)){
            return;
        }
        ImageButton imageButton;
        switch (previousTag){
            case PROFILE:
                imageButton = findViewById(R.id.lower_menu_profile_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile_button_notpressed,null));
            case FEED:
                imageButton = findViewById(R.id.lower_menu_feed_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.feed_button_notpressed,null));
            case RESPONCIES:
                imageButton = findViewById(R.id.lower_menu_responces_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.responces_button_notpressed,null));
            case PROJECTS:
                imageButton = findViewById(R.id.lower_menu_myProjects_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.projects_button_notpressed,null));
            case SEARCH:
                imageButton = findViewById(R.id.lower_menu_search_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.search_button_notpressed,null));
        }
        switch (currentTag){
            case PROFILE:
                imageButton = findViewById(R.id.lower_menu_profile_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile_button_notpressed,null));
                return;
            case FEED:
                imageButton = findViewById(R.id.lower_menu_feed_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.feed_button_pressed,null));
                return;
            case RESPONCIES:
                imageButton = findViewById(R.id.lower_menu_responces_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.responces_button_pressed,null));
                return;
            case PROJECTS:
                imageButton = findViewById(R.id.lower_menu_myProjects_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.projects_button_pressed,null));
                return;
            case SEARCH:
                imageButton = findViewById(R.id.lower_menu_search_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.search_button_pressed,null));
        }
    }
}