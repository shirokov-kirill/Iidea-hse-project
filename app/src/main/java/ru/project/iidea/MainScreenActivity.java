package ru.project.iidea;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainScreenActivity extends AppCompatActivity {

    private enum FragmentTag{
        PROFILE,
        FEED,
        RESPONCIES,
        PROJECTS,
        SEARCH;


        @NonNull
        @Override
        public String toString() {
            switch (this){
                case PROFILE:
                    return "profile";
                case SEARCH:
                    return "search";
                case FEED:
                    return "feed";
                case PROJECTS:
                    return "projects";
                case RESPONCIES:
                    return "responcies";
            }
            return "";
        }
    }

    private FragmentTag currentTag;
    private User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        ProfileFragmentEditing profileFragmentEditing = new ProfileFragmentEditing();
        //Integer host_id = getIdByToken(this.getIntent().getExtras().getString("token"));//toServer
        //User myUser = getUserById(host_id);//toServer
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(ProjectType.IT, "FirstPr", "", 1, ProjectState.InProgress));
        myUser = new User(1, "Shirokov", "Kirill", "", "25", "k.s.shirokov@mail.ru", "+7921", "Hello everione. I created my own project. I want to do everithing well.", UserState.SEEKING, new ArrayList<ProjectType>(), projects);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", myUser);
        profileFragmentEditing.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_screen_activity_fragment_placement, profileFragmentEditing, FragmentTag.PROFILE.toString()).commit();
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
                break;
            case FEED:
                imageButton = findViewById(R.id.lower_menu_feed_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.feed_button_notpressed,null));
                break;
            case RESPONCIES:
                imageButton = findViewById(R.id.lower_menu_responces_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.responces_button_notpressed,null));
                break;
            case PROJECTS:
                imageButton = findViewById(R.id.lower_menu_myProjects_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.projects_button_notpressed,null));
                break;
            case SEARCH:
                imageButton = findViewById(R.id.lower_menu_search_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.search_button_notpressed,null));
        }
        switch (currentTag){
            case PROFILE:
                imageButton = findViewById(R.id.lower_menu_profile_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile_button_pressed,null));
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

    public void feedButtonOnClick(View view){

    }

    public void searchButtonOnClick(View view){

    }

    public void responcesButtonOnClick(View view){

    }

    public void profileButtonOnClick(View view){
        if(currentTag == FragmentTag.PROFILE){
            return;
        }
        ProfileFragmentEditing profileFragmentEditing = new ProfileFragmentEditing();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", myUser);
        profileFragmentEditing.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(currentTag.toString())).add(R.id.main_screen_activity_fragment_placement, profileFragmentEditing, FragmentTag.PROFILE.toString()).commit();
        updateBottomLine(currentTag, FragmentTag.PROFILE);
        currentTag = FragmentTag.PROFILE;
    }

    public void myProjectsButtonOnClick(View view){
        if(currentTag == FragmentTag.PROJECTS){
            return;
        }
        MyProjectsFragment myProjectsFragment = new MyProjectsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", myUser);
        myProjectsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(currentTag.toString())).add(R.id.main_screen_activity_fragment_placement, myProjectsFragment, FragmentTag.PROJECTS.toString()).commit();
        updateBottomLine(currentTag, FragmentTag.PROJECTS);
        currentTag = FragmentTag.PROJECTS;
    }
}