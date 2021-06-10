package ru.project.iidea;

public interface ResponseViewFragmentInterface {
    void onBackButtonPressed();
    void onUserIdClicked(User user);
    void showToast(String message);
    void onRejectResponseButtonClicked(long responseID);
}
