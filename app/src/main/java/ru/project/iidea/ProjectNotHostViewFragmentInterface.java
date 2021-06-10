package ru.project.iidea;

public interface ProjectNotHostViewFragmentInterface {
    void onUserIdClicked(User user);
    void onBackButtonPressed();
    void onRespondButtonClicked(long projectID);
    void showToast(String message);
}
