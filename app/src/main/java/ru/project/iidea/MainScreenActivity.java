package ru.project.iidea;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.Serializable;
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
        RESPONSES,
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
                case RESPONSES:
                    return "responses";
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
        currentTag = FragmentTag.PROFILE;
        ImageButton imageButton = findViewById(R.id.lower_menu_profile_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile_button_pressed, null));
        imageButton = findViewById(R.id.lower_menu_feed_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.feed_button_notpressed, null));
        imageButton = findViewById(R.id.lower_menu_search_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.search_button_notpressed, null));
        imageButton = findViewById(R.id.lower_menu_responses_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.responces_button_notpressed, null));
        imageButton = findViewById(R.id.lower_menu_myProjects_button);
        imageButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.projects_button_notpressed, null));
        long userID = 1;//changeable
        server.user(userID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null){
                    myUser = response.body();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userId", myUser.getId());
                    profileFragmentEditing.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.main_screen_activity_fragment_placement, profileFragmentEditing, FragmentTag.PROFILE.toString()).commit();
                } else {
                    onFailure(call, new IOException("Critical error."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                showToast("Something is wrong.");
                onBackPressed();
            }
        });
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
            case RESPONSES:
                imageButton = findViewById(R.id.lower_menu_responses_button);
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
            case RESPONSES:
                imageButton = findViewById(R.id.lower_menu_responses_button);
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
        bundle.putSerializable("userID", myUser.getId());
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("userID", myUser.getId());
        searchFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, searchFragment, FragmentTag.SEARCH.toString()).addToBackStack(null).commit();
        updateBottomLine(currentTag, FragmentTag.SEARCH);
        currentTag = FragmentTag.SEARCH;
    }

    public void responsesButtonOnClick(View view) {
        if (currentTag == FragmentTag.RESPONSES) {
            return;
        }
        ResponsesFragment responsesFragment = new ResponsesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userProjectIDs", (Serializable) myUser.getProjects());
        responsesFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_activity_fragment_placement, responsesFragment, FragmentTag.RESPONSES.toString()).addToBackStack(null).commit();
        updateBottomLine(currentTag, FragmentTag.RESPONSES);
        currentTag = FragmentTag.RESPONSES;
    }

    public void profileButtonOnClick(View view) {
        if (currentTag == FragmentTag.PROFILE) {
            return;
        }
        ProfileFragmentEditing profileFragmentEditing = new ProfileFragmentEditing();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userId", myUser.getId());
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
    public void unsubscribeOnTag(String tag, LinearLayout linearLayout) {
        if(NetworkConnectionChecker.isNetworkAvailable(this)){
            server.unsubscribe(tag).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()){
                        linearLayout.setVisibility(View.GONE);
                    } else {
                        onFailure(call, new IOException("Error thrown from server."));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    showToast("Something went wrong, please try again.");
                }
            });
        } else {
            showToast("No internet connection.");
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
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()){
                        onBackPressed();
                    } else {
                        onFailure(call, new IOException("Something went wrong. Please try again."));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()){
                        onBackButtonPressed();
                        onBackButtonPressed();
                        showToast("Successfully modified.");
                    } else {
                        onFailure(call, new IOException("some error happened."));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()){
                        //TODO изменить стиль кнопки
                    } else {
                        onFailure(call, new IOException("Wrong server answer"));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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
                public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                    if(response.isSuccessful() && response.body() != null){
                        long id = response.body();
                        myUser.addProject(id);
                        onBackButtonPressed();
                    } else {
                        onFailure(call, new IOException("Error in server connection."));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {
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
        if (NetworkConnectionChecker.isNetworkAvailable(this)) {
            server.subscribe(type.toString()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()){
                        view.setClickable(false);
                        showToast("Вы успешно подписались на " + type.toString());
                    } else {
                        onFailure(call, new IOException("Server error."));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(response.isSuccessful()){
                        onBackPressed();
                    } else {
                        onFailure(call, new IOException("Some error."));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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
        if (NetworkConnectionChecker.isNetworkAvailable(this)){
            server.project(response.getProjectId()).enqueue(new Callback<Project>() {
                @Override
                public void onResponse(@NonNull Call<Project> call, @NonNull Response<Project> response) {
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
                public void onFailure(@NonNull Call<Project> call, Throwable t) {
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
            case "responses":
                return FragmentTag.RESPONSES;
            case "responseView":
                return FragmentTag.RESPONSES;
            case "projectHostEdit":
                return FragmentTag.PROJECTS;
            case "projectHostView":
                return FragmentTag.PROJECTS;
            default:
                return FragmentTag.PROFILE;
        }
    }
}