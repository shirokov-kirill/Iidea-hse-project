package ru.project.iidea;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.project.iidea.network.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreenActivity
        extends
        BaseActivity
        implements
        ProfileFragmentViewInterface,
        ProfileFragmentEditingInterface,
        AddUserDescriptionFragmentInterface,
        SearchFragmentInterface,
        ProjectNotHostViewFragmentInterface,
        FeedFragmentInterface,
        ResponseViewFragmentInterface,
        ProjectHostViewInterface,
        ProjectHostEditInterface,
        NewProjectFragmentInterface,
        ResponsesFragmentInterface,
        MyProjectsFragmentInterface{

    private enum FragmentTag {
        PROFILE,
        FEED,
        RESPONCIES,
        PROJECTS,
        SEARCH;


        @NonNull
        @Override
        public String toString() {
            switch (this) {
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
    private IideaBackendService server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        ProfileFragmentEditing profileFragmentEditing = new ProfileFragmentEditing();
        server = IideaBackend.getInstance().getService();
        Thread thread1 = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    myUser = server.user(2).execute().body();
                    System.out.println(myUser);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        try{
            thread1.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
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

    private void updateBottomLine(@NonNull FragmentTag previousTag, @NonNull FragmentTag currentTag) {
        if (Objects.equals(previousTag, currentTag)) {
            return;
        }
        ImageButton imageButton;
        switch (previousTag) {
            case PROFILE:
                imageButton = findViewById(R.id.lower_menu_profile_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile_button_notpressed, null));
                break;
            case FEED:
                imageButton = findViewById(R.id.lower_menu_feed_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.feed_button_notpressed, null));
                break;
            case RESPONCIES:
                imageButton = findViewById(R.id.lower_menu_responces_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.responces_button_notpressed, null));
                break;
            case PROJECTS:
                imageButton = findViewById(R.id.lower_menu_myProjects_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.projects_button_notpressed, null));
                break;
            case SEARCH:
                imageButton = findViewById(R.id.lower_menu_search_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.search_button_notpressed, null));
        }
        switch (currentTag) {
            case PROFILE:
                imageButton = findViewById(R.id.lower_menu_profile_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile_button_pressed, null));
                return;
            case FEED:
                imageButton = findViewById(R.id.lower_menu_feed_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.feed_button_pressed, null));
                return;
            case RESPONCIES:
                imageButton = findViewById(R.id.lower_menu_responces_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.responces_button_pressed, null));
                return;
            case PROJECTS:
                imageButton = findViewById(R.id.lower_menu_myProjects_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.projects_button_pressed, null));
                return;
            case SEARCH:
                imageButton = findViewById(R.id.lower_menu_search_button);
                imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.search_button_pressed, null));
        }
    }

    public void feedButtonOnClick(View view) {
        if (currentTag == FragmentTag.FEED) {
            return;
        }
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", myUser);
        feedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, feedFragment, FragmentTag.FEED.toString()).addToBackStack(null).commit();
        updateBottomLine(currentTag, FragmentTag.FEED);
        currentTag = FragmentTag.FEED;
    }

    public void searchButtonOnClick(View view) {
        if (currentTag == FragmentTag.SEARCH) {
            return;
        }
        SearchFragment searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, searchFragment, FragmentTag.SEARCH.toString()).addToBackStack(null).commit();
        updateBottomLine(currentTag, FragmentTag.SEARCH);
        currentTag = FragmentTag.SEARCH;
    }

    public void responcesButtonOnClick(View view) {
        if (currentTag == FragmentTag.RESPONCIES) {
            return;
        }
        ResponsesFragment responsesFragment = new ResponsesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userProjectIDs", (Serializable) myUser.getProjects());
        responsesFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, responsesFragment, FragmentTag.RESPONCIES.toString()).addToBackStack(null).commit();
        updateBottomLine(currentTag, FragmentTag.RESPONCIES);
        currentTag = FragmentTag.RESPONCIES;
    }

    public void profileButtonOnClick(View view) {
        if (currentTag == FragmentTag.PROFILE) {
            return;
        }
        ProfileFragmentEditing profileFragmentEditing = new ProfileFragmentEditing();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", myUser);
        profileFragmentEditing.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, profileFragmentEditing, FragmentTag.PROFILE.toString()).addToBackStack(null).commit();
        updateBottomLine(currentTag, FragmentTag.PROFILE);
        currentTag = FragmentTag.PROFILE;
    }

    public void myProjectsButtonOnClick(View view) {
        if (currentTag == FragmentTag.PROJECTS) {
            return;
        }
        MyProjectsFragment myProjectsFragment = new MyProjectsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userID", myUser.getId());
        myProjectsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, myProjectsFragment, FragmentTag.PROJECTS.toString()).addToBackStack(null).commit();
        updateBottomLine(currentTag, FragmentTag.PROJECTS);
        currentTag = FragmentTag.PROJECTS;
    }

    @Override
    public void onSaveButtonInAddUserDescriptionClicked(String newDescription){
        getSupportFragmentManager().popBackStack();
        //TODO отправить описание на сервер
    }

    @Override
    public void onAddDescriptionButtonClicked() {
        if(currentTag == FragmentTag.PROFILE){
            AddUserDescriptionFragment addUserDescriptionFragment = new AddUserDescriptionFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, addUserDescriptionFragment, "addUserDescription").addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackButtonPressed() {
        onBackPressed();
    }

    @Override
    public void onDeleteProjectPressed(long projectID) {
        if(NetworkConnectionChecker.isNetworkAvailable(this)){
            server.deleteProject(projectID).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        onBackPressed();
                    } else {
                        onFailure(call, new IOException("Something went wrong. Please try again."));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Something went wrong. Please try again.");
                }
            });
        } else {
            showToast("No Internet Connection");
        }
    }

    @Override
    public void onSaveProjectPressed(String name, String description, String state, String type, long id) {
        if(NetworkConnectionChecker.isNetworkAvailable(this)){
            server.updateProject(id, name, type, description, state).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        onBackButtonPressed();
                        onBackButtonPressed();
                        showToast("Successfully modified.");
                    } else {
                        onFailure(call, new IOException("some error happened."));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Something went wrong. Please try again.");
                }
            });
        } else {
            showToast("No Internet connection.");
        }
    }

    @Override
    public void onRespondButtonClicked(long projectID) {
        if(!myUser.getProjects().contains(projectID)){
            server.respondTo(projectID).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        //TODO
                    } else {
                        onFailure(call, new IOException("Wrong server answer"));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Error. Please try again");
                }
            });
        } else {
            showToast("You can't respond your own project.");
        }
    }

    public void newProjectOnClick(View view) {
        NewProjectFragment newProjectFragment = new NewProjectFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_screen_activity_fragment_placement, newProjectFragment, "newProjectCreate").addToBackStack(null).commit();
    }

    @Override
    public void onCreateNewProjectClicked(String projectType, String name, String description, String projectState) {
        if(NetworkConnectionChecker.isNetworkAvailable(this)){
            server.createProject(name, projectType, description, projectState).enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    if(response.isSuccessful() && response.body() != null){
                        long id = response.body();
                        myUser.addProject(id);
                        onBackButtonPressed();
                    } else {
                        onFailure(call, new IOException("Error in server connection."));
                    }
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    showToast("Error creating project. Please try again.");
                }
            });
        } else {
            showToast("No Internet connection.");
        }
    }

    @Override
    public void onSearchButtonClicked(List<ProjectType> types) {
        EditText askline =  getSupportFragmentManager().findFragmentByTag(currentTag.toString()).getView().findViewById(R.id.searchExpressionEditView);
        String ask = askline.getText().toString();
        if(!ask.equals("")){
            SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("projectTypes", (Serializable) types);
            searchResultsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, searchResultsFragment, "viewSearchResults").addToBackStack(null).commit();
        }
    }

    @Override
    public void onAddSubscriptionClicked(View view, ProjectType type) {
        if(NetworkConnectionChecker.isNetworkAvailable(this)){
            server.subscribe(type.toString()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        //TODO change button style
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Something went wrong. Please try again.");
                }
            });
        } else {
            showToast("No Internet connection.");
        }
    }

    @Override
    public void onProjectBlockInMyProjectsClicked(Project project) {
        ProjectHostView projectHostView = new ProjectHostView();
        Bundle bundle = new Bundle();
        bundle.putSerializable("project", project);
        projectHostView.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, projectHostView, "projectHostView").addToBackStack(null).commit();
    }

    public void editProject(View view) {
        ProjectHostEdit projectHostEdit = new ProjectHostEdit();
        Bundle bundle = getSupportFragmentManager().findFragmentByTag("projectHostView").getArguments();
        projectHostEdit.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_screen_activity_fragment_placement, projectHostEdit, "projectHostEdit").addToBackStack(null).commit();
    }

    @Override
    public void onUserIdClicked(final long userID) {
        ProfileFragmentView profileFragmentView = new ProfileFragmentView();
        Bundle bundle = new Bundle();
        bundle.putLong("userID", userID);
        profileFragmentView.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, profileFragmentView, "showHostProfile").addToBackStack(null).commit();
    }

    @Override
    public void onRejectResponseButtonClicked(long responseID) {
        if(NetworkConnectionChecker.isNetworkAvailable(this)){
            server.deleteResponse(responseID).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        onBackPressed();
                    } else {
                        onFailure(call, new IOException("Some error."));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Some error occurred. Please try again.");
                }
            });
        } else {
            showToast("No Internet connection.");
        }
    }

    @Override
    public void onProjectBlockClicked(Project project) {
        ProjectNotHostViewFragment projectNotHostViewFragment = new ProjectNotHostViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("project", project);
        projectNotHostViewFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, projectNotHostViewFragment, "showProjectInfo").addToBackStack(null).commit();
    }

    @Override
    public void openCurrentResponse(ru.project.iidea.Response response) {
        ResponseViewFragment responseViewFragment = new ResponseViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("response", response);
        if(NetworkConnectionChecker.isNetworkAvailable(this)){
            server.project(response.getProjectId()).enqueue(new Callback<Project>() {
                @Override
                public void onResponse(Call<Project> call, Response<Project> response) {
                    if(response.isSuccessful() && response.body() != null){
                        bundle.putString("projectName", response.body().getName());
                        responseViewFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_screen_activity_fragment_placement, responseViewFragment, "responseView").addToBackStack(null).commit();
                    } else {
                        onFailure(call, new IOException("Error connecting to database."));
                    }
                }

                @Override
                public void onFailure(Call<Project> call, Throwable t) {
                    showToast("Something went wrong, please try again.");
                    t.printStackTrace();
                }
            });
        } else {
            showToast("No Internet connection");
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_screen_activity_fragment_placement);
            FragmentTag fragmentTag = tagStringToFragmentTag(fragment.getTag());
            updateBottomLine(currentTag, fragmentTag);
            currentTag = fragmentTag;
        }
    }

    private FragmentTag tagStringToFragmentTag(String tag){
        switch (tag){
            case "profile":
                return FragmentTag.PROFILE;
            case "addUserDescription":
                return FragmentTag.PROFILE;
            case "newProjectCreate":
                return FragmentTag.PROJECTS;
            case "viewSearchResults":
                return FragmentTag.SEARCH;
            case "showProjectInfo":
                return FragmentTag.FEED;
            case "search":
                return FragmentTag.SEARCH;
            case "showHostProfile":
                return FragmentTag.FEED;
            case "feed":
                return FragmentTag.FEED;
            case "projects":
                return FragmentTag.PROJECTS;
            case "responcies":
                return FragmentTag.RESPONCIES;
            case "responseView":
                return FragmentTag.RESPONCIES;
            case "projectHostEdit":
                return FragmentTag.PROJECTS;
            case "projectHostView":
                return FragmentTag.PROJECTS;
            default:
                return FragmentTag.PROFILE;
        }
    }
}